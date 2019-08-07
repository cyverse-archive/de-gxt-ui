package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.apps.client.models.ToolFilter;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.services.ToolRequestServiceFacade;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.dialogs.IplantInfoBox;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.events.AddNewToolSelected;
import org.iplantc.de.tools.client.events.DeleteToolSelected;
import org.iplantc.de.tools.client.events.EditToolSelected;
import org.iplantc.de.tools.client.events.RefreshToolsSelectedEvent;
import org.iplantc.de.tools.client.events.RequestToolSelected;
import org.iplantc.de.tools.client.events.ShareToolsSelected;
import org.iplantc.de.tools.client.events.ShowToolInfoEvent;
import org.iplantc.de.tools.client.events.ToolFilterChanged;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.views.dialogs.ToolInfoDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolSharingDialog;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;
import java.util.List;


/**
 * Created by sriram on 4/24/17.
 */
public class ManageToolsViewPresenter implements ManageToolsView.Presenter {

    @Inject ManageToolsView toolsView;
    EditToolView editToolView;
    ReactToolViews.EditToolProps editToolProps;
    ManageToolsView.ManageToolsViewAppearance appearance;
    ToolServices toolServices = ServicesInjector.INSTANCE.getDeployedComponentServices();

    @Inject IplantAnnouncer announcer;
    @Inject AsyncProviderWrapper<ToolSharingDialog> shareDialogProvider;
    @Inject AsyncProviderWrapper<ToolInfoDialog> toolInfoDialogProvider;
    @Inject EventBus eventBus;
    @Inject ToolAutoBeanFactory factory;
    @Inject DEProperties deProperties;
    protected List<Tool> currentSelection = Lists.newArrayList();
    private final NewToolRequestFormView requestFormView;
    private final ToolRequestServiceFacade reqServices =
            ServicesInjector.INSTANCE.getToolRequestServiceProvider();

