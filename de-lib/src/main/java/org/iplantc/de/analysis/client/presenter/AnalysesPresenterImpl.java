package org.iplantc.de.analysis.client.presenter;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.events.OpenAppForRelaunchEvent;
import org.iplantc.de.analysis.client.events.UpdateAnalysesWindowTitleEvent;
import org.iplantc.de.analysis.client.models.FilterAutoBeanFactory;
import org.iplantc.de.analysis.client.models.FilterBeanList;
import org.iplantc.de.analysis.client.views.ViceLogsView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.AnalysesList;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.client.models.analysis.AnalysisPermissionFilter;
import org.iplantc.de.client.models.analysis.VICELogs;
import org.iplantc.de.client.models.analysis.sharing.AnalysisPermission;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingAutoBeanFactory;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequest;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequestList;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportAutoBeanFactory;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequest;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequestFields;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.SharingSubject;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.DEUserSupportServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AnalysisCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A presenter for analyses view
 *
 * @author sriram, jstroot
 */
public class AnalysesPresenterImpl implements AnalysesView.Presenter {


    private final class CompleteAnalysisServiceCallback extends AnalysisCallback<String> {
        private final String analysisName;
        final ReactSuccessCallback callback;
        final ReactErrorCallback errorCallback;

        public CompleteAnalysisServiceCallback(final String analysisName,
                                               final ReactSuccessCallback callback,
                                               final ReactErrorCallback errorCallback) {
            this.analysisName = analysisName;
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.stopAnalysisError(analysisName));
            announcer.schedule(new ErrorAnnouncementConfig(msg, true, 3000));
            if (errorCallback != null) {
                errorCallback.onError(statusCode, caught.getMessage());
            }
        }

