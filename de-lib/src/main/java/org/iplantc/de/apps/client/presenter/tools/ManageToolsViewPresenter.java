package org.iplantc.de.apps.client.presenter.tools;

import org.iplantc.de.apps.client.events.tools.AddNewToolSelected;
import org.iplantc.de.apps.client.events.tools.DeleteToolSelected;
import org.iplantc.de.apps.client.events.tools.RefreshToolsSelectedEvent;
import org.iplantc.de.apps.client.events.tools.ShareToolsSelected;
import org.iplantc.de.apps.client.events.tools.ToolSelectionChangedEvent;
import org.iplantc.de.apps.client.views.tools.EditToolDialog;
import org.iplantc.de.apps.client.views.tools.ManageToolsView;
import org.iplantc.de.apps.client.views.tools.ToolSharingDialog;
import org.iplantc.de.apps.client.views.tools.ToolSharingPresenter;
import org.iplantc.de.apps.client.views.tools.ToolSharingView;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.models.sharing.UserPermission;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.sharing.ToolPermission;
import org.iplantc.de.client.models.tool.sharing.ToolSharingAutoBeanFactory;
import org.iplantc.de.client.models.tool.sharing.ToolSharingRequest;
import org.iplantc.de.client.models.tool.sharing.ToolSharingRequestList;
import org.iplantc.de.client.models.tool.sharing.ToolUnSharingRequestList;
import org.iplantc.de.client.models.tool.sharing.ToolUnsharingRequest;
import org.iplantc.de.client.models.tool.sharing.ToolUserPermissions;
import org.iplantc.de.client.models.tool.sharing.ToolUserPermissionsList;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.client.sharing.SharingPermissionView;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sriram on 4/24/17.
 */
public class ManageToolsViewPresenter implements ManageToolsView.Presenter, ToolSharingPresenter {


    private EditToolDialog editToolDialog;

    private final class LoadPermissionsCallback implements AsyncCallback<String> {
        private final class GetUserInfoCallback implements AsyncCallback<FastMap<Collaborator>> {
            private final ToolUserPermissionsList toolPermsList;

            private GetUserInfoCallback(ToolUserPermissionsList toolPermsList) {
                this.toolPermsList = toolPermsList;
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(FastMap<Collaborator> results) {
                FastMap<List<Sharing>> sharingMap = new FastMap<>();
                for (ToolUserPermissions toolUserPerms : toolPermsList.getResourceUserPermissionsList()) {
                    for (UserPermission userPerms : toolUserPerms.getPermissions()) {
                        String userName = userPerms.getUser();

                        Collaborator user = results.get(userName);
                        if (user == null) {
                            user = collaboratorsUtil.getDummyCollaborator(userName);
                        }

                        List<Sharing> shares = sharingMap.get(userName);

                        if (shares == null) {
                            shares = new ArrayList<>();
                            sharingMap.put(userName, shares);
                        }

                        Sharing sharing = new Sharing(user,
                                                      PermissionValue.valueOf(userPerms.getPermission()),
                                                      null,
                                                      toolUserPerms.getId(),
                                                      toolUserPerms.getName());
                        shares.add(sharing);
                    }
                }

                permissionsPanel.loadSharingData(sharingMap);
                permissionsPanel.unmask();
            }
        }

        @Override
        public void onFailure(Throwable caught) {
            permissionsPanel.unmask();
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            ToolUserPermissionsList toolPermsList =
                    AutoBeanCodex.decode(sharingAutoBeanFactory, ToolUserPermissionsList.class, result).as();
            final List<String> usernames = new ArrayList<>();
            for (ToolUserPermissions toolUserPerms : toolPermsList.getResourceUserPermissionsList()) {
                for (UserPermission up : toolUserPerms.getPermissions()) {
                    usernames.add(up.getUser());
                }
            }

            collaboratorsServiceFacade.getUserInfo(usernames,
                                                   new ManageToolsViewPresenter.LoadPermissionsCallback.GetUserInfoCallback(
                                                           toolPermsList));
        }

    }

    @Inject
    ManageToolsView toolsView;

    ManageToolsView.ManageToolsViewAppearance appearance;

    ToolServices dcService = ServicesInjector.INSTANCE.getDeployedComponentServices();

    @Inject
    AsyncProviderWrapper<EditToolDialog> editDialogProvider;

    @Inject
    AsyncProviderWrapper<ToolSharingDialog> shareDialogProvider;

    @Inject
    IplantAnnouncer announcer;

    @Inject
    ToolSharingView sharingView;

    @Inject
    SharingPermissionViewFactory permFactory;

    @Inject
    ToolAutoBeanFactory toolAutoBeanFactory;

    @Inject
    ToolSharingAutoBeanFactory sharingAutoBeanFactory;
    
    private SharingPermissionView permissionsPanel;

    private final CollaboratorsUtil collaboratorsUtil;

    private CollaboratorsServiceFacade collaboratorsServiceFacade;

    protected List<Tool> currentSelection = Lists.newArrayList();

    @Inject
    public ManageToolsViewPresenter(ManageToolsView.ManageToolsViewAppearance appearance,
                                    final CollaboratorsUtil collaboratorsUtil,
                                    CollaboratorsServiceFacade collaboratorsServiceFacade) {
        this.appearance = appearance;
        this.collaboratorsUtil = collaboratorsUtil;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
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
        loadTools();
    }

