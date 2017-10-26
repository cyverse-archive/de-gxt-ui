package org.iplantc.de.diskResource.client.views.metadata;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.model.DiskResourceMetadataProperties;
import org.iplantc.de.diskResource.client.presenters.metadata.MetadataUtil;
import org.iplantc.de.diskResource.share.DiskResourceModule.MetadataIds;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * FIXME REFACTOR Segregate programmatic view construction to a different UIBinder, class, etc FIXME
 * REFACTOR Factor out an appearance for this class
 *
 * @author jstroot sriram
 */
public class DiskResourceMetadataViewImpl extends Composite implements MetadataView {

    private DiskResourceMetadataProperties props;

    private final class AttributeValidationHandler implements ValidHandler, InvalidHandler {
        public AttributeValidationHandler() {
        }

        @Override
        public void onInvalid(InvalidEvent event) {
            valid = false;
        }

        @Override
        public void onValid(ValidEvent event) {
            valid = true;
        }
    }

    private final class MetadataCell extends AbstractCell<String> {
        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            appearance.renderMetadataCell(sb, value);
        }
    }

    @UiTemplate("DiskResourceMetadataEditorPanel.ui.xml")
    interface DiskResourceMetadataEditorPanelUiBinder
            extends UiBinder<Widget, DiskResourceMetadataViewImpl> {
    }

    private static final DiskResourceMetadataEditorPanelUiBinder uiBinder =
            GWT.create(DiskResourceMetadataEditorPanelUiBinder.class);

    private final AccordionLayoutAppearance accordionLayoutAppearance =
            GWT.create(AccordionLayoutAppearance.class);

    private boolean dirty;

    @UiField(provided = true) MetadataView.Appearance appearance;
    @UiField TextButton addMetadataButton;
    @UiField TextButton deleteMetadataButton;
    @UiField ToolBar toolbar;
    @UiField TextButton selectButton;
    @UiField TextButton importButton;
    @UiField TextButton editMetadataButton;

    @UiField ListStore<Avu> userMdListStore;
    @UiField ListStore<Avu> additionalMdListStore;
    @UiField Grid<Avu> additionalMdgrid;
    @UiField Grid<Avu> userMdGrid;
    @UiField GridView<Avu> uview;
    @UiField GridView<Avu> aview;
    @UiField(provided = true) ColumnModel<Avu> ucm;
    @UiField(provided = true) ColumnModel<Avu> acm;
    @UiField IPlantAnchor infoLink;
    @UiField TextButton saveToFileButton;
    @UiField TabPanel panel;


    private HashSet<Avu> selectedSet;
    private GridRowEditing<Avu> userGridRowEditing;
    private boolean editable;
    private boolean valid;
    private MetadataView.Presenter presenter;
    private String baseId;
    private CellSelectionModel<Avu> userChxBoxModel;
    private CheckBoxSelectionModel<Avu> addChxBoxModel;
    @Inject MetadataUtil metadataUtil;

    @Inject
    public DiskResourceMetadataViewImpl(DiskResourceMetadataProperties props,
                                        MetadataView.Appearance appearance,
        this.props = props;
        this.appearance = appearance;
        selectedSet = new HashSet<>();
        valid = true;
        userChxBoxModel = new CellSelectionModel<>();
        addChxBoxModel = new CheckBoxSelectionModel<Avu>();
        createColumnModel();
        initWidget(uiBinder.createAndBindUi(this));
        importButton.setToolTip(appearance.importMdTooltip());
        userMdGrid.setSelectionModel(userChxBoxModel);
        additionalMdgrid.setSelectionModel(addChxBoxModel);
        additionalMdgrid.getSelectionModel()
                        .addSelectionChangedHandler(new DiskResourceAdditionalMetadataSelectionChangedHandler());
        infoLink.addClickHandler(event -> Window.open(appearance.metadataLink(), "", "_blank"));
    }

    @Override
    public void mask() {
        userMdGrid.mask(appearance.loadingMask());
    }

    @Override
    public void unmask() {
        userMdGrid.unmask();
    }


    @Override
    public List<Avu> getAvus() {
        return additionalMdListStore.getAll();
    }

    @Override
    public List<Avu> getUserMetadata() {
        return userMdListStore.getAll();
    }


    @Override
    public void loadMetadata(final List<Avu> metadataList) {
        if (metadataList == null || metadataList.size() == 0) {
            panel.remove(1);
            panel.forceLayout();
            return;
        }
        additionalMdListStore.clear();
        additionalMdListStore.commitChanges();
        for (Avu avu : metadataList) {
            additionalMdListStore.add(metadataUtil.setAvuModelKey(avu));
        }

        additionalMdgrid.getStore().setEnableFilters(true);
    }

    @Override
    public void loadUserMetadata(final List<Avu> metadataList) {
        userMdListStore.clear();
        userMdListStore.commitChanges();

        for (Avu avu : metadataList) {
            userMdListStore.add(metadataUtil.setAvuModelKey(avu));
        }

        userMdGrid.getStore().setEnableFilters(true);
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        this.baseId = baseID;
        addMetadataButton.ensureDebugId(baseID + MetadataIds.ADD_METADATA);
        editMetadataButton.ensureDebugId(baseID + MetadataIds.EDIT_METADATA);
        deleteMetadataButton.ensureDebugId(baseID + MetadataIds.DELETE_METADATA);
        selectButton.ensureDebugId(baseID + MetadataIds.TEMPLATES);

        setUserMetadataDebugIds();
    }


    @UiHandler("addMetadataButton")
    void onAddMetadataSelected(SelectEvent event) {
        String attr = getUniqueAttrName(appearance.newAttribute(), 0);
        Avu md = metadataUtil.newMetadata(attr, appearance.newValue(), appearance.newUnit());
        metadataUtil.setAvuModelKey(md);
        userMdListStore.add(0, md);
        userGridRowEditing.startEditing(new GridCell(0, 1));
    }

    @UiHandler("deleteMetadataButton")
    void onDeleteMetadataSelected(SelectEvent event) {
        for (Avu md : selectedSet) {
            userMdListStore.remove(md);
        }

        //remove deleted items
        selectedSet.clear();
        setButtonState();
    }

    @UiHandler("editMetadataButton")
    void onEditMetadataSelected(SelectEvent event) {
        int row = uview.findRowIndex(uview.getRow(selectedSet.iterator().next()));
        userGridRowEditing.startEditing(new GridCell(row, 1));
        userGridRowEditing.getEditor(userMdGrid.getColumnModel().getColumn(1)).validate(false);
    }

    @UiHandler("selectButton")
    void onSelectButtonSelected(SelectEvent event) {
        presenter.onSelectTemplate();

    }

    @UiHandler("importButton")
    void onImportSelected(SelectEvent event) {
        presenter.onImport(additionalMdgrid.getSelectionModel().getSelectedItems());
    }

    @UiHandler("saveToFileButton")
    void onSaveToFileButtonSelected(SelectEvent event) {
        presenter.onSaveToFile();
    }

    private void setUserMetadataDebugIds() {
        panel.getWidget(0).ensureDebugId(baseId + MetadataIds.USER_METADATA);
    }

    @UiFactory
    ColumnModel<Avu> createColumnModel() {
        List<ColumnConfig<Avu, ?>> columns = Lists.newArrayList();


        ColumnConfig<Avu, Boolean> selectColumn = new ColumnConfig<>(new ValueProvider<Avu, Boolean>() {
            @Override
            public Boolean getValue(Avu object) {
                return selectedSet.contains(object);
            }

            @Override
            public void setValue(Avu object, Boolean value) {
                //do nothing
            }

            @Override
            public String getPath() {
                return "SelectCheckBox";
            }
        }, 30, "");
        ColumnConfig<Avu, String> attributeColumn =
                new ColumnConfig<>(props.attribute(), 150, appearance.attribute());
        ColumnConfig<Avu, String> valueColumn =
                new ColumnConfig<>(props.value(), 150, appearance.paramValue());
        ColumnConfig<Avu, String> unitColumn =
                new ColumnConfig<>(props.unit(), 150, appearance.paramUnit());


        CheckBoxCell checkBoxCell = getCheckBoxCell();

        selectColumn.setCell(checkBoxCell);
        selectColumn.setFixed(true);
        selectColumn.setHideable(false);
        selectColumn.setMenuDisabled(true);
        selectColumn.setResizable(false);
        selectColumn.setSortable(false);

        MetadataCell metadataCell = new MetadataCell();
        attributeColumn.setCell(metadataCell);
        valueColumn.setCell(metadataCell);

        columns.add(selectColumn);
        columns.add(attributeColumn);
        columns.add(valueColumn);
        columns.add(unitColumn);

        List<ColumnConfig<Avu, ?>> userMdCols = Lists.newArrayList();
        userMdCols.addAll(columns);
        ucm = new ColumnModel<>(userMdCols);

        List<ColumnConfig<Avu, ?>> addMdCols = Lists.newArrayList();
        addMdCols.add(addChxBoxModel.getColumn());
        addMdCols.addAll(Arrays.asList(attributeColumn, valueColumn, unitColumn));
        return new ColumnModel<>(addMdCols);
    }

    private CheckBoxCell getCheckBoxCell() {
        return new CheckBoxCell() {
            @Override
            protected void onClick(XElement parent, final NativeEvent event) {
                super.onClick(parent, event);
                Avu avu = userChxBoxModel.getSelectedItem();
                if (!selectedSet.remove(avu)) {
                    selectedSet.add(avu);
                }
                setButtonState();
            }
        };
    }

    @UiFactory
    ListStore<Avu> createAdditionalListStore() {
        return new ListStore<>(item -> {
            if (item != null) {
                final AutoBean<Object> metadataBean = AutoBeanUtils.getAutoBean(item);
                return metadataBean.getTag(Avu.AVU_BEAN_TAG_MODEL_KEY);
            } else {
                return ""; //$NON-NLS-1$
            }
        });


    }

    private void setButtonState() {
        addMetadataButton.setEnabled(editable);
        boolean deleteEnabled = (selectedSet.size() > 0) && editable;
        boolean editEnabled = (selectedSet.size() == 1) && editable;
        deleteMetadataButton.setEnabled(deleteEnabled);
        editMetadataButton.setEnabled(editEnabled);
    }

    private String getUniqueAttrName(String attrName, int i) {
        String retName = i > 0 ? attrName + "_(" + i + ")" : attrName;
        for (Avu md : additionalMdListStore.getAll()) {
            if (md.getAttribute().equals(retName)) {
                return getUniqueAttrName(attrName, ++i);
            }
        }
        return retName;
    }

    private void initUserMdGridEditor() {
        userGridRowEditing = new GridRowEditing<>(userMdGrid);
        userGridRowEditing.setClicksToEdit(ClicksToEdit.TWO);
        userGridRowEditing.setErrorSummary(true);

        userMdListStore.setAutoCommit(true);

        ColumnConfig<Avu, Boolean> column0 = userMdGrid.getColumnModel().getColumn(0);
        ColumnConfig<Avu, String> column1 = userMdGrid.getColumnModel().getColumn(1);
        ColumnConfig<Avu, String> column2 = userMdGrid.getColumnModel().getColumn(2);
        ColumnConfig<Avu, String> column3 = userMdGrid.getColumnModel().getColumn(3);

        CheckBox cxb = new CheckBox();
        cxb.setEnabled(false);

        TextField field1 = new TextField();
        field1.setEmptyText(appearance.requiredGhostText());
        TextField field2 = new TextField();
        TextField field3 = new TextField();

        userGridRowEditing.addEditor(column0, cxb);
        userGridRowEditing.addEditor(column1, field1);
        userGridRowEditing.addEditor(column2, field2);
        userGridRowEditing.addEditor(column3, field3);
        userGridRowEditing.addCompleteEditHandler(new CompleteEditHandler<Avu>() {

            @Override
            public void onCompleteEdit(CompleteEditEvent<Avu> event) {
                dirty = true;
                for (Avu avu : userMdListStore.getAll()) {
                    if (Strings.isNullOrEmpty(avu.getAttribute())) {
                        valid = false;
                        return;
                    }
                }
                valid = true;
            }
        });
    }

    @Override
    public void init(boolean editable) {
        this.editable = editable;
        if (editable) {
            initUserMdGridEditor();
            new QuickTip(userMdGrid);
        }
        setButtonState();
    }

    @Override
    public void updateMetadataFromTemplateView(List<Avu> metadataList,
                                               List<MetadataTemplateAttribute> templateAttributes) {
        userMdGrid.mask();
        if (userMdListStore.size() == 0) {
            userMdListStore.addAll(metadataList);
            userMdGrid.unmask();
            return;
        }

        List<Avu> toRemove = new ArrayList<>();
        templateAttributes.forEach(ta -> {
            userMdListStore.getAll().forEach(umd -> {
                if (ta.getName().equals(umd.getAttribute())) {
                    toRemove.add(umd);
                }
            });
        });

        toRemove.forEach(tr -> {
            userMdListStore.remove(tr);
        });

        userMdListStore.addAll(metadataList);
        userMdGrid.unmask();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addToUserMetadata(List<Avu> umd) {
        userMdListStore.addAll(umd);
        userMdGrid.getView().refresh(false);
    }

    @Override
    public void removeImportedMetadataFromStore(List<Avu> umd) {
        for (Avu md : umd) {
            additionalMdListStore.remove(md);
        }
    }


    @Override
    public boolean isDirty() {
        return dirty;
    }

    private class DiskResourceAdditionalMetadataSelectionChangedHandler
            implements SelectionChangedHandler<Avu> {
        @Override
        public void onSelectionChanged(SelectionChangedEvent<Avu> event) {
            if (event.getSelection() != null && event.getSelection().size() > 0) {
                importButton.enable();
            } else {
                importButton.disable();
            }

        }
    }
}