        @Override
        public void onSuccess(String result) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.analysisStopSuccess(analysisName));
            announcer.schedule(new SuccessAnnouncementConfig(msg, true, 3000));
            if (callback != null) {
                callback.onSuccess(null);
            }
        }

    }

    private final class CancelAnalysisServiceCallback extends AnalysisCallback<String> {
        private final String analysisName;
        private final ReactSuccessCallback callback;
        private final ReactErrorCallback errorCallback;

        public CancelAnalysisServiceCallback(final String analysisName,
                                             final ReactSuccessCallback callback,
                                             final ReactErrorCallback errorCallback) {
            this.analysisName = analysisName;
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Integer statusCode,
                              Throwable caught) {
            /*
             * JDS Send generic error message. In the future, the "error_code" string should be parsed
             * from the JSON to provide more detailed user feedback.
             */
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.stopAnalysisError(analysisName));
            announcer.schedule(new ErrorAnnouncementConfig(msg, true, 3000));
            if (errorCallback != null) {
                errorCallback.onError(statusCode, caught.getMessage());
            }
        }

        @Override
        public void onSuccess(String result) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.analysisStopSuccess(analysisName));
            announcer.schedule(new SuccessAnnouncementConfig(msg, true, 3000));
            if (callback != null) {
                callback.onSuccess(null);
            }
        }

    }

    @Inject
    AnalysisServiceFacade analysisService;
    @Inject
    AsyncProviderWrapper<DEUserSupportServiceFacade> supportServiceProvider;
    @Inject
    IplantAnnouncer announcer;
    @Inject
    AnalysesView.Presenter.Appearance appearance;
    @Inject
    AsyncProviderWrapper<AnalysisSharingDialog> aSharingDialogProvider;

    @Inject
    DEProperties deProperties;
    @Inject
    AnalysisSupportAutoBeanFactory supportFactory;
    @Inject
    AnalysisSharingAutoBeanFactory shareFactory;
    @Inject
    UserInfo userInfo;

    @Inject
    AnalysesView view;
    @Inject
    FilterAutoBeanFactory filterAutoBeanFactory;
    @Inject
    AnalysesAutoBeanFactory factory;
    @Inject
    ViceLogsView viceLogsView;

    AnalysisPermissionFilter currentPermFilter;
    AppTypeFilter currentTypeFilter;

    private String viceLogsAnalysisId;
    private String viceLogsSinceTime = "0";
    private String viceLogsAnalysisName;
    private Timer viceLogsFollowTimer;
    private final HasHandlers eventBus;
    private String baseDebugId;


    @Inject
    AnalysesPresenterImpl(final EventBus eventBus) {
        this.eventBus = eventBus;

        //Set default filter to ALL
        currentPermFilter = AnalysisPermissionFilter.ALL;
        currentTypeFilter = AppTypeFilter.ALL;
    }


    @Override
    public void handleViewAndTypeFilterChange(String viewFilter,
                                              String appTypeFilter) {
        view.updateFilter(viewFilter, appTypeFilter, "", "", "", "");
    }

    @Override
    public void handleBatchIconClick(String parentId,
                                     String analysisName) {
        view.updateFilter("", "","", "", "", parentId);
        UpdateAnalysesWindowTitleEvent event =
                new UpdateAnalysesWindowTitleEvent(" " + appearance.htAnalysisTitle(analysisName));
        eventBus.fireEvent(event);
    }

    @Override
    public void handleSearch(String searchTerm) {
        if (Strings.isNullOrEmpty(searchTerm)) {
            view.updateFilter("All", "All", "", "", "", "");
       } else {
            view.updateFilter("", "", searchTerm, searchTerm, "", "");
        }
    }

    @Override
    public void handleViewAllIconClick() {
        view.updateFilter("All", "All", "", "", "", "");
        UpdateAnalysesWindowTitleEvent event = new UpdateAnalysesWindowTitleEvent("");
        eventBus.fireEvent(event);
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void getAnalyses(int limit,
                            int offset,
                            Splittable filters,
                            String sortField,
                            String sortDir,
                            ReactSuccessCallback callback,
                            ReactErrorCallback errorCallback) {
        AutoBean<FilterBeanList> filterList =
                AutoBeanCodex.decode(filterAutoBeanFactory, FilterBeanList.class, filters.getPayload());

        analysisService.getAnalyses(limit,
                                    offset,
                                    filterList.as(),
                                    sortField,
                                    sortDir,
                                    new AnalysisCallback<String>() {

            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                ErrorHandler.post(exception.getMessage());
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, exception.getMessage());
                }

            }

            @Override
            public void onSuccess(String result) {
                AutoBean<AnalysesList> ret = AutoBeanCodex.decode(factory, AnalysesList.class, result);
                if (callback != null) {
                    callback.onSuccess(AutoBeanCodex.encode(ret));
                }
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onShareAnalysisSelected(Splittable[] analysisList) {
        ArrayList<Analysis> selected = new ArrayList<>();
        for(Splittable sp: analysisList) {
            Analysis analysis = AutoBeanCodex.decode(factory, Analysis.class, sp).as();
            selected.add(analysis);
        }
        aSharingDialogProvider.get(new AsyncCallback<AnalysisSharingDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(AnalysisSharingDialog asd) {
                asd.show(selected);
            }
        });
    }

    @Override
    public void onCancelAnalysisSelected(String analysisId,
                                         String analysisName,
                                         ReactSuccessCallback callback,
                                         ReactErrorCallback errorCallback) {


        analysisService.stopAnalysis(analysisId,
                                     new CancelAnalysisServiceCallback(analysisName,
                                                                       callback,
                                                                       errorCallback),
                                     AnalysisExecutionStatus.CANCELED.toString());
    }

    @Override
    public void onCompleteAnalysisSelected(String analysisId,
                                           String analysisName,
                                           ReactSuccessCallback callback,
                                           ReactErrorCallback errorCallback) {
        analysisService.stopAnalysis(analysisId, new CompleteAnalysisServiceCallback(analysisName,
                                                                         callback,
                                                                         errorCallback),
                                     AnalysisExecutionStatus.COMPLETED.toString());
    }

    @Override
    public void deleteAnalyses(String[] analysesToDelete,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback) {
        analysisService.deleteAnalyses(analysesToDelete, new AnalysisCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(appearance.deleteAnalysisError(), caught);
                if(errorCallback != null) {
                    errorCallback.onError(statusCode, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(String arg0) {
                if(callback != null) {
                  callback.onSuccess(null);
                }
            }
        });
    }

    ArrayList<Analysis> getNewAnalysisList() {
        return Lists.newArrayList();
    }

    @Override
    public void go(final HasOneWidget container,
                   String baseDebugId,
                   List<Analysis> selectedAnalyses) {
        this.baseDebugId = baseDebugId;
        container.setWidget(view);
        if (selectedAnalyses != null && selectedAnalyses.size() > 0) {
            view.load(this, baseDebugId, selectedAnalyses.get(0));
        } else {
            view.load(this, baseDebugId, null);
        }
    }

    @Override
    public AnalysisPermissionFilter getCurrentPermFilter() {
        return currentPermFilter;
    }

    @Override
    public AppTypeFilter getCurrentTypeFilter() {
        return currentTypeFilter;
    }

    @Override
    public void onAnalysisAppSelected(String analysisId,
                                      String systemId,
                                      String appId) {
        eventBus.fireEvent(new OpenAppForRelaunchEvent(analysisId, systemId, appId));
    }

    @Override
    public void onAnalysisNameSelected(String resultFolderId) {
        // Request disk resource window
        eventBus.fireEvent(new OpenFolderEvent(resultFolderId, true));
    }

    @Override
    public void renameAnalysis(String analysisId,
                               String newName,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback) {
        analysisService.renameAnalysis(analysisId, newName, new AnalysisCallback<Void>() {

            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.analysisRenameFailed(),
                                                               true,
                                                               5000));
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, exception.getMessage());
                }

            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.analysisRenameSuccess(),
                                                                 true,
                                                                 5000));
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void updateAnalysisComments(String id,
                                       String comment,
                                       ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback) {
        analysisService.updateAnalysisComments(id, comment, new AnalysisCallback<Void>() {

            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.analysisCommentUpdateFailed(),
                                                               true,
                                                               5000));
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, exception.getMessage());
                }

            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.analysisCommentUpdateSuccess(),
                                                                 true,
                                                                 5000));
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
    }

    @Override
    public void onAnalysisJobInfoSelected(String id,
                                          ReactSuccessCallback callback,
                                          ReactErrorCallback errorCallback) {
        analysisService.getAnalysisHistory(id, new AnalysisCallback<Splittable>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                IplantAnnouncer.getInstance()
                               .schedule(new ErrorAnnouncementConfig(appearance.analysisStepInfoError()));
                if(errorCallback != null) {
                    errorCallback.onError(statusCode, caught.getMessage());
                }

            }

            @Override
            public void onSuccess(Splittable history) {
                if(callback != null) {
                    callback.onSuccess(history);
                }
            }
        });

    }

    @Override
    public void getVICELogs(String id,
                            String analysisName) {
        viceLogsAnalysisId = id;
        viceLogsAnalysisName = analysisName;
        getLogs(id, analysisName);

    }

    protected void getLogs(String id,
                           String analysisName) {
        analysisService.getVICELogs(id, viceLogsSinceTime, new AnalysisCallback<String>() {

            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(exception.getMessage()));
                viceLogsView.mask(false);
            }

            @Override
            public void onSuccess(String result) {
                VICELogs viceLogs = AutoBeanCodex.decode(factory, VICELogs.class, result).as();
                String logLines = viceLogs.getLines().stream().collect(Collectors.joining("\n"));
                if (viceLogsSinceTime.equals("0")) {
                    viceLogsSinceTime = viceLogs.getSinceTime();
                    viceLogsView.load(AnalysesPresenterImpl.this, analysisName, logLines,
                                      baseDebugId);
                } else {
                    viceLogsSinceTime = viceLogs.getSinceTime();
                    viceLogsView.update(logLines);
                }
            }
        });
    }

    @Override
    public void closeViceLogsViewer() {
        viceLogsSinceTime = "0";
        cancelViceLogsFollowTimer();
        viceLogsView.closeViceLogsViewer();
    }

    @Override
    public void onFollowViceLogs(boolean follow) {
        viceLogsView.setFollowLogs(follow);
        if (follow) {
            viceLogsFollowTimer = new Timer() {
                @Override
                public void run() {
                    viceLogsView.mask(true);
                    getLogs(viceLogsAnalysisId, viceLogsAnalysisName);
                }
            };
            viceLogsFollowTimer.scheduleRepeating(deProperties.getViceLogsPollInterval());
        } else {
            cancelViceLogsFollowTimer();
        }
    }

    protected void cancelViceLogsFollowTimer() {
        if (viceLogsFollowTimer != null) {
            viceLogsFollowTimer.cancel();
        }
        viceLogsView.setFollowLogs(false);
    }

    @Override
    public void refreshViceLogs() {
        viceLogsSinceTime = "0";
        cancelViceLogsFollowTimer();
        viceLogsView.mask(true);
        getLogs(viceLogsAnalysisId, viceLogsAnalysisName);
    }



    @Override
    @SuppressWarnings("unusable-by-js")
    public void onUserSupportRequested(Splittable analysis,
                                       String comment,
                                       ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback) {
        Analysis selectedAnalysis = AutoBeanCodex.decode(factory, Analysis.class, analysis).as();
        AnalysisSupportRequest req = getAnalysisSupportRequest(selectedAnalysis, comment);
        shareWithSupport(selectedAnalysis,
                         AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(req)),
                         callback,
                         errorCallback);
    }

    protected AnalysisSupportRequest getAnalysisSupportRequest(Analysis value, String comment) {
        AnalysisSupportRequestFields fields =
                supportFactory.analysisSupportRequestFields().as();
        fields.setName(value.getName());
        fields.setApp(value.getAppName());
        fields.setOutputFolder(value.getResultFolderId());
        if(value.getStartDate()!= 0) {
            fields.setStartDate(new Date(value.getStartDate()).toString());
        } else {
            fields.setStartDate("");
        }
        if(value.getEndDate()!=0) {
            fields.setEndDate(new Date(value.getEndDate()).toString());
        } else {
            fields.setEndDate("");
        }
        fields.setComment(comment);
        fields.setStatus(value.getStatus());
        fields.setEmail(userInfo.getEmail());
        fields.setAnalysisId(value.getId());

        AnalysisSupportRequest req = supportFactory.analysisSupportRequest().as();
        req.setFrom(userInfo.getFullUsername());
        req.setSubject(
                userInfo.getUsername() +" " + appearance.userRequestingHelpSubject());
        req.setFields(fields);
        return req;
    }

    protected void emailSupport(final Splittable parent,
                                ReactSuccessCallback callback,
                                ReactErrorCallback errorCallback) {
        supportServiceProvider.get(new AsyncCallback<DEUserSupportServiceFacade>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(DEUserSupportServiceFacade serviceFacade) {
                serviceFacade.submitSupportRequest(parent, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.supportRequestFailed()));
                        errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        announcer.schedule(new SuccessAnnouncementConfig(appearance.supportRequestSuccess()));
                        callback.onSuccess(null);
                    }
                });
            }
        });
    }

    private void shareWithSupport(Analysis selectedAnalysis,
                                  final Splittable parent,
                                  ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {
        AnalysisPermission ap = shareFactory.analysisPermission().as();
        ap.setId(selectedAnalysis.getId());
        ap.setPermission(PermissionValue.read.toString());
        AnalysisSharingRequest asr = shareFactory.AnalysisSharingRequest().as();
        SharingSubject sharingSubject = shareFactory.getSharingSubject().as();
        sharingSubject.setSourceId("ldap");
        sharingSubject.setId(deProperties.getSupportUser());
        asr.setSubject(sharingSubject);
        asr.setAnalysisPermissions(Arrays.asList(ap));
        AnalysisSharingRequestList listRequest = shareFactory.AnalysisSharingRequestList().as();
        listRequest.setAnalysisSharingRequestList(Arrays.asList(asr));
        analysisService.shareAnalyses(listRequest, new AnalysisCallback<String>() {
            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                ErrorHandler.post(exception);
                errorCallback.onError(statusCode, exception.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                emailSupport(parent, callback, errorCallback);
            }
        });
    }
}

