/**
 * @author sriram
 */

package org.iplantc.de.analysis.client.presenter.sharing;

import org.iplantc.de.analysis.client.views.sharing.AnalysisSharingView;
import org.iplantc.de.commons.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.sharing.AnalysisPermission;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingAutoBeanFactory;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequest;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequestList;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUnsharingRequest;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUnsharingRequestList;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUserPermissions;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUserPermissionsList;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.diskResources.PermissionValue;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.models.sharing.SharingSubject;
import org.iplantc.de.client.models.sharing.UserPermission;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AnalysisCallback;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

import java.util.ArrayList;
import java.util.List;

public class AnalysisSharingPresenter implements SharingPresenter {


    private final class LoadPermissionsCallback extends AnalysisCallback<String> {
        private final class GetUserInfoCallback implements AsyncCallback<FastMap<Subject>> {
            private final AnalysisUserPermissionsList analysisPermsList;

            private GetUserInfoCallback(AnalysisUserPermissionsList analysisPermsList) {
                this.analysisPermsList = analysisPermsList;
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(FastMap<Subject> results) {
                FastMap<List<Sharing>> sharingMap = new FastMap<>();
                for (AnalysisUserPermissions analysisUserPerms : analysisPermsList.getResourceUserPermissionsList()) {
                    for (UserPermission userPerms: analysisUserPerms.getPermissions()) {

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
                                                      analysisUserPerms.getId(),
                                                      analysisUserPerms.getName());
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
            AnalysisUserPermissionsList analysisPermsList = AutoBeanCodex.decode(shareFactory,
                                                                                 AnalysisUserPermissionsList.class,
                                                                                 result).as();
            final List<String> usernames = Lists.newArrayList();
            for (AnalysisUserPermissions analysisUserPerms : analysisPermsList.getResourceUserPermissionsList()) {
                for (UserPermission userPerm : analysisUserPerms.getPermissions()) {
                    usernames.add(userPerm.getSubject().getId());
                }
            }
            collaboratorsServiceFacade.getUserInfo(usernames, new GetUserInfoCallback(analysisPermsList));
        }

    }

    final AnalysisSharingView sharingView;
    private final SharingPermissionView permissionsPanel;
    private final List<Analysis> selectedAnalysis;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private Appearance appearance;
    private final CollaboratorsUtil collaboratorsUtil;
    private final AnalysisServiceFacade aService;
    private AnalysisSharingAutoBeanFactory shareFactory = GWT.create(AnalysisSharingAutoBeanFactory.class);

    @Inject
    public AnalysisSharingPresenter(final AnalysisServiceFacade aService,
                                    @Assisted final List<Analysis> selectedAnalysis,
                                    final AnalysisSharingView view,
                                    final CollaboratorsUtil collaboratorsUtil,
                                    CollaboratorsServiceFacade collaboratorsServiceFacade,
                                    SharingPermissionViewFactory sharingViewFactory,
                                    Appearance appearance) {

        this.sharingView = view;
        this.aService = aService;
        this.collaboratorsUtil = collaboratorsUtil;
        this.selectedAnalysis = selectedAnalysis;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
        this.appearance = appearance;
        this.permissionsPanel = sharingViewFactory.create(this, getSelectedResourcesAsMap(this.selectedAnalysis));
        permissionsPanel.hidePermissionColumn();
        permissionsPanel.setExplainPanelVisibility(false);
        view.addShareWidget(permissionsPanel.asWidget());
        loadResources();
        loadPermissions();
    }

    private FastMap<SharedResource> getSelectedResourcesAsMap(List<Analysis> selectedAnalysis) {
        FastMap<SharedResource> resourcesMap = new FastMap<>();
        for (Analysis sr : selectedAnalysis) {
            resourcesMap.put(sr.getId(), new SharedResource(sr.getId(), sr.getName()));
        }
        return resourcesMap;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(sharingView.asWidget());
   }

    @Override
    public void loadResources() {
        sharingView.setSelectedAnalysis(selectedAnalysis);
   }

    @Override
    public void loadPermissions() {
       permissionsPanel.mask();
       aService.getPermissions(selectedAnalysis,new LoadPermissionsCallback());

    }

    @Override
    public PermissionValue getDefaultPermissions() {
        return PermissionValue.read;
    }

    @Override
    public void processRequest() {
       AnalysisSharingRequestList request = buildSharingRequest();
       AnalysisUnsharingRequestList  unshareRequest = buildUnsharingRequest();

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

    private AnalysisSharingRequestList buildSharingRequest() {
        AnalysisSharingRequestList sharingRequestList = null;

        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            List<AnalysisSharingRequest> requests = new ArrayList<>();

            for (String userName : sharingMap.keySet()) {
                AnalysisSharingRequest sharingRequest = shareFactory.AnalysisSharingRequest().as();
                SharingSubject sharingSubject = shareFactory.getSharingSubject().as();
                List<Sharing> shareList = sharingMap.get(userName);
                sharingSubject.setSourceId(getSourceId(shareList));
                sharingSubject.setId(userName);
                sharingRequest.setSubject(sharingSubject);
                sharingRequest.setAnalysisPermissions(buildAnalysisPermissions(shareList));
                requests.add(sharingRequest);
            }

            sharingRequestList = shareFactory.AnalysisSharingRequestList().as();
            sharingRequestList.setAnalysisSharingRequestList(requests);
        }
        return sharingRequestList;
    }

    String getSourceId(List<Sharing> shareList) {
        Sharing share = shareList.get(0);
        return share.getSourceId();
    }

    private List<AnalysisPermission> buildAnalysisPermissions(List<Sharing> shareList) {
        List<AnalysisPermission> aPermList = new ArrayList<>();
        for (Sharing s : shareList) {
            AnalysisPermission aPerm = shareFactory.analysisPermission().as();
            aPerm.setId(s.getId());
            aPerm.setPermission(getDefaultPermissions().toString());
            aPermList.add(aPerm);
        }
        return aPermList;
    }

    private AnalysisUnsharingRequestList buildUnsharingRequest() {
        AnalysisUnsharingRequestList unsharingRequestList = null;

        FastMap<List<Sharing>> unSharingMap = permissionsPanel.getUnshareList();

        if (unSharingMap != null && unSharingMap.size() > 0) {
            List<AnalysisUnsharingRequest> requests = new ArrayList<>();

            for (String userName : unSharingMap.keySet()) {
                List<Sharing> shareList = unSharingMap.get(userName);

                AnalysisUnsharingRequest unsharingRequest = shareFactory.AnalysisUnsharingRequest().as();
                SharingSubject sharingSubject = shareFactory.getSharingSubject().as();
                sharingSubject.setSourceId(getSourceId(shareList));
                sharingSubject.setId(userName);
                unsharingRequest.setSubject(sharingSubject);
                unsharingRequest.setAnalyses(buildUnshareAnalysisPermissionList(shareList));
                requests.add(unsharingRequest);
            }
            unsharingRequestList = shareFactory.AnalysisUnsharingRequestList().as();
            unsharingRequestList.setAnalysisUnSharingRequestList(requests);

        }
        return unsharingRequestList;
    }

    private List<String> buildUnshareAnalysisPermissionList(List<Sharing> unshareList) {
        List<String> analysisPermList = Lists.newArrayList();
        for (Sharing unshare : unshareList) {
            final AnalysisPermission analysisPerm = shareFactory.analysisPermission().as();
            analysisPermList.add(unshare.getId());
        }

        return analysisPermList;
    }


    private void callSharingService(AnalysisSharingRequestList obj) {
        aService.shareAnalyses(obj, new AnalysisCallback<String>() {

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

    private void callUnshareService(AnalysisUnsharingRequestList obj) {
        aService.unshareAnalyses(obj, new AnalysisCallback<String>() {

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
