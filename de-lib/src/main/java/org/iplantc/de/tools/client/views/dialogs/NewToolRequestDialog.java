/**
 *
 */
package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.tools.client.gin.factory.NewToolRequestFormPresenterFactory;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import javax.ws.rs.HEAD;

/**
 * @author sriram
 *
 */
public class NewToolRequestDialog extends IPlantDialog {


    private Tool tool;
    private final NewToolRequestFormView.Presenter presenter;

    @Inject
    NewToolRequestDialog(final NewToolRequestFormPresenterFactory presenterFactory) {
        setHeading(I18N.DISPLAY.requestNewTool());
        setPixelSize(480, 400);
        this.setResizable(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        setHideOnButtonClick(false);
        setOkButtonText(I18N.DISPLAY.submit());
        presenter = presenterFactory.createPresenter(new Command() {
            @Override
            public void execute() {
                hide();

            }
        });
        presenter.go(this);
        addOkButtonSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                presenter.onSubmitBtnClick();
            }
        });
        
        addCancelButtonSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                presenter.onCancelBtnClick();
            }
        });

    }

    @Override
    public void show() {
        super.show();
        ensureDebugId(ToolsModule.RequestToolIds.TOOL_REQUEST);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + ToolsModule.RequestToolIds.TOOL_REQUEST_VIEW);
    }

    public void setTool(Tool tool) {
        this.tool = tool;
        presenter.setTool(tool);
   }

}

