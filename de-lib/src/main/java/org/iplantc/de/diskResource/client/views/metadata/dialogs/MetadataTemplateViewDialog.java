package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.models.diskResources.TemplateAttributeSelectionItem;
import org.iplantc.de.commons.client.validators.UrlValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.presenters.metadata.MetadataPresenterImpl;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Dialog;
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
 * Created by sriram on 5/9/16.
 */
public class MetadataTemplateViewDialog extends IPlantDialog {
    final MetadataView.Appearance appearance = GWT.create(MetadataView.Appearance.class);
    private final MetadataView.Presenter presenter;
    private VerticalLayoutContainer widget;
    private final DateTimeFormat timestampFormat;
    private boolean writable;
    private final FastMap<Avu> templateTagAvuMap;
    private final FastMap<MetadataTemplateAttribute> templateTagAtrrMap;
    private final FastMap<Field<?>> templateTagFieldMap;
    private List<MetadataTemplateAttribute> attributes;
    private List<Avu> templateMd;
    private boolean valid;
    private int dupeCounter;

    public MetadataTemplateViewDialog(MetadataView.Presenter presenter,
                                      List<Avu> templateMd,
                                      boolean writable,
                                      List<MetadataTemplateAttribute> attributes) {
        templateTagAvuMap = new FastMap<>();
        templateTagFieldMap = new FastMap<>();
        templateTagAtrrMap = new FastMap<>();
        timestampFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
        this.presenter = presenter;
        this.writable = writable;
        this.attributes = attributes;
        this.templateMd = templateMd;
        valid = true;

        widget = new VerticalLayoutContainer();
        widget.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        buildAvuMap();
        loadTemplateAttributes();
        add(widget);
    }

    public ArrayList<Avu> getMetadataFromTemplate() {
        ArrayList<Avu> avus = new ArrayList<>();
        templateTagAtrrMap.keySet().forEach(tag -> {
            Avu avu = templateTagAvuMap.get(tag);
            if (avu == null) {
                MetadataTemplateAttribute metadataTemplateAttribute = templateTagAtrrMap.get(tag);
                avu = MetadataPresenterImpl.newMetadata(metadataTemplateAttribute.getName(),
                                                        "",
                                                        ""); //$NON-NLS-1$ //$NON-NLS-2$
                templateTagAvuMap.put(tag, presenter.setAvuModelKey(avu));
            }

            Field<?> field = templateTagFieldMap.get(tag);
            if (field.getValue() != null) {
                String value = field.getValue().toString();
                if ((field instanceof DateField) && !Strings.isNullOrEmpty(value)) {
                    value = timestampFormat.format(((DateField)field).getValue());
                } else if (field instanceof ComboBox<?>) {
                    @SuppressWarnings("unchecked") ComboBox<TemplateAttributeSelectionItem> temp =
                            (ComboBox<TemplateAttributeSelectionItem>)field;
                    value = temp.getValue().getValue();
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

    private void buildAvuMap() {
        for (MetadataTemplateAttribute attribute : attributes) {
            List<Avu> mds = findMetadataForAttribute(attribute.getName());
            mds.forEach(md -> {
                final AutoBean<Object> metadataBean = AutoBeanUtils.getAutoBean(md);
                String tag = metadataBean.getTag(presenter.AVU_BEAN_TAG_MODEL_KEY);
                templateTagAvuMap.put(tag, md);
                templateTagAtrrMap.put(tag, attribute);
                GWT.log(tag + "-->" + attribute.getName());
            });

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
            tf.setEmptyText("Valid URL");
        }
        return tf;
    }


    private void loadTemplateAttributes() {
        templateTagFieldMap.clear();
        IPlantAnchor helpLink = buildHelpLink(attributes);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        hp.add(helpLink);
        widget.add(hp, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        templateTagAtrrMap.keySet().forEach(tag -> {
            Field<?> field = getAttributeValueWidget(tag);
            addFieldToTemplate(tag, templateTagAtrrMap.get(tag), field);
        });

    }

    private void addFieldToTemplate(String tag, MetadataTemplateAttribute attribute, Field<?> field) {
        if (field != null) {
            field.setReadOnly(!writable);
            templateTagFieldMap.put(tag, field);
            FieldLabel lbl = buildFieldLabel(field,
                                             attribute.getName(),
                                             attribute.getDescription(),
                                             !attribute.isRequired());
            HorizontalPanel panel = buildMultiField(tag, attribute, lbl);
            widget.add(panel, new VerticalLayoutContainer.VerticalLayoutData(.90, -1));
            widget.forceLayout();
        }
    }

    private HorizontalPanel buildMultiField(String tag,
                                            MetadataTemplateAttribute attribute,
                                            FieldLabel field) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(field);
        field.setWidth("300px");
        TextButton addBtn = new TextButton("+");
        addBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Field<?> field = getAttributeValueWidget(tag);
                addFieldToTemplate(tag, attribute, field);
            }
        });

        TextButton remBtn = new TextButton("-");
        remBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                panel.remove(field);
                panel.removeFromParent();
                widget.remove(panel);
                widget.forceLayout();
            }
        });

