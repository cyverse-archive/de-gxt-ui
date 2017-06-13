package org.iplantc.de.diskResource.client.presenters.sharing;

import org.iplantc.de.commons.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;

import org.iplantc.de.client.models.diskResources.sharing.DataPermission;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingRequest;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUnsharingRequest;
import org.iplantc.de.client.models.diskResources.sharing.DataUnsharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUserPermission;
import org.iplantc.de.client.models.diskResources.sharing.DataUserPermissionList;
import org.iplantc.de.client.models.sharing.OldUserPermission;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DataSharingView;
import org.iplantc.de.diskResource.share.DiskResourceModule;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sriram, jstroot
 */
public class DataSharingPresenterImpl implements SharingPresenter {

    class GetUserInfoCallback implements AsyncCallback<FastMap<Subject>> {

        private DataUserPermissionList dataPermsList;

        private GetUserInfoCallback(DataUserPermissionList dataPermsList) {
            this.dataPermsList = dataPermsList;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(FastMap<Subject> results) {
            FastMap<List<Sharing>> dataSharingMap = new FastMap<>();
            for (DataUserPermission dataPermission : dataPermsList.getDataUserPermissions()) {
                for (OldUserPermission userPerms : dataPermission.getUserPermissions()) {
                    String userName = userPerms.getUser();

                    Subject user = results.get(userName);
                    if (user == null) {
                        user = collaboratorsUtil.getDummySubject(userName);
                    }

                    List<Sharing> dataShares = dataSharingMap.get(userName);

                    if (dataShares == null) {
                        dataShares = new ArrayList<>();
                        dataSharingMap.put(userName, dataShares);
                    }

                    String path = dataPermission.getPath();
                    Sharing dataSharing = new Sharing(user,
                                                      PermissionValue.valueOf(userPerms.getPermission()),
                                                      path,
                                                      DiskResourceUtil.getInstance()
                                                                      .parseNameFromPath(path));
                    dataShares.add(dataSharing);
                }
            }
            permissionsPanel.loadSharingData(dataSharingMap);
            permissionsPanel.unmask();
        }
    }


    class LoadPermissionsCallback extends DataCallback<String> {

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            permissionsPanel.unmask();
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            DataUserPermissionList dataPermsList =
                    AutoBeanCodex.decode(dataSharingFactory, DataUserPermissionList.class, result).as();

            final List<String> usernames = new ArrayList<>();

            for (DataUserPermission dataUserPermission : dataPermsList.getDataUserPermissions()) {
                for (OldUserPermission up : dataUserPermission.getUserPermissions()) {
                    usernames.add(up.getUser());
                }
            }
            collaboratorsServiceFacade.getUserInfo(usernames, new GetUserInfoCallback(dataPermsList));
        }

    }

    final DataSharingView view;
    private final DiskResourceServiceFacade diskResourceService;
    private final SharingPermissionView permissionsPanel;
    private final List<DiskResource> selectedResources;
    private final Appearance appearance;
    private DataSharingAutoBeanFactory dataSharingFactory;
    private DiskResourceAutoBeanFactory drFactory;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private final CollaboratorsUtil collaboratorsUtil;


    @Inject
    public DataSharingPresenterImpl(final DiskResourceServiceFacade diskResourceService,
                                    @Assisted final List<DiskResource> selectedResources,
                                    final DataSharingView view,
                                    final CollaboratorsUtil collaboratorsUtil,
                                    CollaboratorsServiceFacade collaboratorsServiceFacade,
                                    SharingPresenter.Appearance appearance,
                                    SharingPermissionViewFactory sharingViewFactory,
                                    DataSharingAutoBeanFactory dataSharingFactory,
                                    DiskResourceAutoBeanFactory drFactory) {
        this.diskResourceService = diskResourceService;
        this.view = view;
        this.selectedResources = selectedResources;
        this.collaboratorsUtil = collaboratorsUtil;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
        this.appearance = appearance;
        this.dataSharingFactory = dataSharingFactory;
        this.drFactory = drFactory;
        permissionsPanel = sharingViewFactory.create(this, getSelectedResourcesAsMap(selectedResources));
        view.addShareWidget(permissionsPanel.asWidget());
        loadResources();
        loadPermissions();
    }

