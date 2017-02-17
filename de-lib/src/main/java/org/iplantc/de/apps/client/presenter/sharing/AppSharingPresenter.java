/**
 * @author sriram
 */
package org.iplantc.de.apps.client.presenter.sharing;

import org.iplantc.de.apps.client.views.sharing.AppSharingView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.sharing.AppPermission;
import org.iplantc.de.client.models.apps.sharing.AppSharingAutoBeanFactory;
import org.iplantc.de.client.models.apps.sharing.AppSharingRequest;
import org.iplantc.de.client.models.apps.sharing.AppSharingRequestList;
import org.iplantc.de.client.models.apps.sharing.AppUnSharingRequestList;
import org.iplantc.de.client.models.apps.sharing.AppUnsharingRequest;
import org.iplantc.de.client.models.apps.sharing.AppUserPermissions;
import org.iplantc.de.client.models.apps.sharing.AppUserPermissionsList;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.diskResources.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.models.sharing.UserPermission;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.sharing.SharingPermissionsPanel;
import org.iplantc.de.client.sharing.SharingPresenter;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AppsCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.List;

public class AppSharingPresenter implements SharingPresenter {

    private final class LoadPermissionsCallback extends AppsCallback<String> {
        private final class GetUserInfoCallback implements AsyncCallback<FastMap<Collaborator>> {
            private final AppUserPermissionsList appPermsList;

            private GetUserInfoCallback(AppUserPermissionsList appPermsList) {
                this.appPermsList = appPermsList;
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(FastMap<Collaborator> results) {
                FastMap<List<Sharing>> sharingMap = new FastMap<>();
                for (AppUserPermissions appUserPerms : appPermsList.getResourceUserPermissionsList()) {
                    for (UserPermission userPerms : appUserPerms.getPermissions()) {
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
                                                      appUserPerms.getSystemId(),
                                                      appUserPerms.getAppId(),
                                                      appUserPerms.getName());
                        shares.add(sharing);
                    }
                }

                permissionsPanel.loadSharingData(sharingMap);
                permissionsPanel.unmask();
            }
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            permissionsPanel.unmask();
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            AppUserPermissionsList appPermsList = AutoBeanCodex.decode(shareFactory,
                                                                       AppUserPermissionsList.class,
                                                                       result).as();
            final List<String> usernames = new ArrayList<>();
            for (AppUserPermissions appUserPerms : appPermsList.getResourceUserPermissionsList()) {
                for (UserPermission up : appUserPerms.getPermissions()) {
                    usernames.add(up.getUser());
                }
            }

            collaboratorsUtil.getUserInfo(usernames, new GetUserInfoCallback(appPermsList));
        }

    }

    final AppSharingView view;
    private final SharingPermissionsPanel permissionsPanel;
    private final List<App> selectedApps;
    private final AppUserServiceFacade appService;
    private Appearance appearance;
    private final CollaboratorsUtil collaboratorsUtil;
    private AppAutoBeanFactory appFactory = GWT.create(AppAutoBeanFactory.class);
    private AppSharingAutoBeanFactory shareFactory = GWT.create(AppSharingAutoBeanFactory.class);


    public AppSharingPresenter(final AppUserServiceFacade appService,
                               final List<App> selectedApps,
                               final AppSharingView view,
                               final CollaboratorsUtil collaboratorsUtil) {
        this(appService,
             selectedApps,
             view,
             collaboratorsUtil,
             GWT.<SharingPresenter.Appearance>create(SharingPresenter.Appearance.class));
    }

    public AppSharingPresenter(final AppUserServiceFacade appService,
                               final List<App> selectedApps,
                               final AppSharingView view,
                               final CollaboratorsUtil collaboratorsUtil,
                               Appearance appearance) {

        this.view = view;
        this.appearance = appearance;
        this.appService = appService;
        view.setPresenter(this);
        this.collaboratorsUtil = collaboratorsUtil;
        this.selectedApps = selectedApps;
        this.permissionsPanel = new SharingPermissionsPanel(this, getSelectedApps(selectedApps));
        view.addShareWidget(permissionsPanel.asWidget());
        loadResources();
        loadPermissions();

    }