        panel.add(addBtn);
        panel.add(remBtn);

        return panel;
    }

    /**
     * @param tag the template attribute
     * @return Field based on MetadataTemplateAttribute type.
     */
/*    private Field<?> getAttributeValueWidget(MetadataTemplateAttribute attribute) {
        String type = attribute.getType();
        if (type.equalsIgnoreCase("timestamp")) { //$NON-NLS-1$
            return buildDateField(attribute);
        } else if (type.equalsIgnoreCase("boolean")) { //$NON-NLS-1$
            return buildBooleanField(attribute);
        } else if (type.equalsIgnoreCase("number")) { //$NON-NLS-1$
            return buildNumberField(attribute);
        } else if (type.equalsIgnoreCase("integer")) { //$NON-NLS-1$
            return buildIntegerField(attribute);
        } else if (type.equalsIgnoreCase("string")) { //$NON-NLS-1$
            return buildTextField(attribute);
        } else if (type.equalsIgnoreCase("multiline text")) { //$NON-NLS-1$
            return buildTextArea(attribute);
        } else if (type.equalsIgnoreCase("URL/URI")) { //$NON-NLS-1$
            return buildURLField(attribute);
        } else if (type.equalsIgnoreCase("Enum")) {
            return buildListField(attribute);
        } else {
            return null;
        }
    }*/
    private Field<?> getAttributeValueWidget(String tag) {
        MetadataTemplateAttribute attribute = templateTagAtrrMap.get(tag);
        String type = attribute.getType();
        if (type.equalsIgnoreCase("timestamp")) { //$NON-NLS-1$
            return buildDateField(tag, attribute);
        } else if (type.equalsIgnoreCase("boolean")) { //$NON-NLS-1$
            return buildBooleanField(tag, attribute);
        } else if (type.equalsIgnoreCase("number")) { //$NON-NLS-1$
            return buildNumberField(tag, attribute);
        } else if (type.equalsIgnoreCase("integer")) { //$NON-NLS-1$
            return buildIntegerField(tag, attribute);
        } else if (type.equalsIgnoreCase("string")) { //$NON-NLS-1$
            return buildTextField(tag, attribute);
        } else if (type.equalsIgnoreCase("multiline text")) { //$NON-NLS-1$
            return buildTextArea(tag, attribute);
        } else if (type.equalsIgnoreCase("URL/URI")) { //$NON-NLS-1$
            return buildURLField(tag, attribute);
        } else if (type.equalsIgnoreCase("Enum")) {
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
        return combo;

    }

    public IPlantAnchor buildHelpLink(final List<MetadataTemplateAttribute> attributes) {
        IPlantAnchor helpLink =
                new IPlantAnchor(appearance.metadataTermGuide(), 150, new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        VerticalLayoutContainer helpVlc = new VerticalLayoutContainer();
                        helpVlc.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
                        for (MetadataTemplateAttribute mta : attributes) {
                            HTML l = new HTML("<b>" + mta.getName() + ":</b> <br/>");
                            HTML helpText = new HTML("<p>" + mta.getDescription() + "</p><br/>");
                            helpVlc.add(l, new VerticalLayoutContainer.VerticalLayoutData(.25, -1));
                            helpVlc.add(helpText,
                                        new VerticalLayoutContainer.VerticalLayoutData(.90, -1));
                        }
                        Dialog w = new Dialog();
                        w.setHideOnButtonClick(true);
                        w.setSize("350", "400");
                        w.setPredefinedButtons(PredefinedButton.OK);
                        w.setHeading(MetadataTemplateViewDialog.this.getHeader().getText());
                        w.setBodyStyle("background: #fff;");
                        w.setWidget(helpVlc);
                        w.show();

                    }
                });

        return helpLink;
    }


}
