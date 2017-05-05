package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.tools.client.events.AddNewToolSelected;
import org.iplantc.de.tools.client.events.DeleteToolSelected;
import org.iplantc.de.tools.client.events.RefreshToolsSelectedEvent;
import org.iplantc.de.tools.client.events.ShareToolsSelected;
import org.iplantc.de.tools.client.events.ToolFilterChanged;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;
import org.iplantc.de.apps.client.models.ToolFilter;
import org.iplantc.de.tools.client.views.manage.EditToolDialog;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.manage.ToolSharingDialog;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
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
    AsyncProviderWrapper<ToolSharingDialog> shareDialogProvider;

    protected List<Tool> currentSelection = Lists.newArrayList();

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
        toolsView.addToolSelectionChangedEventHandler(this);
        toolsView.getToolbar().addNewToolSelectedHandler(this);
        toolsView.getToolbar().addShareToolselectedHandler(this);
        toolsView.getToolbar().addDeleteToolsSelectedHandler(this);
        toolsView.getToolbar().addToolFilterChangedHandler(this);
        loadTools(false);
    }

    @Override
    public void onToolSelectionChanged(ToolSelectionChangedEvent event) {
        currentSelection = event.getToolSelection();
        toolsView.getToolbar().setSelection(currentSelection);
    }

    @Override
    public void loadTools(boolean isPublic) {
        toolsView.mask(appearance.mask());
        dcService.getTools(new AppsCallback<List<Tool>>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
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
        dcService.addTool(tool, new AppsCallback<Tool>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
            }

            @Override
            public void onSuccess(Tool s) {
                announcer.schedule(new SuccessAnnouncementConfig(
                        "Your tool " + s.getName() + " is added."));
                toolsView.addTool(s);
            }
        });
    }

    @Override
    public void onRefreshToolsSelected(RefreshToolsSelectedEvent event) {
        loadTools(false);
    }

    @Override
    public void onNewToolSelected(AddNewToolSelected event) {
        editDialogProvider.get(new AsyncCallback<EditToolDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(EditToolDialog etd) {
                etd.setSize("600px", "600px");
                etd.show();
            }
        });

    }

    @Override
    public void onDeleteToolsSelected(final DeleteToolSelected event) {
        Tool tool = currentSelection.get(0);
        dcService.deleteTool(tool, new AppsCallback<String>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
            }

            @Override
            public void onSuccess(String s) {
                announcer.schedule(new SuccessAnnouncementConfig("Tool deleted successfully!"));
                toolsView.removeTool(tool);
            }
        });
    }

    @Override
    public void onShareToolsSelected(final ShareToolsSelected event) {
        shareDialogProvider.get(new AsyncCallback<ToolSharingDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ToolSharingDialog tsd) {
                tsd.show(currentSelection);
            }
        });
    }

    @Override
    public void onToolFilterChanged(ToolFilterChanged event) {
        ToolFilter tf = event.getFilter();
    }
}