    private FastMap<SharedResource> getSelectedApps(List<App> selectedResources) {
        FastMap<SharedResource> resourcesMap = new FastMap<>();
        for (App sr : selectedResources) {
            resourcesMap.put(sr.getId(), new SharedResource(sr.getSystemId(), sr.getId(), sr.getName()));
        }
        return resourcesMap;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());

    }

    @Override
    public void loadResources() {
        view.setSelectedApps(selectedApps);

    }

    @Override
    public void loadPermissions() {
        permissionsPanel.mask();
        appService.getPermissions(selectedApps, new LoadPermissionsCallback());
    }

    @Override
    public PermissionValue getDefaultPermissions() {
        return PermissionValue.read;
    }

    @Override
    public void processRequest() {
        AppSharingRequestList request = buildSharingRequest();
        AppUnSharingRequestList unshareRequest = buildUnSharingRequest();
        if (request != null) {
            callSharingService(request);
        }

        if (unshareRequest != null) {
            callUnshareService(unshareRequest);
        }

        if (request != null || unshareRequest != null) {
            IplantAnnouncer.getInstance().schedule(appearance.sharingCompleteMsg());
        }

    }

    private AppSharingRequestList buildSharingRequest() {
        AppSharingRequestList sharingRequestList = null;

        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            List<AppSharingRequest> requests = new ArrayList<>();

            for (String userName : sharingMap.keySet()) {
                AppSharingRequest sharingRequest = appFactory.appSharingRequest().as();
                List<Sharing> shareList = sharingMap.get(userName);
                sharingRequest.setUser(userName);
                sharingRequest.setAppPermissions(buildShareAppPermissionList(shareList));
                requests.add(sharingRequest);
            }

            sharingRequestList = appFactory.appSharingRequestList().as();
            sharingRequestList.setAppSharingRequestList(requests);
        }

        return sharingRequestList;
    }

    private AppUnSharingRequestList buildUnSharingRequest() {
        AppUnSharingRequestList unsharingRequestList = null;

        FastMap<List<Sharing>> unSharingMap = permissionsPanel.getUnshareList();

        if (unSharingMap != null && unSharingMap.size() > 0) {
            List<AppUnsharingRequest> requests = new ArrayList<>();

            for (String userName : unSharingMap.keySet()) {
                List<Sharing> shareList = unSharingMap.get(userName);

                AppUnsharingRequest unsharingRequest = appFactory.appUnSharingRequest().as();
                unsharingRequest.setUser(userName);
                unsharingRequest.setApps(buildUnshareAppPermissionList(shareList));
                requests.add(unsharingRequest);
            }

            unsharingRequestList = appFactory.appUnSharingRequestList().as();
            unsharingRequestList.setAppUnSharingRequestList(requests);
        }

        return unsharingRequestList;
    }

    private List<AppPermission> buildUnshareAppPermissionList(List<Sharing> unshareList) {
        List<AppPermission> appPermList = new ArrayList<>();
        for (Sharing unshare : unshareList) {
            final AppPermission appPerm = shareFactory.AppPermission().as();
            appPerm.setSystemId(unshare.getSystemId());
            appPerm.setAppId(unshare.getId());

            appPermList.add(appPerm);
        }

        return appPermList;
    }

    private List<AppPermission> buildShareAppPermissionList(List<Sharing> shareList) {
        List<AppPermission> appPermList = new ArrayList<>();
        for (Sharing s : shareList) {
            AppPermission appPerm = shareFactory.AppPermission().as();
            appPerm.setAppId(s.getId());
            appPerm.setSystemId(s.getSystemId());
            appPerm.setPermission(s.getPermission().toString());
            appPermList.add(appPerm);
        }
        return appPermList;
    }

    private void callSharingService(AppSharingRequestList obj) {
        appService.shareApp(obj, new AppsCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing intentionally
            }
        });
    }

    private void callUnshareService(AppUnSharingRequestList obj) {
        appService.unshareApp(obj, new AppsCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing
            }
        });
    }

}
