package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.admin.desktop.client.toolAdmin.view.subviews.ToolContainerPortsListEditor;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.commons.client.validators.ImageNameValidator;
import org.iplantc.de.commons.client.widgets.EmptyStringValueChangeHandler;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.List;

/**
 * Created by sriram on 4/27/17.
 */
public class EditToolViewImpl extends Composite implements EditToolView, Editor<Tool> {

    private static final long ONE_GB = (long)(1024 * 1024 * 1024);

    @UiField
    VBoxLayoutContainer container;

    @UiField
    FramedPanel form;

    @UiField
    TextField name;

    @UiField
    TextArea description;

    @Path("container.image.name")
    @UiField
    TextField  imgName;

    @Path("container.image.tag")
    @UiField
    TextField tag;

    @Path("container.image.url")
    @UiField
    TextField url;

    @UiField
    TextField version;

    @Ignore
    @UiField
    FieldLabel nameLbl;

    @Ignore
    @UiField
    FieldLabel versionLbl;

    @Ignore
    @UiField
    FieldLabel imgLbl;

    @Ignore
    @UiField
    FieldLabel typeLabel;

    @Ignore
    @UiField
    FieldLabel osgImagePathLabel;

    @UiField
    StringComboBox typeEditor;

    @Path("container.image.osgImagePath")
    @UiField
    TextField osgImagePathEditor;

    @Path("container.maxCPUCores")
    @UiField (provided = true)
    SimpleComboBox<Double> maxCPUCoresEditor;

    @Path("container.minMemoryLimit")
    @UiField (provided = true)
    SimpleComboBox<Long> minMemoryLimitEditor;

    @Path("container.minDiskSpace")
    @UiField (provided = true)
    SimpleComboBox<Long> minDiskSpaceEditor;

    @Path("container.pidsLimit")
    @UiField
    IntegerField pidsLimit;

    @Path("container.memoryLimit")
    @UiField (provided = true)
    SimpleComboBox<Long> memory;

    @Path("container.networkMode")
    @UiField
    TextField network;

    @Path("timeLimit")
    @UiField
    IntegerField time;

    @UiField
    FramedPanel restrictions;

    /**
     * Entrypoint for a tool container
     */
    @Path("container.entryPoint")
    @UiField
    TextField entryPoint;

    /**
     * Working Directory inside the tool's container
     */
    @Path("container.workingDirectory")
    @UiField
    TextField workingDir;

    @Path("container.UID")
    @UiField
    IntegerField uidEditor;

    @Ignore
    @UiField
    FieldSet containerPortsFieldSet;

    @Path("container.containerPorts")
    @UiField (provided = true)
    ToolContainerPortsListEditor containerPortsEditor;

    @Ignore
    @UiField
    TextButton addPortsButton;

    @Ignore
    @UiField
    TextButton deletePortsButton;

    @UiField
    EditToolView.EditToolViewAppearance appearance;

    interface EditorDriver extends SimpleBeanEditorDriver<Tool, EditToolViewImpl> {
    }

    private final EditToolViewImpl.EditorDriver editorDriver =
            GWT.create(EditToolViewImpl.EditorDriver.class);


    @UiTemplate("EditToolView.ui.xml")
    interface EditToolViewUiBinder extends UiBinder<Widget, EditToolViewImpl> {

    }

    private static  final  EditToolViewUiBinder uiBinder = GWT.create(EditToolViewUiBinder.class);

    @Inject
    public EditToolViewImpl(DEProperties deProperties, ToolContainerPortsListEditor containerPortsEditor) {
        this.containerPortsEditor = containerPortsEditor;

        minMemoryLimitEditor = createDataSizeComboBox();
        minDiskSpaceEditor = createDataSizeComboBox();
        memory = createDataSizeComboBox();
        maxCPUCoresEditor = createDoubleComboBox();

        buildResourceSizeLimitList(minMemoryLimitEditor, 2 * ONE_GB, deProperties.getToolsMaxMemLimit());
        buildResourceSizeLimitList(memory, 2 * ONE_GB, deProperties.getToolsMaxMemLimit());
        buildResourceSizeLimitList(minDiskSpaceEditor, ONE_GB, deProperties.getToolsMaxDiskLimit());
        buildResourceDoubleLimitList(maxCPUCoresEditor, 1, deProperties.getToolsMaxCPULimit());

        initWidget(uiBinder.createAndBindUi(this));

        nameLbl.setHTML(buildRequiredFieldLabel(nameLbl.getText()));
        versionLbl.setHTML(buildRequiredFieldLabel(versionLbl.getText()));

        imgLbl.setHTML(buildRequiredFieldLabel(imgLbl.getText()));
        imgName.addValidator(new ImageNameValidator());

        typeLabel.setHTML(buildRequiredFieldLabel(typeLabel.getText()));
        typeEditor.addSelectionHandler(event -> onTypeChanged());

        osgImagePathLabel.setHTML(buildRequiredFieldLabel(osgImagePathLabel.getText()));

        tag.addValueChangeHandler(new EmptyStringValueChangeHandler(tag));
        url.addValueChangeHandler(new EmptyStringValueChangeHandler(url));
        osgImagePathEditor.addValueChangeHandler(new EmptyStringValueChangeHandler(osgImagePathEditor));
        entryPoint.addValueChangeHandler(new EmptyStringValueChangeHandler(entryPoint));
        workingDir.addValueChangeHandler(new EmptyStringValueChangeHandler(workingDir));

        editorDriver.initialize(this);
    }