    @Inject
    public ManageToolsViewPresenter(NewToolRequestFormView requestFormView,
                                    ManageToolsView.ManageToolsViewAppearance appearance,
                                    EditToolViewFactory editToolViewFactory) {
        this.requestFormView = requestFormView;
        this.appearance = appearance;
        this.editToolView = editToolViewFactory.create(getBaseProps());
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
        toolsView.getToolbar().addRequestToolSelectedHandler(this);
        toolsView.getToolbar().addEditToolSelectedHandler(this);
        toolsView.addShowToolInfoEventHandlers(this);

        toolsView.mask(appearance.mask());

        toolServices.getToolTypes(new AppsCallback<List<ToolType>>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
                toolsView.unmask();
            }

            @Override
            public void onSuccess(List<ToolType> result) {
                List<String> types = Lists.newArrayList();
                result.forEach(toolType -> {
                    if (!toolType.getName().equals("fAPI") && !toolType.getName().equals("internal")) {
                        types.add(toolType.getName());
                    }
                });
                editToolView.setToolTypes(types.toArray(new String[0]));
                toolsView.unmask();
                loadTools(null);
            }
        });
    }

    @Override
    public void setViewDebugId(String baseId) {
        toolsView.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void onToolSelectionChanged(ToolSelectionChangedEvent event) {
        currentSelection = event.getToolSelection();
        toolsView.getToolbar().setSelection(currentSelection);
    }

    @Override
    public void loadTools(Boolean isPublic) {
        toolsView.mask(appearance.mask());
        toolServices.searchTools(isPublic, null, new AppsCallback<List<Tool>>() {
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
    @SuppressWarnings("unusable-by-js")
    public void addTool(final Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        checkForViceTool(tool);

        editToolView.mask();

        toolServices.addTool(tool, new AppsCallback<Tool>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                editToolView.unmask();
                ErrorHandler.postReact(exception);
            }

            @Override
            public void onSuccess(Tool result) {
                editToolView.close();
                displayInfoMessage(appearance.create(),
                                   appearance.toolAdded(result.getName()));
                tool.setId(result.getId());
                tool.setPermission(result.getPermission());
                tool.setType(result.getType());
                toolsView.addTool(tool);
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void updateTool(final Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        checkForViceTool(tool);

        editToolView.mask();

        toolServices.updateTool(tool, new AppsCallback<Tool>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                editToolView.unmask();
                ErrorHandler.postReact(exception);
            }

            @Override
            public void onSuccess(Tool result) {
                displayInfoMessage(appearance.edit(),
                                   appearance.toolUpdated(result.getName()));
                editToolView.close();
                toolsView.updateTool(result);
            }
        });
    }

    void checkForViceTool(Tool tool) {
        boolean interactive = false;
        boolean skipTmpMount = false;
        String networkMode = tool.getContainer().getNetworkMode();

        if (tool.getType().equals(ToolType.Types.interactive.toString())) {
            factory.appendDefaultInteractiveAppValues(tool, deProperties);
            skipTmpMount = true;
            interactive = true;
            networkMode = ToolContainer.NetworkMode.bridge.toString();
        } else {
            tool.getContainer().setInteractiveApps(null);
            tool.getContainer().setContainerPorts(null);
        }

        tool.setInteractive(interactive);
        tool.getContainer().setSkipTmpMount(skipTmpMount);
        tool.getContainer().setNetworkMode(networkMode);
    }

    Tool getSelectedTool() {
        return currentSelection.get(0);
    }

    @Override
    public void onRefreshToolsSelected(RefreshToolsSelectedEvent event) {
        loadTools(null);
    }

    @Override
    public void onNewToolSelected(AddNewToolSelected event) {
        editToolView.edit(null);
    }

    @Override
    public void closeEditToolDlg() {
        editToolView.close();
    }

    @Override
    public void onDeleteToolsSelected(final DeleteToolSelected event) {
        Tool tool = currentSelection.get(0);
        ConfirmMessageBox cmb =
                new ConfirmMessageBox(appearance.deleteTool(), appearance.confirmDelete());
        cmb.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                switch (event.getHideButton()) {
                    case YES:
                        doDelete(tool);
                        break;
                    case NO:
                        //do nothing
                        break;
                }
            }
        });
        cmb.show();
    }

    void doDelete(final Tool tool) {
        toolServices.deleteTool(tool, new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
            }

            @Override
            public void onSuccess(Void s) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.toolDeleted(tool.getName())));
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
        toolsView.getToolbar().clearSearch();
        switch (tf) {
            case ALL:
                loadTools(null);
                break;
            case MY_TOOLS:
                loadTools(false);
                break;
            case PUBLIC:
                loadTools(true);
                break;
        }
    }

    @Override
    public void onRequestToolSelected(RequestToolSelected event) {
        requestFormView.load(this);
    }

    @Override
    public void onEditToolSelected(EditToolSelected event) {
        toolsView.mask(appearance.mask());

        toolServices.getToolInfo(getSelectedTool().getId(), new AppsCallback<Tool>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                toolsView.unmask();
                announcer.schedule(new ErrorAnnouncementConfig(appearance.toolInfoError()));
            }

            @Override
            public void onSuccess(Tool result) {
                toolsView.unmask();
                editToolView.edit(convertToolToSplittable(result));
            }
        });
    }

    private ReactToolViews.EditToolProps getBaseProps() {
        ReactToolViews.EditToolProps props = new ReactToolViews.EditToolProps();
        props.presenter = this;
        props.parentId = ToolsModule.EditToolIds.EDIT_DIALOG;
        props.isAdmin = false;
        props.isAdminPublishing = false;

        return props;
    }

    @Override
    public void submitRequest(Splittable toolRequest,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback) {

        reqServices.requestInstallation(toolRequest, new AsyncCallback<ToolRequestDetails>() {
            @Override
            public void onFailure(final Throwable caught) {
                if(errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(final ToolRequestDetails response) {
                if(callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
    }

    @Override
    public void onToolRequestDialogClose() {
        requestFormView.onClose();
    }

    @Override
    public HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<Tool> handler) {
        return toolsView.addSelectionChangedHandler(handler);
    }

    @Override
    public void onShowToolInfo(ShowToolInfoEvent event) {
        toolServices.getAppsForTool(event.getTool().getId(), new AppsCallback<List<App>>() {

            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.appsLoadError()));
                getToolInfo(event.getTool().getId(), Arrays.asList());

            }

            @Override
            public void onSuccess(final List<App> apps) {
                getToolInfo(event.getTool().getId(), apps);
            }
        });

    }


    private void getToolInfo(String toolId, List<App> appsUsingTool) {
        toolServices.getToolInfo(toolId, new AppsCallback<Tool>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.toolInfoError()));
            }

            @Override
            public void onSuccess(final Tool tool) {
                showToolInfo(tool, appsUsingTool);
            }
        });
    }

    private void showToolInfo(final Tool tool, final List<App> result) {
        toolInfoDialogProvider.get(new AsyncCallback<ToolInfoDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ToolInfoDialog o) {
                o.show(tool, result);
            }
        });
    }

    void displayInfoMessage(String title, String message) {
        IplantInfoBox iib = new IplantInfoBox(title, message);
        iib.show();
    }

    Tool convertSplittableToTool(Splittable toolSpl) {
        return AutoBeanCodex.decode(factory, Tool.class, toolSpl.getPayload()).as();
    }

    Splittable convertToolToSplittable(Tool tool) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
    }
}
