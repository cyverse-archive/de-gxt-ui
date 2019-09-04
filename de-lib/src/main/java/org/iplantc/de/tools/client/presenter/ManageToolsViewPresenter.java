package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.dialogs.IplantInfoBox;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;
import org.iplantc.de.tools.client.events.UseToolInNewAppEvent;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.gin.factory.ManageToolsViewFactory;
import org.iplantc.de.tools.client.views.dialogs.NewToolRequestDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolInfoDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolSharingDialog;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import java.util.Arrays;
import java.util.List;


/**
 * Created by sriram on 4/24/17.
 */
public class ManageToolsViewPresenter implements ManageToolsView.Presenter {

    ManageToolsView toolsView;
    EditToolView editToolView;
    ReactToolViews.EditToolProps editToolProps;
    ManageToolsView.ManageToolsViewAppearance appearance;
    ToolServices toolServices = ServicesInjector.INSTANCE.getDeployedComponentServices();
    @Inject IplantAnnouncer announcer;
    @Inject AsyncProviderWrapper<ToolSharingDialog> shareDialogProvider;
    @Inject AsyncProviderWrapper<NewToolRequestDialog> newToolRequestDialogProvider;
    @Inject AsyncProviderWrapper<ToolInfoDialog> toolInfoDialogProvider;
    @Inject EventBus eventBus;
    @Inject ToolAutoBeanFactory factory;
    @Inject DEProperties deProperties;
    protected List<Tool> currentSelection = Lists.newArrayList();
    private HandlerManager handlerManager;