    @Override
    public void onToolSelectionChanged(ToolSelectionChangedEvent event) {
        currentSelection = event.getToolSelection();
        toolsView.getToolbar().setSelection(currentSelection);
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
                announcer.schedule(new SuccessAnnouncementConfig(
                        "Your tool " + s.getName() + " is added."));
                if(editToolDialog != null) {
                    editToolDialog.hide();
                }

            }
        });
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
            public void onSuccess(EditToolDialog etd) {
                editToolDialog = etd;
                editToolDialog.setSize("600px", "600px");
                editToolDialog.show();
            }
        });

    }

    @Override
    public void onDeleteToolsSelected(final DeleteToolSelected event) {
        Tool tool = currentSelection.get(0);
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
    public void onShareToolselected(final ShareToolsSelected event) {
        shareDialogProvider.get(new AsyncCallback<ToolSharingDialog>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ToolSharingDialog o) {
                sharingView.setSelectedTools(currentSelection);
                o.add(sharingView.asWidget());
                permissionsPanel = permFactory.create(ManageToolsViewPresenter.this,
                                                      getSharedResources(currentSelection));
                permissionsPanel.setExplainPanelVisibility(false);
                sharingView.addShareWidget(permissionsPanel.asWidget());
                loadPermissions();
                o.show();
            }
        });
    }

    private FastMap<SharedResource> getSharedResources(List<Tool> selectedResources) {
        FastMap<SharedResource> resourcesMap = new FastMap<>();
        for (Tool sr : selectedResources) {
            resourcesMap.put(sr.getId(), new SharedResource(null, sr.getId(), sr.getName()));
        }
        return resourcesMap;
    }

    @Override
    public void loadResources() {
        // DO Nothing
    }

    @Override
    public void loadPermissions() {
        permissionsPanel.mask();
        dcService.getPermissions(currentSelection, new LoadPermissionsCallback());
    }

    @Override
    public PermissionValue getDefaultPermissions() {
        return PermissionValue.read;
    }

    @Override
    public void processRequest() {
        ToolSharingRequestList request = buildSharingRequest();
        ToolUnSharingRequestList unshareRequest = buildUnSharingRequest();
        if (request != null) {
            callSharingService(request);
        }

        if (unshareRequest != null) {
            callUnshareService(unshareRequest);
        }

        if (request != null || unshareRequest != null) {
            IplantAnnouncer.getInstance().schedule("Sharing request submitted");
        }

    }

    private ToolSharingRequestList buildSharingRequest() {
       ToolSharingRequestList sharingRequestList = null;

        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            List<ToolSharingRequest> requests = new ArrayList<>();

            for (String userName : sharingMap.keySet()) {
                ToolSharingRequest sharingRequest = sharingAutoBeanFactory.toolSharingRequest().as();
                List<Sharing> shareList = sharingMap.get(userName);
                sharingRequest.setUser(userName);
                sharingRequest.setToolPermissions(buildShareToolPermissionList(shareList));
                requests.add(sharingRequest);
            }

            sharingRequestList = sharingAutoBeanFactory.toolSharingRequestList().as();
            sharingRequestList.setToolSharingRequestList(requests);
        }

        return sharingRequestList;
    }

    private ToolUnSharingRequestList buildUnSharingRequest() {
        ToolUnSharingRequestList unsharingRequestList = null;

        FastMap<List<Sharing>> unSharingMap = permissionsPanel.getUnshareList();

        if (unSharingMap != null && unSharingMap.size() > 0) {
            List<ToolUnsharingRequest> requests = new ArrayList<>();

            for (String userName : unSharingMap.keySet()) {
                List<Sharing> shareList = unSharingMap.get(userName);

                ToolUnsharingRequest unsharingRequest = sharingAutoBeanFactory.toolUnSharingRequest().as();
                unsharingRequest.setUser(userName);
                unsharingRequest.setTools(buildUnshareToolPermissionList(shareList));
                requests.add(unsharingRequest);
            }

            unsharingRequestList = sharingAutoBeanFactory.toolUnSharingRequestList().as();
            unsharingRequestList.setToolUnSharingRequestList(requests);
        }

        return unsharingRequestList;
    }

    private List<ToolPermission> buildUnshareToolPermissionList(List<Sharing> unshareList) {
        List<ToolPermission> toolPermList = new ArrayList<>();
        for (Sharing unshare : unshareList) {
            final ToolPermission toolPerm = sharingAutoBeanFactory.toolPermission().as();
            toolPerm.setId(unshare.getId());

            toolPermList.add(toolPerm);
        }

        return toolPermList;
    }

    private List<ToolPermission> buildShareToolPermissionList(List<Sharing> shareList) {
        List<ToolPermission> appPermList = new ArrayList<>();
        for (Sharing s : shareList) {
            ToolPermission toolPerm = sharingAutoBeanFactory.toolPermission().as();
            toolPerm.setId(s.getId());
            toolPerm.setPermission(s.getPermission().toString());
            appPermList.add(toolPerm);
        }
        return appPermList;
    }

    private void callSharingService(ToolSharingRequestList obj) {
        dcService.shareTool(obj, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing intentionally
            }
        });
    }

    private void callUnshareService(ToolUnSharingRequestList obj) {
        dcService.unShareTool(obj, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing
            }
        });
    }

}