    @Override
    public PermissionValue getDefaultPermissions() {
        return PermissionValue.read;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    @Override
    public void loadResources() {
        view.setSelectedDiskResources(selectedResources);
    }

    @Override
    public void loadPermissions() {
        permissionsPanel.mask();
        diskResourceService.getPermissions(getPaths(selectedResources), new LoadPermissionsCallback());
    }

    HasPaths getPaths(List<DiskResource> selectedResources) {
        HasPaths hasPaths = drFactory.pathsList().as();
        List<String> paths = new ArrayList<>();

        for (DiskResource resource : selectedResources) {
            String path = resource.getPath();
            paths.add(path);
        }

        hasPaths.setPaths(paths);
        return hasPaths;
    }

    @Override
    public void processRequest() {
        DataSharingRequestList requestBody = buildSharingRequest();
        DataUnsharingRequestList unshareRequestBody = buildUnsharingRequest();
        if (requestBody != null) {
            callSharingService(requestBody);
        }

        if (unshareRequestBody != null) {
            callUnshareService(unshareRequestBody);
        }

        if (requestBody != null || unshareRequestBody != null) {
            IplantAnnouncer.getInstance().schedule(appearance.sharingCompleteMsg());
        }

    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId + DiskResourceModule.Ids.SHARING_VIEW);
        permissionsPanel.asWidget().ensureDebugId(debugId + DiskResourceModule.Ids.SHARING_VIEW + DiskResourceModule.Ids.SHARING_PERMS);
    }

    private List<DataPermission> buildShareDataPermissionList(List<Sharing> shareList) {
        List<DataPermission> dataPermList = new ArrayList<>();

        for(Sharing sharing : shareList) {
            DataPermission dataPerm = dataSharingFactory.getDataPermission().as();
            dataPerm.setPath(sharing.getId());
            dataPerm.setPermission(sharing.getPermission().toString());
            dataPermList.add(dataPerm);
        }

        return dataPermList;
    }

    DataSharingRequestList buildSharingRequest() {
        DataSharingRequestList sharingRequestList = null;

        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            List<DataSharingRequest> requests = new ArrayList<>();

            for (String userName : sharingMap.keySet()) {
                DataSharingRequest sharingRequest = dataSharingFactory.getDataSharingRequest().as();
                sharingRequest.setUser(userName);
                List<Sharing> shareList = sharingMap.get(userName);
                sharingRequest.setDataPermissions(buildShareDataPermissionList(shareList));
                requests.add(sharingRequest);
            }

            sharingRequestList = dataSharingFactory.getDataSharingRequestList().as();
            sharingRequestList.setDataSharingRequestList(requests);
        }

        return sharingRequestList;
    }

    DataUnsharingRequestList buildUnsharingRequest() {
        DataUnsharingRequestList unsharingRequestList = null;

        FastMap<List<Sharing>> unsharingMap = permissionsPanel.getUnshareList();

        if (unsharingMap != null && unsharingMap.size() > 0) {
            List<DataUnsharingRequest> requests = new ArrayList<>();

            for (String userName : unsharingMap.keySet()) {
                DataUnsharingRequest unsharingRequest = dataSharingFactory.getDataUnsharingRequest().as();
                unsharingRequest.setUser(userName);
                List<Sharing> unshareList = unsharingMap.get(userName);
                unsharingRequest.setPaths(buildUnsharePathList(unshareList));
                requests.add(unsharingRequest);
            }

            unsharingRequestList = dataSharingFactory.getDataUnsharingRequestList().as();
            unsharingRequestList.setDataUnsharingRequests(requests);
        }

        return unsharingRequestList;
    }

    List<String> buildUnsharePathList(List<Sharing> shareList) {
        List<String> paths = new ArrayList<>();
        for(Sharing unshare : shareList) {
            String path = unshare.getId();
            paths.add(path);
        }
        return paths;
    }

    void callSharingService(DataSharingRequestList requestList) {
        diskResourceService.shareDiskResource(requestList, new AsyncCallback<String>() {

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

    void callUnshareService(DataUnsharingRequestList unshareRequestList) {
        diskResourceService.unshareDiskResource(unshareRequestList, new AsyncCallback<String>() {

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

    private FastMap<SharedResource> getSelectedResourcesAsMap(List<DiskResource> selectedResources) {
        FastMap<SharedResource> resourcesMap = new FastMap<>();
        for (DiskResource sr : selectedResources) {
            resourcesMap.put(sr.getPath(),
                             new SharedResource(sr.getPath(),
                                                DiskResourceUtil.getInstance()
                                                                .parseNameFromPath(sr.getPath())));
        }
        return resourcesMap;
    }
}