    @Inject
    public ManageToolsViewPresenter(ManageToolsView.ManageToolsViewAppearance appearance,
                                    ManageToolsViewFactory manageToolsViewFactory,
                                    EditToolViewFactory editToolViewFactory) {
        this.appearance = appearance;
        this.toolsView = manageToolsViewFactory.create(getBaseManageToolsProps());
        this.editToolView = editToolViewFactory.create(getBaseEditToolProps());
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(toolsView.asWidget());
        toolsView.mask();

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
                loadTools(null, "", "asc", "name", 100, 0);
            }
        });
    }

    @Override
    public void setViewDebugId(String baseId) {
        toolsView.setBaseId(baseId);
    }

    @Override
    public void loadTools(Boolean isPublic,
                          String searchTerm,
                          String order,
                          String orderBy,
                          int rowsPerPage,
                          int page) {
        toolsView.setListingConfig(isPublic, searchTerm, order, orderBy, rowsPerPage, page);
        toolsView.mask();
        toolServices.searchTools(isPublic,
                                 searchTerm,
                                 order,
                                 orderBy,
                                 rowsPerPage,
                                 rowsPerPage * page,
                                 new AppsCallback<Splittable>() {
                                     @Override
                                     public void onFailure(Integer statusCode, Throwable exception) {
                                         toolsView.unmask();
                                         ErrorHandler.postReact(exception);
                                     }

                                     @Override
                                     public void onSuccess(Splittable toolList) {
                                         toolsView.loadTools(toolList);
                                         toolsView.unmask();
                                     }
                                 });
    }

    void refreshListing() {
        loadTools(toolsView.isPublic(),
                  toolsView.getSearchTerm(),
                  toolsView.getOrder(),
                  toolsView.getOrderBy(),
                  toolsView.getRowsPerPage(),
                  toolsView.getPage());
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
                displayInfoMessage(appearance.create(), appearance.toolAdded(result.getName()));
                tool.setId(result.getId());
                tool.setPermission(result.getPermission());
                tool.setType(result.getType());
                Splittable toolSpl = convertToolToSplittable(tool);
                Splittable currentToolList = toolsView.getCurrentToolList();
                Splittable tools = currentToolList.get("tools");
                toolSpl.assign(tools, tools.size());

                toolsView.loadTools(currentToolList);
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
                displayInfoMessage(appearance.edit(), appearance.toolUpdated(result.getName()));
                Splittable currentToolList = toolsView.getCurrentToolList();
                Splittable updatedToolListing = updateToolInToolList(result, currentToolList);
                toolsView.loadTools(updatedToolListing);

                editToolView.close();
            }
        });
    }

    Splittable updateToolInToolList(Tool updatedTool, Splittable currentToolList) {
        ToolList toolList = convertSplittableToToolList(currentToolList);
        List<Tool> tools = toolList.getToolList();
        Tool oldTool = tools.stream()
                            .filter(tool -> tool.getId().equals(updatedTool.getId()))
                            .findFirst()
                            .get();
        tools.remove(oldTool);
        tools.add(updatedTool);

        toolList.setToolList(tools);
        return convertToolListToSplittable(toolList);
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

    @Override
    public void onNewToolSelected() {
        editToolView.edit(null);
    }

    @Override
    public void closeEditToolDlg() {
        editToolView.close();
    }

    @Override
    public void onDeleteToolsSelected(String toolId, String toolName) {
        ConfirmMessageBox cmb =
                new ConfirmMessageBox(appearance.deleteTool(), appearance.confirmDelete());
        cmb.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                switch (event.getHideButton()) {
                    case YES:
                        doDelete(toolId, toolName);
                        break;
                    case NO:
                        //do nothing
                        break;
                }
            }
        });
        cmb.show();
    }

    void doDelete(String toolId, String toolName) {
        toolsView.mask();

        toolServices.deleteTool(toolId, new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.postReact(exception);
                toolsView.unmask();
            }

            @Override
            public void onSuccess(Void s) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.toolDeleted(toolName)));
                Splittable currentToolList = toolsView.getCurrentToolList();
                Splittable updatedToolList = deleteToolInToolList(toolId, currentToolList);
                toolsView.loadTools(updatedToolList);
                toolsView.unmask();
            }
        });
    }

    Splittable deleteToolInToolList(String deletedToolId, Splittable currentToolList) {
        ToolList toolList = convertSplittableToToolList(currentToolList);
        List<Tool> tools = toolList.getToolList();
        Tool oldTool =
                tools.stream().filter(tool -> tool.getId().equals(deletedToolId)).findFirst().get();
        tools.remove(oldTool);

        toolList.setToolList(tools);
        return convertToolListToSplittable(toolList);
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onToolSelectionChanged(Splittable toolSpl) {
        if (toolSpl != null) {
            Tool tool = convertSplittableToTool(toolSpl);
            ensureHandlers().fireEvent(new ToolSelectionChangedEvent(tool));
        }
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void useToolInNewApp(Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        eventBus.fireEvent(new UseToolInNewAppEvent(tool));
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onShareToolsSelected(Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        shareDialogProvider.get(new AsyncCallback<ToolSharingDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ToolSharingDialog tsd) {
                tsd.show(Lists.newArrayList(tool));
            }
        });
    }

    @Override
    public void onRequestToolSelected() {
        newToolRequestDialogProvider.get(new AsyncCallback<NewToolRequestDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewToolRequestDialog o) {
                o.show(NewToolRequestFormView.Mode.NEWTOOL);
            }
        });
    }

    @Override
    public void onEditToolSelected(String toolId) {
        toolsView.mask();

        toolServices.getToolInfo(toolId, new AppsCallback<Tool>() {
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

    private ReactToolViews.EditToolProps getBaseEditToolProps() {
        ReactToolViews.EditToolProps props = new ReactToolViews.EditToolProps();
        props.presenter = this;
        props.parentId = ToolsModule.EditToolIds.EDIT_DIALOG;
        props.isAdmin = false;
        props.isAdminPublishing = false;

        return props;
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onRequestToMakeToolPublicSelected(Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        newToolRequestDialogProvider.get(new AsyncCallback<NewToolRequestDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewToolRequestDialog o) {
                o.setTool(tool);
                o.show(NewToolRequestFormView.Mode.MAKEPUBLIC);
            }
        });
    }

    @Override
    public void onShowToolInfo(String toolId) {
        toolServices.getAppsForTool(toolId, new AppsCallback<List<App>>() {

            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.appsLoadError()));
                getToolInfo(toolId, Arrays.asList());

            }

            @Override
            public void onSuccess(final List<App> apps) {
                getToolInfo(toolId, apps);
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

    @Override
    public HandlerRegistration addToolSelectionChangedEventHandler(ToolSelectionChangedEvent.ToolSelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(ToolSelectionChangedEvent.TYPE, handler);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    Tool convertSplittableToTool(Splittable toolSpl) {
        return toolSpl != null ?
               AutoBeanCodex.decode(factory, Tool.class, toolSpl.getPayload()).as() :
               null;
    }

    Splittable convertToolToSplittable(Tool tool) {
        return tool != null ? AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool)) : null;
    }

    Splittable convertToolListToSplittable(ToolList tools) {
        return tools != null ? AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tools)) : null;
    }

    ToolList convertSplittableToToolList(Splittable toolListSpl) {
        return toolListSpl != null ?
               AutoBeanCodex.decode(factory, ToolList.class, toolListSpl.getPayload()).as() :
               null;
    }

    ReactToolViews.ManageToolsProps getBaseManageToolsProps() {
        ReactToolViews.ManageToolsProps props = new ReactToolViews.ManageToolsProps();
        props.presenter = this;
        props.loading = false;
        props.searchTerm = "";
        props.order = "asc";
        props.orderBy = "name";
        props.rowsPerPage = 100;
        props.page = 0;

        return props;
    }
}

