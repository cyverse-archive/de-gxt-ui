package org.iplantc.de.apps.client.presenter.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.RunAppEvent;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.RunAppSelected;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.dialogs.AgaveAuthPrompt;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.exceptions.HttpRedirectException;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jstroot
 */
public class AppsListPresenterImpl implements AppsListView.Presenter,
                                              AppUpdatedEvent.AppUpdatedEventHandler{

    private final DEProperties deProperties;
    private final EventBus eventBus;
    @Inject
    IplantAnnouncer announcer;
    @Inject
    AppServiceFacade appService;
    @Inject
    AppUserServiceFacade appUserService;
    @Inject
    AppsListView.AppsListAppearance appearance;
    @Inject
    AsyncProviderWrapper<CommentsDialog> commentsDialogProvider;
    @Inject
    AppMetadataServiceFacade metadataFacade;
    @Inject
    UserInfo userInfo;
    @Inject AppAutoBeanFactory factory;
    OntologyServiceFacade ontologyService;
    OntologyUtil ontologyUtil;
    HandlerManager handlerManager;
    AppsListView listView;
    String filter;
    String sortField = "name";
    String sortDir = "asc";
    private OntologyHierarchy selectedHierarchy;
    private AppCategory appCategory;
    private App desiredSelectedApp;
    private String activeView;
    private List<App> selectedApps = new ArrayList<App>();

    @Inject
    AppsListPresenterImpl(final EventBus eventBus,
                          OntologyServiceFacade ontologyService,
                          final DEProperties deProperties,
                          final AppsListView listView
                          ) {
        this.eventBus = eventBus;
        this.ontologyService = ontologyService;
        this.ontologyUtil = OntologyUtil.getInstance();
        this.deProperties = deProperties;
        this.listView = listView;

        activeView = AppsListView.TILE_VIEW;

        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
    }

    @Override
    public void go(HasOneWidget widget) {
        String baseId = DeModule.WindowIds.APPS_WINDOW + AppsModule.Ids.APPS_VIEW;
        widget.setWidget(listView);
        listView.load(this, activeView, sortField, sortDir, baseId);
    }

    @Override
    public void loadApps(Splittable apps) {
        listView.setApps(apps, false);
    }

    /**
     * Get current active view
     *
     * @return the active view
     */
    @Override
    public String getActiveView() {
        return activeView;
    }

    /**
     * Set new active view
     *
     * @param newView view to set as active
     */
    @Override
    public void setActiveView(String newView) {
        setTypeFilterPreferences();
        activeView = newView;
        listView.setViewType(newView);

    }

    private void setTypeFilterPreferences() {
        filter = AppTypeFilter.ALL.getFilterString();
        listView.setTypeFilter(filter);
        disableTypeFilterForHPC();
    }


    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppInfoSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(AppSelectionChangedEvent.TYPE, handler);
    }

    @Override
    public void onAppInfoSelected(Splittable app) {
        AppInfoSelectedEvent event = new AppInfoSelectedEvent(splittableToApp(app));
        fireEvent(event);
    }

    App splittableToApp(Splittable app) {
        return AutoBeanCodex.decode(factory, App.class, app).as();
    }

    @Override
    public void onAppSelectionChanged(Splittable splittableSelectedApps) {
        selectedApps.clear();
        for (int i = 0; i < splittableSelectedApps.size(); i++) {
            App app = splittableToApp(splittableSelectedApps.get(i));
            selectedApps.add(app);
        }
        AppSelectionChangedEvent event = new AppSelectionChangedEvent(selectedApps);
        fireEvent(event);
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        filter = AppTypeFilter.ALL.getFilterString();
        listView.setTypeFilter(filter);
        listView.disableTypeFilter(true);
        listView.setLoadingMask(true);
    }

    public App getDesiredSelectedApp() {
        return desiredSelectedApp;
    }

    public void setDesiredSelectedApp(final App desiredSelectedApp) {
        this.desiredSelectedApp = desiredSelectedApp;
    }

    @Override
    public App getSelectedApp() {
        return selectedApps.size() > 0 ? selectedApps.get(0) : null ;
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);
        listView.setHeading(Joiner.on(" >> ").join(event.getGroupHierarchy()));
        appCategory = event.getAppCategorySelection().iterator().next();

        disableTypeFilterForHPC();
        getAppsWithSelectedCategory();
    }

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChangedEvent event) {
        Group selectedCommunity = event.getCommunitySelection();
        Preconditions.checkNotNull(selectedCommunity);
        if (!selectedCommunity.getId().equals(CommunitiesView.COMMUNITIES_ROOT)) {
            getCommunityApps(selectedCommunity);
        } else {
            listView.setApps(null, false);
            listView.setHeading(Joiner.on(" >> ").join(event.getPath()));
            listView.setLoadingMask(true);
            getCommunityApps(selectedCommunity);
        }
    }

    protected void disableTypeFilterForHPC() {
        if (appCategory != null && appCategory.getId().equals(deProperties.getDefaultHpcCategoryId())) {
            filter = AppTypeFilter.AGAVE.getFilterString();
            listView.disableTypeFilter(true);
        } else {
            listView.disableTypeFilter(false);
            if (filter != null && filter.equals(AppTypeFilter.AGAVE.getFilterString())) {
                filter = AppTypeFilter.ALL.getFilterString();
            }
        }
        listView.setTypeFilter(filter);

    }

    void getCommunityApps(Group community) {
        listView.setLoadingMask(true);
        appService.getCommunityApps(community.getDisplayName(),
                                    filter,
                                    sortField,
                                    sortDir,
                                    new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                new AppListCallback().onFailure(statusCode, caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                new AppListCallback().onSuccess(result);
            }
        });
    }

    protected void getAppsWithSelectedCategory() {
        listView.setLoadingMask(true);
        appService.getAppsAsSplittable(appCategory, filter, sortField, sortDir, new AppListCallback());
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        listView.setLoadingMask(true);
        selectedHierarchy = event.getSelectedHierarchy();
        listView.setHeading(Joiner.on(" >> ").join(event.getPath()));
        listView.disableTypeFilter(false);
        getAppsWithSelectedHierarchy();
    }

    protected void getAppsWithSelectedHierarchy() {
        if (selectedHierarchy != null) {
            listView.setLoadingMask(true);
            Avu avu = ontologyUtil.convertHierarchyToAvu(selectedHierarchy);
            String iri = selectedHierarchy.getIri();
            if (ontologyUtil.isUnclassified(selectedHierarchy)) {
                ontologyService.getUnclassifiedAppsInCategory(ontologyUtil.getUnclassifiedParentIri(
                        selectedHierarchy), avu, filter, sortField, sortDir, new AppListCallback());
            } else {
                ontologyService.getAppsInCategory(iri,
                                                  avu,
                                                  filter,
                                                  sortField,
                                                  sortDir,
                                                  new AppListCallback());
            }
        }
    }

    @Override
    public void onAppCommentSelected(Splittable appSplittable) {
        final App app = splittableToApp(appSplittable);
        if (App.EXTERNAL_APP.equalsIgnoreCase(app.getAppType())) {
            return;
        }
        commentsDialogProvider.get(new AsyncCallback<CommentsDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(
                        "Something happened while trying to manage comments. Please try again or contact support for help."));
            }

            @Override
            public void onSuccess(CommentsDialog result) {
                result.show(app, app.getIntegratorEmail().equals(userInfo.getEmail()), metadataFacade);
            }
        });
    }

    @Override
    public void onAppFavoriteSelected(Splittable appSplittable) {
        final App app = splittableToApp(appSplittable);
        if (App.EXTERNAL_APP.equalsIgnoreCase(app.getAppType())) {
            return;
        }
        appUserService.favoriteApp(app, !app.isFavorite(), new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.favServiceFailure()));
            }

            @Override
            public void onSuccess(Void result) {
                app.setFavorite(!app.isFavorite());
                eventBus.fireEvent(new AppFavoritedEvent(app));
                eventBus.fireEvent(new AppUpdatedEvent(app));
            }
        });
    }

    @Override
    public void onAppNameSelected(Splittable appSplittable) {
        doRunApp(splittableToApp(appSplittable));
    }

    @Override
    public void onAppRatingDeselected(final Splittable appSplittable) {
        final App appToUnRate = splittableToApp(appSplittable);
        appUserService.deleteRating(appToUnRate, new DeleteRatingCallback(appToUnRate, eventBus));
    }

    @Override
    public void onAppRatingSelected(final Splittable appSplittable, int score) {
        final App appToRate = splittableToApp(appSplittable);
        appUserService.rateApp(appToRate, score, new RateAppCallback(appToRate, eventBus));
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        Splittable apps;
        AppListLoadResult results = event.getResults();
        String searchText = event.getSearchText();
        int total = event.getResults() != null ? event.getResults().getTotal() : 0;
        String heading = appearance.searchAppResultsHeader(searchText, total);
        if(results != null) {
            apps = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(results));
            listView.loadSearchResults(apps.get("apps"), event.getSearchPattern(), heading, false);
        } else {
            listView.loadSearchResults(null, event.getSearchPattern(), heading, false);
        }
    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
        refreshAppListing();
    }

    @Override
    public void onDeleteAppsSelected(final DeleteAppsSelected event) {
        appUserService.deleteAppsFromWorkspace(event.getAppsToBeDeleted(), new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(appearance.appRemoveFailure(), caught);
            }

            @Override
            public void onSuccess(Void result) {
                refreshAppListing();
            }
        });
    }

    @Override
    public void onRunAppSelected(RunAppSelected event) {
        doRunApp(event.getApp());
    }

    void doRunApp(final App app) {
        if (app.isRunnable()) {
            if (!app.isDisabled()) {
                eventBus.fireEvent(new RunAppEvent(app));
            }
        } else {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.appLaunchWithoutToolError()));
        }
    }

    @Override
    public void onSwapViewButtonClicked(SwapViewButtonClickedEvent event) {
        setTypeFilterPreferences();
        if (activeView.equals(AppsListView.GRID_VIEW)) {
            activeView = AppsListView.TILE_VIEW;
        } else {
            activeView = AppsListView.GRID_VIEW;
        }
        listView.setViewType(activeView);
    }

    @Override
    public void onRequestSort(String sortField,
                              String sortDir) {
        this.sortField = sortField;
        this.sortDir = sortDir;
        listView.setSortInfo(sortField, sortDir);
        refreshAppListing();
    }

    private void refreshAppListing() {
        if (selectedHierarchy != null) {
            getAppsWithSelectedHierarchy();
            return;
        }
        if (appCategory != null) {
            getAppsWithSelectedCategory();
        }
    }


    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    void postToErrorHandler(Throwable caught) {
        ErrorHandler.post(caught);
    }

    @Override
    public void onTypeFilterChanged(String filter) {
        this.filter = filter;
        listView.setTypeFilter(filter);
        refreshAppListing();
    }

    private class AppListCallback extends AppsCallback<Splittable> {
        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            if (caught instanceof HttpRedirectException) {
                final String uri = ((HttpRedirectException)caught).getLocation();
                AgaveAuthPrompt prompt = new AgaveAuthPrompt(uri);
                prompt.show();
                prompt.addDialogHideHandler(event -> {
                    if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                        listView.setHeading(appearance.agaveAuthRequiredTitle());
                    }
                });
            } else {
                postToErrorHandler(caught);
                listView.setHeading(appearance.appLoadError());
            }
            listView.setApps(null, false);
        }

        @Override
        public void onSuccess(final Splittable apps) {
            listView.setApps(apps.get("apps"), false);
        }
    }

}
