package org.iplantc.de.apps.client.presenter.tools;

import org.iplantc.de.apps.client.events.tools.AddNewToolSelected;
import org.iplantc.de.apps.client.events.tools.DeleteToolSelected;
import org.iplantc.de.apps.client.events.tools.RefreshToolsSelectedEvent;
import org.iplantc.de.apps.client.events.tools.ShareToolsSelected;
import org.iplantc.de.apps.client.views.tools.EditToolDialog;
import org.iplantc.de.apps.client.views.tools.ManageToolsView;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by sriram on 4/24/17.
 */
public class ManageToolsViewPresenter implements ManageToolsView.Presenter {

    @Inject
    ManageToolsView toolsView;

    ManageToolsView.ManageToolsViewAppearance appearance;

    ToolServices dcService = ServicesInjector.INSTANCE.getDeployedComponentServices();

    @Inject
    AsyncProviderWrapper<EditToolDialog> editDialogProvider;

    @Inject
    IplantAnnouncer announcer;

    @Inject
    public ManageToolsViewPresenter(ManageToolsView.ManageToolsViewAppearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(toolsView.asWidget());
        toolsView.getToolbar().addBeforeToolSearchEventHandler(toolsView);
        toolsView.getToolbar().addToolSearchResultLoadEventHandler(toolsView);
        toolsView.getToolbar().addRefreshToolsSelectedEventHandler(this);
        toolsView.addToolSelectionChangedEventHandler(toolsView.getToolbar());
        toolsView.getToolbar().addNewToolSelectedHandler(this);
        toolsView.getToolbar().addNewToolSelectedHandler(this);
        loadTools();
    }

    @Override
    public void loadTools() {
        toolsView.mask(appearance.mask());
        dcService.getTools(new AsyncCallback<List<Tool>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(List<Tool> tools) {
                toolsView.unmask();
                toolsView.loadTools(tools);
            }
        });
    }

    @Override
    public void addTool(Tool tool) {
        dcService.addTool(tool, new AsyncCallback<Tool>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(Tool s) {
              announcer.schedule(new SuccessAnnouncementConfig("Your tool "+ s.getName() + " is added."));

            }
        });
    }

    @Override
    public void removeTool(Tool tool) {
        
    }

    @Override
    public void onRefreshToolsSelected(RefreshToolsSelectedEvent event) {
        loadTools();
    }

    @Override
    public void onNewToolSelected(AddNewToolSelected event) {
        editDialogProvider.get(new AsyncCallback<EditToolDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(EditToolDialog o) {
                o.setSize("600px", "600px");
                o.show();
            }
        });

    }

    @Override
    public void onDeleteToolsSelected(final DeleteToolSelected event) {
        Tool tool = event.getToolsToBeDeleted().get(0);
        dcService.deleteTool(tool, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(String s) {
               announcer.schedule(new SuccessAnnouncementConfig("Tool deleted successfully!"));
               toolsView.removeTool(tool);
            }
        });
    }

    @Override
    public void onShareToolselected(ShareToolsSelected event) {
        
    }
}
