package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.models.sharing.SharingSubject;
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
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.tools.client.views.manage.ToolSharingPresenter;
import org.iplantc.de.tools.client.views.manage.ToolSharingView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sriram on 5/3/17.
 */
public class ToolSharingPresenterImpl implements ToolSharingPresenter {

    private final class LoadPermissionsCallback extends AppsCallback<String> {
        private final class GetUserInfoCallback implements AsyncCallback<FastMap<Subject>> {
            private final ToolUserPermissionsList toolPermsList;

            private GetUserInfoCallback(ToolUserPermissionsList toolPermsList) {
                this.toolPermsList = toolPermsList;
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(FastMap<Subject> results) {
                FastMap<List<Sharing>> sharingMap = new FastMap<>();
                for (ToolUserPermissions toolUserPerms : toolPermsList.getResourceUserPermissionsList()) {
                    for (UserPermission userPerms : toolUserPerms.getPermissions()) {
                        String userName = userPerms.getSubject().getId();

                        Subject user = results.get(userName);
                        if (user == null) {
                            user = collaboratorsUtil.getDummySubject(userName);
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
        public void onFailure(Integer statusCode, Throwable exception) {
            permissionsPanel.unmask();
            ErrorHandler.post(exception);
        }

        @Override
        public void onSuccess(String result) {
            ToolUserPermissionsList toolPermsList =
                    AutoBeanCodex.decode(sharingAutoBeanFactory, ToolUserPermissionsList.class, result)
                                 .as();
            final List<String> usernames = new ArrayList<>();
            for (ToolUserPermissions toolUserPerms : toolPermsList.getResourceUserPermissionsList()) {
                for (UserPermission up : toolUserPerms.getPermissions()) {
                    usernames.add(up.getSubject().getId());
                }
            }

            collaboratorsServiceFacade.getUserInfo(usernames,
                                                   new ToolSharingPresenterImpl.LoadPermissionsCallback.GetUserInfoCallback(
                                                           toolPermsList));
        }

    }


    @Inject
    IplantAnnouncer announcer;

    @Inject
    SharingPermissionViewFactory permFactory;

    @Inject
    ToolAutoBeanFactory toolAutoBeanFactory;

    @Inject
    ToolSharingAutoBeanFactory sharingAutoBeanFactory;

    ToolServices dcService;

    private SharingPermissionView permissionsPanel;
    private final CollaboratorsUtil collaboratorsUtil;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private final ToolSharingView sharingView;
    private final SharingPresenter.Appearance appearance;
    private final List<Tool> selectedTools;

    @Inject
    public ToolSharingPresenterImpl(final ToolServices dcService,
                                    @Assisted final List<Tool> selectedTools,
                                    final ToolSharingView view,
                                    CollaboratorsUtil collaboratorsUtil,
                                    CollaboratorsServiceFacade collaboratorsServiceFacade,
                                    SharingPresenter.Appearance appearance,
                                    SharingPermissionViewFactory sharingViewFactory) {
        this.collaboratorsUtil = collaboratorsUtil;
        this.dcService = dcService;
        this.sharingView = view;
        this.appearance = appearance;
        this.selectedTools = selectedTools;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
        this.permissionsPanel = sharingViewFactory.create(this, getSharedResources(selectedTools));
        view.addShareWidget(permissionsPanel.asWidget());
        loadResources();
        loadPermissions();
    }

    private FastMap<SharedResource> getSharedResources(List<Tool> selectedResources) {
        FastMap<SharedResource> resourcesMap = new FastMap<>();
        for (Tool sr : selectedResources) {
            resourcesMap.put(sr.getId(), new SharedResource(null, sr.getId(), sr.getName()));
        }
        return resourcesMap;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(sharingView.asWidget());
    }

    @Override
    public void loadResources() {
        sharingView.setSelectedTools(selectedTools);
    }

    @Override
    public void loadPermissions() {
        permissionsPanel.mask();
        dcService.getPermissions(selectedTools, new ToolSharingPresenterImpl.LoadPermissionsCallback());
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
            announcer.schedule(new IplantAnnouncementConfig(appearance.sharingCompleteMsg()));
        }

        if (unshareRequest != null) {
            callUnshareService(unshareRequest);
            announcer.schedule(new IplantAnnouncementConfig(appearance.sharingCompleteMsg()));
        }
    }

    @Override
    public void setViewDebugId(String debugId) {
        sharingView.asWidget().ensureDebugId(debugId + ToolsModule.ToolIds.SHARING_VIEW);
        permissionsPanel.asWidget().ensureDebugId(debugId + ToolsModule.ToolIds.SHARING_VIEW + ToolsModule.ToolIds.SHARING_PERMS);
    }

    private ToolSharingRequestList buildSharingRequest() {
        ToolSharingRequestList sharingRequestList = null;

        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            List<ToolSharingRequest> requests = new ArrayList<>();

            for (String userName : sharingMap.keySet()) {
                ToolSharingRequest sharingRequest = sharingAutoBeanFactory.toolSharingRequest().as();
                List<Sharing> shareList = sharingMap.get(userName);
                SharingSubject sharingSubject = buildSharingSubject(userName, shareList);
                sharingRequest.setSubject(sharingSubject);
                sharingRequest.setToolPermissions(buildShareToolPermissionList(shareList));
                requests.add(sharingRequest);
            }

            sharingRequestList = sharingAutoBeanFactory.toolSharingRequestList().as();
            sharingRequestList.setToolSharingRequestList(requests);
        }

        return sharingRequestList;
    }

    private SharingSubject buildSharingSubject(String userName, List<Sharing> shareList) {
        SharingSubject sharingSubject = sharingAutoBeanFactory.getSharingSubject().as();
        sharingSubject.setSourceId(getSourceId(shareList));
        sharingSubject.setId(userName);
        return sharingSubject;
    }

    private ToolUnSharingRequestList buildUnSharingRequest() {
        ToolUnSharingRequestList unsharingRequestList = null;

        FastMap<List<Sharing>> unSharingMap = permissionsPanel.getUnshareList();

        if (unSharingMap != null && unSharingMap.size() > 0) {
            List<ToolUnsharingRequest> requests = new ArrayList<>();

            for (String userName : unSharingMap.keySet()) {
                List<Sharing> shareList = unSharingMap.get(userName);

                ToolUnsharingRequest unsharingRequest =
                        sharingAutoBeanFactory.toolUnSharingRequest().as();
                SharingSubject sharingSubject = buildSharingSubject(userName, shareList);
                unsharingRequest.setSubject(sharingSubject);
                unsharingRequest.setTools(buildUnshareToolPermissionList(shareList));
                requests.add(unsharingRequest);
            }

            unsharingRequestList = sharingAutoBeanFactory.toolUnSharingRequestList().as();
            unsharingRequestList.setToolUnSharingRequestList(requests);
        }

        return unsharingRequestList;
    }

    String getSourceId(List<Sharing> shareList) {
        Sharing share = shareList.get(0);
        return share.getSourceId();
    }

    private List<String> buildUnshareToolPermissionList(List<Sharing> unshareList) {
        List<String> toolPermList = new ArrayList<>();
        for (Sharing unshare : unshareList) {
            toolPermList.add(unshare.getId());
        }

        return toolPermList;
    }

    private List<ToolPermission> buildShareToolPermissionList(List<Sharing> shareList) {
        List<ToolPermission> toolPermList = new ArrayList<>();
        for (Sharing s : shareList) {
            ToolPermission toolPerm = sharingAutoBeanFactory.toolPermission().as();
            toolPerm.setId(s.getId());
            toolPerm.setPermission(s.getPermission().toString());
            toolPermList.add(toolPerm);
        }
        return toolPermList;
    }

    private void callSharingService(ToolSharingRequestList obj) {
        dcService.shareTool(obj, new AppsCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing intentionally
            }
        });
    }

    private void callUnshareService(ToolUnSharingRequestList obj) {
        dcService.unShareTool(obj, new AppsCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing
            }
        });
    }
}
