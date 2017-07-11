package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.validators.ImageNameValidator;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
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
    TextField cpu;

    @Ignore
    @UiField
    TextField memory;

    @Ignore
    @UiField
    TextField network;

    @Ignore
    @UiField
    TextField time;

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
    public EditToolViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        nameLbl.setHTML(buildRequiredFieldLabel(nameLbl.getText()));
        versionLbl.setHTML(buildRequiredFieldLabel(versionLbl.getText()));
        imgLbl.setHTML(buildRequiredFieldLabel(imgLbl.getText()));
        imgName.addValidator(new ImageNameValidator());
        editorDriver.initialize(this);
    }

    private SafeHtml buildRequiredFieldLabel(final String label) {
        if (label == null) {
            return null;
        }

        return appearance.buildRequiredFieldLabel(label); //$NON-NLS-1$
    }

    @Override
    public Tool getTool() {
        return editorDriver.flush();
    }

    @Override
    public void editTool(Tool t) {
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
        cpu.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_CPU);
        memory.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_MEM);
        network.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_NW);
        time.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_TIME);
    }

}
