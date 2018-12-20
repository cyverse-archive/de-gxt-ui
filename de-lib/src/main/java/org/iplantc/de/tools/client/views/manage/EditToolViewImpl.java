package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.validators.ImageNameValidator;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
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
    @UiField
    DoubleField maxCPUCoresEditor;

    @Path("container.minMemoryLimit")
    @UiField
    SimpleComboBox<Long> minMemoryLimitEditor;

    @Path("container.minDiskSpace")
    @UiField
    SimpleComboBox<Long> minDiskSpaceEditor;

    @Path("container.pidsLimit")
    @UiField
    IntegerField pidsLimit;

    @Path("container.memoryLimit")
    @UiField
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
    public EditToolViewImpl(DEProperties deProperties) {
        initWidget(uiBinder.createAndBindUi(this));

        nameLbl.setHTML(buildRequiredFieldLabel(nameLbl.getText()));
        versionLbl.setHTML(buildRequiredFieldLabel(versionLbl.getText()));

        imgLbl.setHTML(buildRequiredFieldLabel(imgLbl.getText()));
        imgName.addValidator(new ImageNameValidator());

        typeLabel.setHTML(buildRequiredFieldLabel(typeLabel.getText()));
        typeEditor.addSelectionHandler(event -> onTypeChanged());

        osgImagePathLabel.setHTML(buildRequiredFieldLabel(osgImagePathLabel.getText()));

        editorDriver.initialize(this);

        long oneGB = (long)(1024 * 1024 * 1024);
        long resourceLimit = oneGB * 2;
        minMemoryLimitEditor.add((long)0);
        minMemoryLimitEditor.add(resourceLimit);
        while (resourceLimit < deProperties.getToolsMaxMemLimit()) {
            resourceLimit *= 2;
            minMemoryLimitEditor.add(resourceLimit);
        }

        resourceLimit = oneGB * 2;
        memory.add((long)0);
        memory.add(resourceLimit);
        while (resourceLimit < deProperties.getToolsMaxMemLimit()) {
            resourceLimit *= 2;
            memory.add(resourceLimit);
        }

        resourceLimit = oneGB;
        minDiskSpaceEditor.add((long)0);
        minDiskSpaceEditor.add(resourceLimit);
        while (resourceLimit < deProperties.getToolsMaxDiskLimit()) {
            resourceLimit *= 2;
            minDiskSpaceEditor.add(resourceLimit);
        }
    }

    private void onTypeChanged() {
        final boolean osgType = "osg".equals(typeEditor.getCurrentValue());
        osgImagePathEditor.setEnabled(osgType);
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

    @Ignore
    @UiFactory
    SimpleComboBox<Long> createComboBox() {
        final SimpleComboBox<Long> resourceSizeSimpleComboBox =
                new SimpleComboBox<>(size -> Math.round((float)(size * 10 / (1024 * 1024 * 1024))) / 10 + " GB");

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
        final boolean osgType = "osg".equals(t.getType());
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
    }

}
