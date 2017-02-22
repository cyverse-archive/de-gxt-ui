package org.iplantc.de.analysis.client.presenter;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.AnalysisToolBarView;
import org.iplantc.de.analysis.client.events.AnalysisCommentUpdate;
import org.iplantc.de.analysis.client.events.AnalysisFilterChanged;
import org.iplantc.de.analysis.client.events.HTAnalysisExpandEvent;
import org.iplantc.de.analysis.client.events.OpenAppForRelaunchEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisAppSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.AnalysisNameSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisUserSupportRequestedEvent;
import org.iplantc.de.analysis.client.events.selection.RefreshAnalysesSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.gin.factory.AnalysesViewFactory;
import org.iplantc.de.analysis.client.models.AnalysisFilter;
import org.iplantc.de.analysis.client.presenter.proxy.AnalysisRpcProxy;
import org.iplantc.de.analysis.client.presenter.sharing.AnalysisSharingPresenter;
import org.iplantc.de.analysis.client.views.AnalysisStepsView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisStepsInfoDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisUserSupportDialog;
import org.iplantc.de.analysis.client.views.sharing.AnalysisSharingViewImpl;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.models.analysis.sharing.AnalysisPermission;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingAutoBeanFactory;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequest;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequestList;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportAutoBeanFactory;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequest;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequestFields;
import org.iplantc.de.client.models.diskResources.PermissionValue;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.DEUserSupportServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AnalysisCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

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
                                              AnalysisNameSelectedEvent.AnalysisNameSelectedEventHandler,
                                              AnalysisAppSelectedEvent.AnalysisAppSelectedEventHandler,
                                              HTAnalysisExpandEvent.HTAnalysisExpandEventHandler,
                                              AnalysisUserSupportRequestedEvent.AnalysisUserSupportRequestedEventHandler,
                                              AnalysisJobInfoSelected.AnalysisJobInfoSelectedHandler,
                                              AnalysisCommentUpdate.AnalysisCommentUpdateHandler,
                                              ShareAnalysisSelected.ShareAnalysisSelectedHandler,
                                              AnalysisFilterChanged.AnalysisFilterChangedHandler,
                                              RefreshAnalysesSelected.RefreshAnalysesSelectedHandler,
                                              RenameAnalysisSelected.RenameAnalysisSelectedHandler {

    private final class CancelAnalysisServiceCallback extends AnalysisCallback<String> {
        private final Analysis ae;

        public CancelAnalysisServiceCallback(final Analysis ae) {
            this.ae = ae;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            /*
             * JDS Send generic error message. In the future, the "error_code" string should be parsed
             * from the JSON to provide more detailed user feedback.
             */
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.stopAnalysisError(ae.getName()));
            announcer.schedule(new ErrorAnnouncementConfig(msg, true, 3000));
        }

        @Override
        public void onSuccess(String result) {
            SafeHtml msg = SafeHtmlUtils.fromString(appearance.analysisStopSuccess(ae.getName()));
            announcer.schedule(new SuccessAnnouncementConfig(msg, true, 3000));
            loadAnalyses(currentFilter);
        }

    }

    /**
     * A LoadHandler needed to set selected analyses after the initial view load, since settings like
     * page size are only set in the reused config by the loader after an initial grid load, which may be
     * by-passed by the {@link org.iplantc.de.analysis.client.views.widget.AnalysisSearchField#filterByAnalysisId}
     * call in {@link AnalysesPresenterImpl#setSelectedAnalyses}.
     *
     * A benefit of selecting analyses with this LoadHandler is if the analysis to select has already
     * loaded when this handler is called, then it can be selected immediately without filtering.
     *
     * @author psarando
     */
    private class FirstLoadHandler
            implements LoadHandler<FilterPagingLoadConfig, PagingLoadResult<Analysis>> {

        private final List<Analysis> selectedAnalyses;

        public FirstLoadHandler(List<Analysis> selectedAnalyses) {
            this.selectedAnalyses = selectedAnalyses;
        }

        @Override
        public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<Analysis>> event) {
            handlerFirstLoad.removeHandler();
            setSelectedAnalyses(selectedAnalyses);
        }
    }

    private class RenameAnalysisCallback extends AnalysisCallback<Void> {

        private final Analysis selectedAnalysis;
        private final String newName;
        private final ListStore<Analysis> listStore;

        public RenameAnalysisCallback(Analysis selectedAnalysis,
                                      String newName,
                                      ListStore<Analysis> listStore) {
            this.selectedAnalysis = selectedAnalysis;
            this.newName = newName;
            this.listStore = listStore;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            final SafeHtml message = appearance.analysisRenameFailed();
            announcer.schedule(new ErrorAnnouncementConfig(message, true, 5000));
        }

        @Override
        public void onSuccess(Void result) {
            selectedAnalysis.setName(newName);
            listStore.update(selectedAnalysis);
            SafeHtml message = appearance.analysisRenameSuccess();
            announcer.schedule(new SuccessAnnouncementConfig(message, true, 5000));
        }
    }

    private class UpdateCommentsCallback extends AnalysisCallback<Void> {
        private final Analysis selectedAnalysis;
        private final String newComment;
        private final ListStore<Analysis> listStore;

        public UpdateCommentsCallback(Analysis selectedAnalysis,
                                      String newComment,
                                      ListStore<Analysis> listStore) {
            this.selectedAnalysis = selectedAnalysis;
            this.newComment = newComment;
            this.listStore = listStore;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            SafeHtml message = appearance.analysisCommentUpdateFailed();
            announcer.schedule(new ErrorAnnouncementConfig(message, true, 5000));
        }

        @Override
        public void onSuccess(Void result) {
            selectedAnalysis.setComments(newComment);
            listStore.update(selectedAnalysis);
            SafeHtml message = appearance.analysisCommentUpdateSuccess();
            announcer.schedule(new SuccessAnnouncementConfig(message, true, 5000));
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

    @Inject
    CollaboratorsUtil collaboratorsUtil;
    @Inject
    JsonUtil jsonUtil;
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

    private final ListStore<Analysis> listStore;
    private final AnalysesView view;
    private final HasHandlers eventBus;
    private HandlerRegistration handlerFirstLoad;
    private final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> loader;
    AnalysisFilter currentFilter;


    @Inject
    AnalysesPresenterImpl(final AnalysesViewFactory viewFactory,
                          final AnalysisRpcProxy proxy,
                          final ListStore<Analysis> listStore,
                          final EventBus eventBus) {
        this.listStore = listStore;
        this.eventBus = eventBus;
        loader = getPagingLoader(proxy);
        loader.useLoadConfig(new FilterPagingLoadConfigBean());
        loader.setRemoteSort(true);
        loader.setReuseLoadConfig(true);

        this.view = viewFactory.create(listStore, loader, this);
        AnalysisToolBarView toolBarView = view.getToolBarView();

        this.view.addAnalysisNameSelectedEventHandler(this);
        this.view.addAnalysisAppSelectedEventHandler(this);
        this.view.addHTAnalysisExpandEventHandler(this);
        this.view.addAnalysisUserSupportRequestedEventHandler(this);
        toolBarView.addAnalysisJobInfoSelectedHandler(this);
        this.view.addAnalysisCommentUpdateHandler(this);
        toolBarView.addAnalysisCommentUpdateHandler(this);
        toolBarView.addShareAnalysisSelectedHandler(this);
        toolBarView.addAnalysisFilterChangedHandler(this);
        toolBarView.addRefreshAnalysesSelectedHandler(this);
        toolBarView.addRenameAnalysisSelectedHandler(this);

        //Set default filter to ALL
        currentFilter = AnalysisFilter.ALL;
    }

    @Override
    public void onShareSupportSelected(List<Analysis> currentSelection, boolean shareWithInput) {

    }

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> getPagingLoader(AnalysisRpcProxy proxy) {
        return new PagingLoader<>(proxy);
    }

    @Override
    public void cancelSelectedAnalyses(final List<Analysis> analysesToCancel) {
        for (Analysis analysis : analysesToCancel) {
            analysisService.stopAnalysis(analysis, new CancelAnalysisServiceCallback(analysis));
        }
    }

    @Override
    public void deleteSelectedAnalyses(final List<Analysis> analysesToDelete) {
        analysisService.deleteAnalyses(analysesToDelete, new AnalysisCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(appearance.deleteAnalysisError(), caught);
            }

            @Override
            public void onSuccess(String arg0) {
                loadAnalyses(currentFilter);
            }
        });
    }

    @Override
    public List<Analysis> getSelectedAnalyses() {
        return view.getSelectedAnalyses();
    }

    @Override
    public void setSelectedAnalyses(final List<Analysis> selectedAnalyses) {
        if (selectedAnalyses == null || selectedAnalyses.isEmpty()) {
            return;
        }

        ArrayList<Analysis> selectNow = getNewAnalysisList();

        for (Analysis select : selectedAnalyses) {
            Analysis storeModel = listStore.findModel(select);
            if (storeModel != null) {
                selectNow.add(storeModel);
            }
        }

        if (selectNow.isEmpty()) {
            Analysis first = selectedAnalyses.get(0);
            view.filterByAnalysisId(first.getId(), first.getName());
        } else {
            view.setSelectedAnalyses(selectNow);
        }
    }

    ArrayList<Analysis> getNewAnalysisList() {
        return Lists.newArrayList();
    }

    @Override
    public void go(final HasOneWidget container, final List<Analysis> selectedAnalyses) {
        if (selectedAnalyses != null && !selectedAnalyses.isEmpty()) {
            handlerFirstLoad = loader.addLoadHandler(new FirstLoadHandler(selectedAnalyses));
        }
        loadAnalyses(AnalysisFilter.ALL);
        container.setWidget(view);
    }

    @Override
    public void loadAnalyses(AnalysisFilter filter) {
        if (!Strings.isNullOrEmpty(view.getSearchField().getCurrentValue())) {
            view.getSearchField().refreshSearch();
            return;
        }

        FilterPagingLoadConfig config = loader.getLastLoadConfig();
        config.getFilters().clear();

        FilterConfigBean idParentFilter = getFilterConfigBean();
        FilterConfigBean filterCb = getFilterConfigBean();

        idParentFilter.setField(AnalysisSearchField.PARENT_ID);
        filterCb.setField("ownership");

        if (filter != null) {
            idParentFilter.setValue("");
            switch (filter) {
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

        config.getFilters().add(idParentFilter);
        config.getFilters().add(filterCb);
        config.setLimit(200);
        config.setOffset(0);
        loader.load(config);
    }

    FilterConfigBean getFilterConfigBean() {
        return new FilterConfigBean();
    }

    @Override
    public void setFilterInView(AnalysisFilter filter) {
        view.setFilterInView(filter);
    }

    @Override
    public void goToSelectedAnalysisFolder(final Analysis selectedAnalysis) {
        // Request disk resource window
        eventBus.fireEvent(new OpenFolderEvent(selectedAnalysis.getResultFolderId(), true));
    }

    @Override
    public void onRefreshAnalysesSelected(RefreshAnalysesSelected event) {
        loadAnalyses(currentFilter);
    }

    @Override
    public void onShowAllSelected() {
        loadAnalyses(AnalysisFilter.ALL);
    }

    @Override
    public void onShareAnalysisSelected(ShareAnalysisSelected event) {
        List<Analysis> selected = event.getAnalysisList();

        AnalysisSharingViewImpl sharingView = new AnalysisSharingViewImpl();
        final AnalysisSharingPresenter sharingPresenter = new AnalysisSharingPresenter(analysisService,
                                                                                 selected,
                                                                                 sharingView,
                                                                                 collaboratorsUtil,
                                                                                 jsonUtil);
       aSharingDialogProvider.get(new AsyncCallback<AnalysisSharingDialog>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(AnalysisSharingDialog asd) {
                asd.setPresenter(sharingPresenter);
                asd.show();

            }
        });

    }

    @Override
    public void onAnalysisFilterChanged(AnalysisFilterChanged event) {
        AnalysisFilter filter = event.getFilter();
        if (filter == null) {
            currentFilter = filter;
            return;
        } else if (!(filter.equals(this.currentFilter))) {
            currentFilter = filter;
            loadAnalyses(currentFilter);
        }
    }

    @Override
    public AnalysisFilter getCurrentFilter() {
        return currentFilter;
    }

    @Override
    public void onAnalysisAppSelected(AnalysisAppSelectedEvent event) {
        eventBus.fireEvent(new OpenAppForRelaunchEvent(event.getAnalysis()));
    }

    @Override
    public void onAnalysisNameSelected(AnalysisNameSelectedEvent event) {
        // Request disk resource window
        eventBus.fireEvent(new OpenFolderEvent(event.getValue().getResultFolderId(), true));
    }

    @Override
    public void relaunchSelectedAnalysis(final Analysis selectedAnalysis) {
        if (selectedAnalysis.isAppDisabled()) {
            return;
        }
        eventBus.fireEvent(new OpenAppForRelaunchEvent(selectedAnalysis));
    }

    @Override
    public void onRenameAnalysisSelected(RenameAnalysisSelected event) {
        Analysis selectedAnalysis = event.getAnalysis();
        String newName = event.getNewName();
        analysisService.renameAnalysis(selectedAnalysis,
                                       newName,
                                       new RenameAnalysisCallback(selectedAnalysis, newName, listStore));

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void onAnalysisCommentUpdate(AnalysisCommentUpdate event) {
        Analysis value = event.getAnalysis();
        String comment = event.getComment();
        analysisService.updateAnalysisComments(value,
                                               comment,
                                               new UpdateCommentsCallback(value, comment, listStore));
    }

    @Override
    public void onHTAnalysisExpanded(HTAnalysisExpandEvent event) {
        view.filterByParentAnalysisId(event.getValue().getId());
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



    private void shareWithSupport(Analysis selectedAnalysis, final Splittable parent, final AnalysisUserSupportDialog ausd) {
        AnalysisPermission ap = shareFactory.analysisPermission().as();
        ap.setId(selectedAnalysis.getId());
        ap.setPermission(PermissionValue.read.toString());
        AnalysisSharingRequest asr = shareFactory.AnalysisSharingRequest().as();
        asr.setUser(deProperties.getSupportUser());
        asr.setAnalysisPermissions(Arrays.asList(ap));
        AnalysisSharingRequestList listRequest = shareFactory.AnalysisSharingRequestList().as();
        listRequest.setAnalysisSharingRequestList(Arrays.asList(asr));
        analysisService.shareAnalyses(listRequest, new AnalysisCallback<String>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.post(exception);
                ausd.unmask();
            }

            @Override
            public void onSuccess(String result) {
                emailSupport(parent, ausd);
            }
        });
    }

    @Override
    public void onUserSupportRequested(AnalysisUserSupportRequestedEvent event) {
        final Analysis value = event.getValue();
        aSupportDialogProvider.get(new AsyncCallback<AnalysisUserSupportDialog>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(final AnalysisUserSupportDialog ausd) {
                ausd.setHeading(value.getName());
                ausd.setSize("800px", "500px");
                ausd.addSubmitSelectHandler(new SelectEvent.SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        ausd.mask(appearance.requestProcessing());
                        AnalysisSupportRequest req = getAnalysisSupportRequest(value, ausd.getComment());
                        shareWithSupport(value, AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(req)), ausd);
                    }
                });
                ausd.renderHelp(value);
                ausd.show();
            }
        });


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

        AnalysisSupportRequest req = supportFactory.analysisSupportRequest().as();
        req.setFrom(userInfo.getFullUsername());
        req.setSubject(
                userInfo.getUsername() + appearance.userRequestingHelpSubject());
        req.setFields(fields);
        return req;
    }

    protected void emailSupport(final Splittable parent, final AnalysisUserSupportDialog ausd) {
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
                        ausd.unmask();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        announcer.schedule(new SuccessAnnouncementConfig(userSupportAppearance.supportRequestSuccess()));
                        ausd.unmask();
                        ausd.hide();
                    }
                });
            }
        });
    }
}
