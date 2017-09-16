package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttributeType;
import org.iplantc.de.client.models.diskResources.TemplateAttributeSelectionItem;
import org.iplantc.de.client.models.ontologies.OntologyClass;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceDoc;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.validators.UrlValidator;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.presenters.metadata.MetadataPresenterImpl;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.MetadataTermGuideDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sriram on 3/23/17.
 */
public class MetadataTemplateView implements IsWidget {

    private VerticalLayoutContainer widget;
    private final DateTimeFormat timestampFormat;
    private boolean writable;
    private boolean valid;

    FastMap<Avu> templateTagAvuMap;
    FastMap<MetadataTemplateAttribute> templateTagAttrMap;
    FastMap<Field<?>> templateTagFieldMap;
    List<MetadataTemplateAttribute> attributes;
    FastMap<VerticalLayoutContainer> templateAttrVLCMap;
    List<Avu> templateMd;
    MetadataView.Presenter presenter;

    MetadataView.Appearance appearance;

    @Inject
    AsyncProviderWrapper<MetadataTermGuideDialog> termGuideDialogProvider;
    private IPlantAnchor helpLink;

    @Inject
    public MetadataTemplateView(MetadataView.Appearance appearance) {
        templateTagAvuMap = new FastMap<>();
        templateTagFieldMap = new FastMap<>();
        templateTagAttrMap = new FastMap<>();
        templateAttrVLCMap = new FastMap<>();
        this.appearance = appearance;
        timestampFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
        valid = true;
        widget = new VerticalLayoutContainer();
        widget.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        widget.getElement().applyStyles(appearance.backgroundStyle());
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public ArrayList<Avu> getMetadataFromTemplate() {
        ArrayList<Avu> avus = new ArrayList<>();
        templateTagAttrMap.keySet().forEach(tag -> {
            Avu avu = templateTagAvuMap.get(tag);
            if (avu == null) {
                MetadataTemplateAttribute metadataTemplateAttribute = templateTagAttrMap.get(tag);
                avu = MetadataPresenterImpl.newMetadata(metadataTemplateAttribute.getName(),
                                                        "",
                                                        ""); //$NON-NLS-1$ //$NON-NLS-2$
            }

            Field<?> field = templateTagFieldMap.get(tag);
            if (field.getValue() != null) {
                String value = field.getValue().toString();
                if ((field instanceof DateField) && !Strings.isNullOrEmpty(value)) {
                    value = timestampFormat.format(((DateField)field).getValue());
                } else if (field instanceof ComboBox<?>) {
                    Object fieldValue = field.getValue();

                    if (fieldValue instanceof TemplateAttributeSelectionItem) {
                        value = ((TemplateAttributeSelectionItem)fieldValue).getValue();
                    } else if (fieldValue instanceof OntologyClass) {
                        value = ((OntologyClass)fieldValue).getLabel();
                    }
                }

                avu.setValue(value);
            }
            avus.add(avu);

        });
        return avus;
    }

    public boolean isValid() {
        List<IsField<?>> fields = FormPanelHelper.getFields(widget);
        for (IsField<?> f : fields) {
            if (!f.isValid(false)) {
                valid = false;
            }
        }

        return valid;
    }

    public void initTemplate(MetadataView.Presenter presenter,
                             List<Avu> templateMd,
                             boolean writable,
                             List<MetadataTemplateAttribute> attributes) {
        this.presenter = presenter;
        this.writable = writable;
        this.attributes = attributes;
        this.templateMd = templateMd;
        templateTagFieldMap.clear();
        buildAvuMap();
        addFields();
    }

    public void buildMdTermDictionary(List<MetadataTemplateAttribute> attributes, String headerText) {
        if(helpLink == null) {
            buildHelpLink(attributes, headerText);
        }
        attachMdTermDictionary();
    }

    private void attachMdTermDictionary() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        hp.add(helpLink);
        widget.add(hp, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
    }


    private void buildAvuMap() {
        for (MetadataTemplateAttribute attribute : attributes) {
            List<Avu> mds = findMetadataForAttribute(attribute.getName());
            if (mds != null && !mds.isEmpty()) {
                mds.forEach(md -> {
                    String tag = getTagForMetadata(md);
                    templateTagAvuMap.put(tag, md);
                    templateTagAttrMap.put(tag, attribute);
                });
            } else {
                Avu avu = presenter.setAvuModelKey(MetadataPresenterImpl.newMetadata(attribute.getName(),
                                                                                     "",
                                                                                     "")); //$NON-NLS-1$ //$NON-NLS-2$
                String tag = getTagForMetadata(avu);
                templateTagAvuMap.put(tag, avu);
                templateTagAttrMap.put(tag, attribute);
            }

        }
    }

    private List<Avu> findMetadataForAttribute(String attribute) {
        if (templateMd != null) {
            List<Avu> mds = new ArrayList<>();
            for (Avu md : templateMd) {
                if (md.getAttribute().equals(attribute)) {
                    mds.add(md);
                }
            }
            return mds;
        }
        return null;
    }

    private CheckBox buildBooleanField(String tag, MetadataTemplateAttribute attribute) {
        CheckBox cb = new CheckBox();

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null && !Strings.isNullOrEmpty(avu.getValue())) {
            cb.setValue(Boolean.valueOf(avu.getValue()));
        }

        // CheckBox fields can still be (un)checked when setReadOnly is set to true.
        cb.setEnabled(writable);

        return cb;
    }

