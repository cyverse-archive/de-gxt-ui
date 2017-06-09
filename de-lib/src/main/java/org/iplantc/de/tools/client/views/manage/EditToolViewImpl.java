package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolImage;
import org.iplantc.de.tools.shared.ToolsModule;
import org.iplantc.de.commons.client.validators.ImageNameValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

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
public class EditToolViewImpl extends Composite implements EditToolView {

    @Inject
    ToolAutoBeanFactory factory;

    @UiField
    VBoxLayoutContainer container;

    @UiField
    FramedPanel form;

    @UiField
    TextField name;

    @UiField
    TextArea desc;

    @UiField
    TextField  imgName;

    @UiField
    TextField tag;

    @UiField
    TextField url;

    @UiField
    TextField version;

    @UiField
    FieldLabel nameLbl;

    @UiField
    FieldLabel versionLbl;

    @UiField
    FieldLabel imgLbl;

    @UiField
    TextField cpu;

    @UiField
    TextField memory;

    @UiField
    TextField network;

    @UiField
    TextField time;

    Hidden toolId;

    @UiField
    EditToolView.EditToolViewAppearance appearance;


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
        toolId = new Hidden();
        imgName.addValidator(new ImageNameValidator());
    }

    private SafeHtml buildRequiredFieldLabel(final String label) {
        if (label == null) {
            return null;
        }

        return appearance.buildRequiredFieldLabel(label); //$NON-NLS-1$
    }

    @Override
    public boolean validate() {
        boolean valid = true;
        List<IsField<?>> fields = FormPanelHelper.getFields(form);
        for (IsField<?> f : fields) {
            if (!f.isValid(false)) {
                GWT.log(f.toString() + "-> " + f.getValue());
                valid = false;
                break;
            }
        }

        return valid;
    }

    @Override
    public Tool getTool() {
        Tool tool = factory.getTool().as();
        tool.setId(toolId.getValue());
        tool.setName(name.getValue());
        tool.setDescription(desc.getValue());
        tool.setVersion(version.getValue());

        ToolImage image = factory.getImage().as();
        image.setName(imgName.getValue());
        image.setTag(tag.getValue());
        image.setUrl(url.getValue());

        ToolContainer container = factory.getContainer().as();
        container.setImage(image);
        tool.setContainer(container);

        GWT.log("json ->" + AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool)).getPayload());
        return tool;
    }

    @Override
    public void editTool(Tool t) {
        toolId.setValue(t.getId());
        name.setValue(t.getName());
        desc.setValue(t.getDescription());
        version.setValue(t.getVersion());
        imgName.setValue(t.getContainer().getImage().getName());
        tag.setValue(t.getContainer().getImage().getTag());
        url.setValue(t.getLocation());
        url.setEnabled(false);
        imgName.setEnabled(false);
        tag.setEnabled(false);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        name.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_NAME);
        desc.ensureDebugId(baseID + ToolsModule.EditToolIds.TOOL_DESC);
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