    private void buildResourceSizeLimitList(SimpleComboBox<Long> limitSelectionList, long resourceLimit, long maxLimit) {
        limitSelectionList.add((long)0);
        limitSelectionList.add(resourceLimit);

        while (resourceLimit < maxLimit) {
            resourceLimit *= 2;
            limitSelectionList.add(resourceLimit);
        }
    }

    private void buildResourceDoubleLimitList(SimpleComboBox<Double> limitSelectionList, double resourceLimit, double maxLimit) {
        limitSelectionList.add((double)0);
        limitSelectionList.add(resourceLimit);

        while (resourceLimit < maxLimit) {
            resourceLimit *= 2;
            limitSelectionList.add(resourceLimit);
        }
    }

    private void onTypeChanged() {
        final String typeValue = typeEditor.getCurrentValue();
        final boolean osgType = ToolType.Types.osg.toString().equals(typeValue);
        final boolean interactiveType = ToolType.Types.interactive.toString().equals(typeValue);

        osgImagePathEditor.setEnabled(osgType);
        containerPortsFieldSet.setEnabled(interactiveType);
    }

    @Override
    public void setToolTypes(List<String> types) {
        typeEditor.add(types);
        typeEditor.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
    }

    private SafeHtml buildRequiredFieldLabel(final String label) {
        if (label == null) {
            return null;
        }

        return appearance.buildRequiredFieldLabel(label);
    }

    private SimpleComboBox<Long> createDataSizeComboBox() {
        final SimpleComboBox<Long> resourceSizeSimpleComboBox =
                new SimpleComboBox<>(size -> Math.round((float)(size * 10 / ONE_GB)) / 10 + " GB");

        resourceSizeSimpleComboBox.setEditable(false);
        resourceSizeSimpleComboBox.setAllowBlank(true);
        resourceSizeSimpleComboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        return resourceSizeSimpleComboBox;
    }

    private SimpleComboBox<Double> createDoubleComboBox() {
        final SimpleComboBox<Double> resourceSizeSimpleComboBox = new SimpleComboBox<>(Object::toString);

        resourceSizeSimpleComboBox.setEditable(false);
        resourceSizeSimpleComboBox.setAllowBlank(true);
        resourceSizeSimpleComboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        return resourceSizeSimpleComboBox;
    }

    @Override
    public Tool getTool() {
        return editorDriver.flush();
    }

    @Override
    public void editTool(Tool t) {
        if (Strings.isNullOrEmpty(t.getType())) {
            t.setType(ToolType.Types.executable.toString());
        }

         // If you try to edit a Tool that was created without a ContainerPorts list,
         // then this field will be omitted in the encoded JSON on save,
         // even when items are added in the UI by the Editor.
         // Add an empty list here as a workaround.
        if (t.getContainer().getContainerPorts() == null) {
            t.getContainer().setContainerPorts(Lists.newArrayList());
        }

        final boolean interactiveType = ToolType.Types.interactive.toString().equals(t.getType());
        containerPortsFieldSet.setEnabled(interactiveType);

        final boolean osgType = ToolType.Types.osg.toString().equals(t.getType());
        osgImagePathEditor.setEnabled(osgType);

        editorDriver.edit(t);
    }

    @Override
    public boolean validate() {
        boolean valid = true;
        List<IsField<?>> fields = FormPanelHelper.getFields(form);
        for (IsField<?> f : fields) {
            if (!f.isValid(false)) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        name.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_NAME);
        description.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_DESC);
        version.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_VER);
        imgName.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_IMG);
        tag.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_TAG);
        url.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_URL);
        pidsLimit.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_CPU);
        memory.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_MEM);
        network.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_NW);
        time.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_TIME);
        osgImagePathEditor.setId(baseID + ToolsModule.EditToolIds.OSG_IMAGE_PATH);
        minMemoryLimitEditor.setId(baseID + ToolsModule.EditToolIds.MIN_MEM_LIMIT);
        maxCPUCoresEditor.setId(baseID + ToolsModule.EditToolIds.MAX_CPU_CORES);
        minDiskSpaceEditor.setId(baseID + ToolsModule.EditToolIds.MIN_DISK_SPACE);
        typeEditor.setId(baseID + ToolsModule.EditToolIds.TOOL_TYPE);
        workingDir.setId(baseID + ToolsModule.EditToolIds.CONTAINER_WORKING_DIR);
        uidEditor.setId(baseID + ToolsModule.EditToolIds.CONTAINER_UID);
        addPortsButton.ensureDebugId(baseID + ToolsModule.EditToolIds.CONTAINER_PORTS_ADD);
        deletePortsButton.ensureDebugId(baseID + ToolsModule.EditToolIds.CONTAINER_PORTS_DELETE);
    }

    @UiHandler("addPortsButton")
    void onAddPortsButtonClicked(SelectEvent event) {
        containerPortsEditor.addContainerPorts();
    }

    @UiHandler("deletePortsButton")
    void onDeletePortsButtonClicked(SelectEvent event) {
        containerPortsEditor.deleteContainerPorts();
    }

}