    private DateField buildDateField(String tag, MetadataTemplateAttribute attribute) {
        final DateField tf = new DateField(new DateTimePropertyEditor(timestampFormat));
        tf.setAllowBlank(!attribute.isRequired());
        if (writable) {
            tf.setEmptyText(timestampFormat.format(new Date(0)));
        }

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null && !Strings.isNullOrEmpty(avu.getValue())) {
            try {
                tf.setValue(timestampFormat.parse(avu.getValue()));
            } catch (Exception e) {
                GWT.log(avu.getValue(), e);
            }
        }

        return tf;
    }

    private FieldLabel buildFieldLabel(IsWidget widget,
                                       String lbl,
                                       String description,
                                       boolean allowBlank) {
        FieldLabel fl = new FieldLabel(widget);
        if (!(widget instanceof CheckBox)) {
            fl.setHTML(appearance.buildLabelWithDescription(lbl, description, allowBlank));
        } else {
            // always set allow blank to true for checkbox
            fl.setHTML(appearance.buildLabelWithDescription(lbl, description, true));
        }
        new QuickTip(fl);
        fl.setLabelAlign(FormPanel.LabelAlign.TOP);
        return fl;
    }

    private NumberField<Integer> buildIntegerField(String tag, MetadataTemplateAttribute attribute) {
        NumberField<Integer> nf = new NumberField<>(new NumberPropertyEditor.IntegerPropertyEditor());
        nf.setAllowBlank(!attribute.isRequired());
        nf.setAllowDecimals(false);
        nf.setAllowNegative(true);

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null && !Strings.isNullOrEmpty(avu.getValue())) {
            nf.setValue(new Integer(avu.getValue()));
        }

        return nf;
    }

    private NumberField<Double> buildNumberField(String tag, MetadataTemplateAttribute attribute) {
        NumberField<Double> nf = new NumberField<>(new NumberPropertyEditor.DoublePropertyEditor());
        nf.setAllowBlank(!attribute.isRequired());
        nf.setAllowDecimals(true);
        nf.setAllowNegative(true);

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null && !Strings.isNullOrEmpty(avu.getValue())) {
            nf.setValue(new Double(avu.getValue()));
        }

        return nf;
    }

    private TextArea buildTextArea(String tag, MetadataTemplateAttribute attribute) {
        TextArea area = new TextArea();
        area.setAllowBlank(!attribute.isRequired());
        area.setHeight(200);

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null) {
            area.setValue(avu.getValue());
        }

        return area;
    }

    private TextField buildTextField(String tag, MetadataTemplateAttribute attribute) {
        TextField fld = new TextField();
        fld.setAllowBlank(!attribute.isRequired());

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null) {
            fld.setValue(avu.getValue());
        }

        return fld;
    }

    private TextField buildURLField(String tag, MetadataTemplateAttribute attribute) {
        TextField tf = buildTextField(tag, attribute);
        tf.addValidator(new UrlValidator());
        if (writable) {
            tf.setEmptyText(appearance.urlGhostText());
        }
        return tf;
    }

    private ComboBox<OntologyLookupServiceDoc> buildOntologyField(String tag, MetadataTemplateAttribute attribute) {
        ComboBox<OntologyLookupServiceDoc> combo = presenter.createMetadataTermSearchField(attribute).asField();

        combo.setAllowBlank(!attribute.isRequired());

        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null) {
            combo.setText(avu.getValue());
        }

        return combo;
    }


    private void addFields() {
        templateTagAttrMap.keySet().forEach(tag -> {
            Field<?> field = getAttributeValueWidget(tag);
            templateTagFieldMap.put(tag, field);
            addFieldToTemplate(tag, templateTagAttrMap.get(tag), field);
        });
    }


    private void addFieldToTemplate(final String tag,
                                    final MetadataTemplateAttribute attribute,
                                    Field<?> field) {
        if (field != null) {
            field.setReadOnly(!writable);
            FieldLabel lbl = buildFieldLabel(field,
                                             attribute.getName(),
                                             attribute.getDescription(),
                                             !attribute.isRequired());
            if (field instanceof CheckBox || field instanceof TextArea) {
                widget.add(lbl, new VerticalLayoutContainer.VerticalLayoutData(.90, -1));
            } else {
                HorizontalPanel panel = buildMultiField(tag, attribute, lbl);
                VerticalLayoutContainer vlc = findVLCForAttribute(attribute);
                if (vlc == null) {
                    vlc = buildVLC(attribute);
                    vlc.add(panel);
                    widget.add(vlc, new VerticalLayoutContainer.VerticalLayoutData(.90, -1));
                } else {
                    vlc.add(panel);
                }
            }
            widget.forceLayout();
        }
    }

    private HorizontalPanel buildMultiField(final String tag,
                                            final MetadataTemplateAttribute attribute,
                                            final FieldLabel field) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(field);
        panel.setSpacing(10);
        field.setWidth("540px");
        TextButton addBtn = new TextButton("+");
        addBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Avu avu = presenter.setAvuModelKey(MetadataPresenterImpl.newMetadata(attribute.getName(),
                                                                                     "",
                                                                                     "")); //$NON-NLS-1$ //$NON-NLS-2$
                String newtag = getTagForMetadata(avu);
                templateTagAvuMap.put(newtag, avu);
                templateTagAttrMap.put(newtag, attribute);
                Field<?> field = getAttributeValueWidget(newtag);
                templateTagFieldMap.put(newtag, field);
                addFieldToTemplate(newtag, attribute, field);
            }
        });

        TextButton remBtn = new TextButton("-");
        remBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                templateTagAttrMap.remove(tag);
                templateTagAvuMap.remove(tag);
                templateTagFieldMap.remove(tag);
                templateAttrVLCMap.clear();
                widget.mask(appearance.loading());
                widget.clear();
                attachMdTermDictionary();
                addFields();
                widget.unmask();
            }
        });

        panel.add(addBtn);
        if (canFieldBeRemoved(attribute)) {
            panel.add(remBtn);
        }

        return panel;
    }

    VerticalLayoutContainer findVLCForAttribute(MetadataTemplateAttribute attribute) {
        return templateAttrVLCMap.get(attribute.getName());
    }

    VerticalLayoutContainer buildVLC(MetadataTemplateAttribute attribute) {
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        templateAttrVLCMap.put(attribute.getName(), vlc);
        return vlc;
    }

    private boolean canFieldBeRemoved(MetadataTemplateAttribute attribute) {
        List<MetadataTemplateAttribute> attrFilter = new ArrayList<>();
        templateTagAttrMap.values().forEach(attr -> {
            if (attr.equals(attribute)) {
                attrFilter.add(attr);
            }
        });
        return attrFilter.size() > 1;
    }

    private String getTagForMetadata(Avu md) {
        final AutoBean<Object> metadataBean = AutoBeanUtils.getAutoBean(md);
        return metadataBean.getTag(presenter.AVU_BEAN_TAG_MODEL_KEY);
    }

    /**
     * @param tag the template attribute
     * @return Field based on MetadataTemplateAttribute type.
     */
    private Field<?> getAttributeValueWidget(String tag) {
        MetadataTemplateAttribute attribute = templateTagAttrMap.get(tag);
        String type = attribute.getType();
        if (MetadataTemplateAttributeType.TIMESTAMP.toString().equalsIgnoreCase(type)) {
            return buildDateField(tag, attribute);
        } else if (MetadataTemplateAttributeType.BOOLEAN.toString().equalsIgnoreCase(type)) {
            return buildBooleanField(tag, attribute);
        } else if (MetadataTemplateAttributeType.NUMBER.toString().equalsIgnoreCase(type)) {
            return buildNumberField(tag, attribute);
        } else if (MetadataTemplateAttributeType.INTEGER.toString().equalsIgnoreCase(type)) {
            return buildIntegerField(tag, attribute);
        } else if (MetadataTemplateAttributeType.STRING.toString().equalsIgnoreCase(type)) {
            return buildTextField(tag, attribute);
        } else if (MetadataTemplateAttributeType.MULTILINE.toString().equalsIgnoreCase(type)) {
            return buildTextArea(tag, attribute);
        } else if (MetadataTemplateAttributeType.URL.toString().equalsIgnoreCase(type)) {
            return buildURLField(tag, attribute);
        } else if (MetadataTemplateAttributeType.OLS_ONTOLOGY_TERM.toString().equalsIgnoreCase(type)) {
            return buildOntologyField(tag, attribute);
        } else if (MetadataTemplateAttributeType.ENUM.toString().equalsIgnoreCase(type)) {
            return buildListField(tag, attribute);
        } else {
            return null;
        }
    }

    private ComboBox<TemplateAttributeSelectionItem> buildListField(String tag,
                                                                    MetadataTemplateAttribute attribute) {
        ListStore<TemplateAttributeSelectionItem> store =
                new ListStore<>(new ModelKeyProvider<TemplateAttributeSelectionItem>() {

                    @Override
                    public String getKey(TemplateAttributeSelectionItem item) {
                        return item.getId();
                    }
                });
        store.addAll(attribute.getValues());
        ComboBox<TemplateAttributeSelectionItem> combo =
                new ComboBox<>(store, new StringLabelProvider<TemplateAttributeSelectionItem>() {
                    @Override
                    public String getLabel(TemplateAttributeSelectionItem item) {
                        return item.getValue();
                    }
                });
        Avu avu = templateTagAvuMap.get(tag);
        if (avu != null) {
            String val = avu.getValue();
            for (TemplateAttributeSelectionItem item : attribute.getValues()) {
                if (item.getValue().equals(val)) {
                    combo.setValue(item);
                    break;
                }
            }

        } else {
            for (TemplateAttributeSelectionItem item : attribute.getValues()) {
                if (item.isDefaultValue()) {
                    combo.setValue(item);
                    break;
                }
            }
        }
        combo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        combo.setAllowBlank(!attribute.isRequired());
        combo.setTypeAhead(true);

        return combo;

    }

    private IPlantAnchor buildHelpLink(final List<MetadataTemplateAttribute> attributes, final String headerText) {
        helpLink = new IPlantAnchor(appearance.metadataTermGuide(), 150, new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                termGuideDialogProvider.get(new AsyncCallback<MetadataTermGuideDialog>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        ErrorHandler.post(throwable);
                    }

                    @Override
                    public void onSuccess(MetadataTermGuideDialog dialog) {
                        dialog.show(attributes,
                                    headerText);
                    }
                });


            }
        });

        return helpLink;
    }


}
