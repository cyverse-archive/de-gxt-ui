package org.iplantc.de.analysis.client.presenter;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.events.AnalysisFilterChanged;
import org.iplantc.de.analysis.client.events.HTAnalysisExpandEvent;
import org.iplantc.de.analysis.client.events.InteractiveIconClicked;
import org.iplantc.de.analysis.client.events.OpenAppForRelaunchEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.CompleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ViewAnalysisParamsSelected;
import org.iplantc.de.analysis.client.models.FilterAutoBeanFactory;
import org.iplantc.de.analysis.client.models.FilterBeanList;
import org.iplantc.de.analysis.client.views.AnalysisStepsView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisCommentsDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisParametersDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisStepsInfoDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisUserSupportDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.AnalysesList;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisPermissionFilter;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
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
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.shared.AnalysisCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A presenter for analyses view
 *
 * @author sriram, jstroot
 */
public class AnalysesPresenterImpl implements AnalysesView.Presenter,
                                              HTAnalysisExpandEvent.HTAnalysisExpandEventHandler,
                                              AnalysisJobInfoSelected.AnalysisJobInfoSelectedHandler,
                                              AnalysisFilterChanged.AnalysisFilterChangedHandler,
                                              CompleteAnalysisSelected.CompleteAnalysisSelectedHandler,
                                              ViewAnalysisParamsSelected.ViewAnalysisParamsSelectedHandler,
                                              InteractiveIconClicked.InteractiveIconClickedHandler {

    @Override
    public void onCancelAnalysisSelected(Splittable[] analysisList,
                                         ReactSuccessCallback callback,
                                         ReactErrorCallback errorCallback) {


        for (Splittable sp : analysisList) {
            Analysis analysis = AutoBeanCodex.decode(factory, Analysis.class, sp).as();
            analysisService.stopAnalysis(analysis,
                                         new CancelAnalysisServiceCallback(analysis,
                                                                           callback,
                                                                           errorCallback),
                                         "Canceled");
        }
    }

    private final class CompleteAnalysisServiceCallback extends AnalysisCallback<String> {
        private final Analysis ae;

        public CompleteAnalysisServiceCallback(final Analysis ae) {
            this.ae = ae;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.stopAnalysisError(ae.getName()));
            announcer.schedule(new ErrorAnnouncementConfig(msg, true, 3000));
        }

        @Override
        public void onSuccess(String result) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.analysisStopSuccess(ae.getName()));
            announcer.schedule(new SuccessAnnouncementConfig(msg, true, 3000));
            loadAnalyses(currentPermFilter, currentTypeFilter);
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
    AnalysisStepsView analysisStepView;
    @Inject
    AsyncProviderWrapper<AnalysisSharingDialog> aSharingDialogProvider;
    @Inject
    AsyncProviderWrapper<AnalysisUserSupportDialog> aSupportDialogProvider;
    @Inject AsyncProviderWrapper<AnalysisStepsInfoDialog> stepsInfoDialogProvider;
    @Inject AsyncProviderWrapper<AnalysisCommentsDialog> analysisCommentsDlgProvider;
    @Inject AsyncProviderWrapper<AnalysisParametersDialog> analysisParametersDialogAsyncProvider;

    @Inject
    AnalysisUserSupportDialog.AnalysisUserSupportAppearance userSupportAppearance;
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

    private final HasHandlers eventBus;
    AnalysisPermissionFilter currentPermFilter;
    AppTypeFilter currentTypeFilter;

    @Inject
    AnalysesAutoBeanFactory factory;


    @Inject
    AnalysesPresenterImpl(final EventBus eventBus) {
        this.eventBus = eventBus;

        //Set default filter to ALL
        currentPermFilter = AnalysisPermissionFilter.ALL;
        currentTypeFilter = AppTypeFilter.ALL;
    }

    @Override
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
    public void onShareSupportSelected(List<Analysis> currentSelection, boolean shareWithInput) {

    }

    @Override
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
    public void onCompleteAnalysisSelected(CompleteAnalysisSelected event) {
        List<Analysis> analysesToComplete = event.getAnalysisList();

        for (Analysis analysis : analysesToComplete) {
            analysisService.stopAnalysis(analysis, new CompleteAnalysisServiceCallback(analysis), "Completed");
        }
    }

    @Override
    public void deleteAnalyses(Splittable[] analysesToDelete,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback) {
        ArrayList<Analysis> selected = new ArrayList<>();
        for (Splittable sp : analysesToDelete) {
            Analysis analysis = AutoBeanCodex.decode(factory, Analysis.class, sp).as();
            selected.add(analysis);
        }
        analysisService.deleteAnalyses(selected, new AnalysisCallback<String>() {

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

    @Override
    public List<Analysis> getSelectedAnalyses() {
        //return view.getSelectedAnalyses();
        return new ArrayList<>();
    }

    @Override
    public void setSelectedAnalyses(final List<Analysis> selectedAnalyses) {
/*        if (selectedAnalyses == null || selectedAnalyses.isEmpty()) {
            return;
        }

        ArrayList<Analysis> selectNow = getNewAnalysisList();

        for (Analysis select : selectedAnalyses) {
            Analysis storeModel = listStore.findModel(select);
            if (storeModel != null) {
                selectNow.add(storeModel);
            }
        }*/

/*        if (selectNow.isEmpty()) {
            Analysis first = selectedAnalyses.get(0);
            view.filterByAnalysisId(first.getId(), first.getName());
        } else {
            view.setSelectedAnalyses(selectNow);
        }*/
    }

    ArrayList<Analysis> getNewAnalysisList() {
        return Lists.newArrayList();
    }

    @Override
    public void go(final HasOneWidget container, final List<Analysis> selectedAnalyses) {
/*        if (selectedAnalyses != null && !selectedAnalyses.isEmpty()) {
            handlerFirstLoad = loader.addLoadHandler(new FirstLoadHandler(selectedAnalyses));
        }
        loadAnalyses(AnalysisPermissionFilter.ALL, AppTypeFilter.ALL);*/
        view.setPresenter(this);
        container.setWidget(view);
        view.load();
    }

    @Override
    public void loadAnalyses(AnalysisPermissionFilter permFilter, AppTypeFilter typeFilter) {
       /* if (!Strings.isNullOrEmpty(view.getSearchField().getCurrentValue())) {
            view.getSearchField().refreshSearch();
            return;
        }

        FilterPagingLoadConfig config = loader.getLastLoadConfig();
        config.getFilters().clear();

        FilterConfigBean idParentFilter = getFilterConfigBean();
        FilterConfigBean filterCb = getFilterConfigBean();

        idParentFilter.setField(AnalysisSearchField.PARENT_ID);
        filterCb.setField("ownership");

        if (permFilter != null) {
            idParentFilter.setValue("");
            switch (permFilter) {
                case ALL:
                    filterCb.setValue("all");
                    break;
                case SHARED_WITH_ME:
                    filterCb.setValue("theirs");
                    break;
                case MY_ANALYSES:
                    filterCb.setValue("mine");
                    break;
            }
        } else {
            idParentFilter.setValue(view.getParentAnalysisId());
        }

        FilterConfigBean typeFilterCb = getFilterConfigBean();
        typeFilterCb.setField("type");

        if (typeFilter != null) {
            switch (typeFilter) {
                case DE:
                    typeFilterCb.setValue("DE");
                    break;
                case OSG:
                    typeFilterCb.setValue("OSG");
                    break;
                case AGAVE:
                    typeFilterCb.setValue("Agave");
                    break;
                case INTERACTIVE:
                    typeFilterCb.setValue("Interactive");
                    break;
                case ALL:
                default:
                    typeFilterCb.setValue(null);
                    break;
            }
            config.getFilters().add(typeFilterCb);
        }

        config.getFilters().add(idParentFilter);
        config.getFilters().add(filterCb);

        config.setLimit(200);
        config.setOffset(0);
        loader.load(config);
        */
    }

    FilterConfigBean getFilterConfigBean() {
        return new FilterConfigBean();
    }

    @Override
    public void setFilterInView(AnalysisPermissionFilter permFilter, AppTypeFilter typeFilter) {
        //  view.setPermFilterInView(permFilter, typeFilter);
    }

    @Override
    public void onShowAllSelected() {
        loadAnalyses(AnalysisPermissionFilter.ALL, AppTypeFilter.ALL);
    }

    private final class CancelAnalysisServiceCallback extends AnalysisCallback<String> {
        private final Analysis ae;
        private final ReactSuccessCallback callback;
        private final ReactErrorCallback errorCallback;

        public CancelAnalysisServiceCallback(final Analysis ae,
                                             final ReactSuccessCallback callback,
                                             final ReactErrorCallback errorCallback) {
            this.ae = ae;
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            /*
             * JDS Send generic error message. In the future, the "error_code" string should be parsed
             * from the JSON to provide more detailed user feedback.
             */
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.stopAnalysisError(ae.getName()));
            announcer.schedule(new ErrorAnnouncementConfig(msg, true, 3000));
            if (errorCallback != null) {
                errorCallback.onError(statusCode, caught.getMessage());
            }
        }

        @Override
        public void onSuccess(String result) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.analysisStopSuccess(ae.getName()));
            announcer.schedule(new SuccessAnnouncementConfig(msg, true, 3000));
            if (callback != null) {
                callback.onSuccess(null);
            }
        }

    }

    @Override
    public void onAnalysisFilterChanged(AnalysisFilterChanged event) {
        AnalysisPermissionFilter permFilter = event.getPermFilter();
        AppTypeFilter typeFilter = event.getTypeFilter();
        boolean filterChanged = false;

        if (permFilter == null && typeFilter == null) {
            currentPermFilter = permFilter;
            currentTypeFilter = typeFilter;
            return;
        }
        if (permFilter!= null && !(permFilter.equals(this.currentPermFilter))) {
            currentPermFilter = permFilter;
            filterChanged = true;
        }

        if (typeFilter != null && !(typeFilter.equals(this.currentTypeFilter))) {
            currentTypeFilter = typeFilter;
            filterChanged = true;
        }
        if(filterChanged) {
            loadAnalyses(currentPermFilter, currentTypeFilter);
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
    public void onAnalysisAppSelected(Splittable analysis) {
        Analysis relaunchAnalysis = AutoBeanCodex.decode(factory,Analysis.class,analysis).as();
        eventBus.fireEvent(new OpenAppForRelaunchEvent(relaunchAnalysis));
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
        analysisService.renameAnalysis(analysisId, newName, new AnalysisCallback() {

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
            public void onSuccess(Object result) {
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
        analysisService.updateAnalysisComments(id, comment, new AnalysisCallback() {

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
            public void onSuccess(Object result) {
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
    public void onHTAnalysisExpanded(HTAnalysisExpandEvent event) {
        //view.filterByParentAnalysisId(event.getValue().getId());
    }

    @Override
    public void onAnalysisJobInfoSelected(AnalysisJobInfoSelected event) {
        analysisService.getAnalysisSteps(event.getAnalysis(), new AnalysisCallback<AnalysisStepsInfo>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                IplantAnnouncer.getInstance()
                               .schedule(new ErrorAnnouncementConfig(appearance.analysisStepInfoError()));

            }

            @Override
            public void onSuccess(AnalysisStepsInfo stepsInfo) {
                stepsInfoDialogProvider.get(new AsyncCallback<AnalysisStepsInfoDialog>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(AnalysisStepsInfoDialog result) {
                        result.show(stepsInfo);
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
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
                errorCallback.onError(statusCode, exception.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                emailSupport(parent, callback, errorCallback);
            }
        });
    }

    @Override
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
                        announcer.schedule(new ErrorAnnouncementConfig(userSupportAppearance.supportRequestFailed()));
                        errorCallback.onError(HttpStatus.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        announcer.schedule(new SuccessAnnouncementConfig(userSupportAppearance.supportRequestSuccess()));
                        callback.onSuccess(null);
                    }
                });
            }
        });
    }

    @Override
    public void onViewAnalysisParamsSelected(ViewAnalysisParamsSelected event) {
        Analysis selectedAnalysis = event.getAnalysis();
        analysisParametersDialogAsyncProvider.get(new AsyncCallback<AnalysisParametersDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(AnalysisParametersDialog result) {
                result.show(selectedAnalysis);
            }
        });
    }

    ConfirmMessageBox getDeleteAnalysisDlg() {
        return new ConfirmMessageBox(appearance.warning(), appearance.analysesExecDeleteWarning());
    }

    IPlantPromptDialog getRenameAnalysisDlg(String name) {
        return new IPlantPromptDialog(appearance.rename(), -1, name, new DiskResourceNameValidator());
    }

    @Override
    public void onInteractiveIconClicked(InteractiveIconClicked event) {
        Analysis analysis = event.getAnalysis();
        List<String> interactiveUrls = analysis.getInteractiveUrls();

        if (interactiveUrls != null && !interactiveUrls.isEmpty()) {
            //For now, assume only one URL is returned since we don't currently
            //allow batch jobs or workflows with VICE apps
            Window.open(interactiveUrls.get(0), "_blank", "");
        }
    }
}
