package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.List;

/**
 * Created by sriram on 4/27/17.
 */
public class EditToolViewImpl implements EditToolView {

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

    @Override
    public Widget asWidget() {
        return container;
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

    @UiTemplate("EditToolView.ui.xml")
    interface EditToolViewUiBinder extends UiBinder<Widget, EditToolViewImpl> {
        
    }

    private static  final  EditToolViewUiBinder uiBinder = GWT.create(EditToolViewUiBinder.class);

    @Inject
    public EditToolViewImpl() {
        uiBinder.createAndBindUi(this);
    }

    @Override
    public Tool getTool() {
        Tool tool = factory.getTool().as();
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
    
}
