package org.iplantc.de.apps.client.presenter.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppTypeFilterChangedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.RunAppEvent;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.RunAppSelected;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
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
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

import java.util.List;

/**
 * @author jstroot
 */
public class AppsListPresenterImpl implements AppsListView.Presenter,
                                              AppNameSelectedEvent.AppNameSelectedEventHandler,
                                              AppRatingSelected.AppRatingSelectedHandler,
                                              AppRatingDeselected.AppRatingDeselectedHandler,
                                              AppCommentSelectedEvent.AppCommentSelectedEventHandler,
                                              AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler,
                                              AppUpdatedEvent.AppUpdatedEventHandler,
                                              AppSelectionChangedEvent.AppSelectionChangedEventHandler,
                                              AppInfoSelectedEvent.AppInfoSelectedEventHandler,
                                              AppTypeFilterChangedEvent.AppTypeFilterChangedEventHandler {

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
    CardLayoutContainer cards;
    AppTypeFilter filter;
    private OntologyHierarchy selectedHierarchy;
    private AppCategory appCategory;
    private App desiredSelectedApp;
    private String activeView;

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

/*        this.gridView.addAppNameSelectedEventHandler(this);
        this.gridView.addAppRatingDeselectedHandler(this);
        this.gridView.addAppRatingSelectedHandler(this);
        this.gridView.addAppCommentSelectedEventHandlers(this);
        this.gridView.addAppFavoriteSelectedEventHandlers(this);
        this.gridView.addAppSelectionChangedEventHandler(this);
        this.gridView.addAppInfoSelectedEventHandler(this);

        this.tileView.addAppNameSelectedEventHandler(this);
        this.tileView.addAppRatingDeselectedHandler(this);
        this.tileView.addAppRatingSelectedHandler(this);
        this.tileView.addAppCommentSelectedEventHandlers(this);
        this.tileView.addAppFavoriteSelectedEventHandlers(this);
        this.tileView.addAppSelectionChangedEventHandler(this);
        this.tileView.addAppInfoSelectedEventHandler(this);*/

        activeView = AppsListView.TILE_VIEW;

        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
        eventBus.addHandler(AppTypeFilterChangedEvent.TYPE, this);
    }

    @Override
    public void go(HasOneWidget widget) {
        //by default support only gridView
        widget.setWidget(listView);
    }

    @Override
    public void loadApps(Splittable apps) {
        GWT.log("apps ==> " + apps.get("apps"));
  /*      if (getDesiredSelectedApp() != null) {

            activeView.select(getDesiredSelectedApp(), false);

        } else if (listStore.size() > 0) {
            // Select first app
            activeView.select(listStore.get(0), false);
        }
        setDesiredSelectedApp(null);
        activeView.unmask();*/
        listView.load(this,
                      apps.get("apps"),
                      "",
                      AppTypeFilter.ALL.getFilterString(),
                      "",
                      "",
                      true,
                      null,
                      activeView);
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
/*        activeView = tileView;
        cards.setActiveWidget(activeView);

        setTypeFilterPreferences();*/
        //  activeView = newView;
        //  listView.setViewType(newView);
        activeView = newView;
        listView.setViewType(newView);

    }

    private void setTypeFilterPreferences() {
        filter = AppTypeFilter.ALL;
   /*     activeView.setAppTypeFilter(filter);
        activeView.enableAppTypeFilter(true);*/
        disableTypeFilterForHPC();
    }

    @Override
    public void setViewDebugId(String baseID) {
        //listView.asWidget().ensureDebugId(baseID);
    }

/*
    @Override
    public HandlerRegistration addStoreAddHandler(StoreAddEvent.StoreAddHandler<App> handler) {
        return listStore.addStoreAddHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreClearHandler(StoreClearEvent.StoreClearHandler<App> handler) {
        return listStore.addStoreClearHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreRemoveHandler(StoreRemoveEvent.StoreRemoveHandler<App> handler) {
        return listStore.addStoreRemoveHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreUpdateHandler(StoreUpdateEvent.StoreUpdateHandler<App> handler) {
        return listStore.addStoreUpdateHandler(handler);
    }
*/

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppInfoSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(AppSelectionChangedEvent.TYPE, handler);
    }

    @Override
    public void onAppInfoSelected(AppInfoSelectedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppSelectionChanged(AppSelectionChangedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        filter = AppTypeFilter.ALL;
   /*     activeView.setAppTypeFilter(filter);
        activeView.enableAppTypeFilter(false);
        activeView.mask(appearance.beforeAppSearchLoadingMask());*/
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
        return null;
    }

    @Override
    public List<DragSource> getAppsDragSources() {
/*        List<DragSource> sources = Lists.newArrayList();
        sources.addAll(gridView.getAppsDragSources());
        sources.addAll(tileView.getAppsDragSources());
        return sources;*/
        return null;
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
     /*   tileView.onAppCategorySelectionChanged(event);
        gridView.onAppCategorySelectionChanged(event);

        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);*/
        appCategory = event.getAppCategorySelection().iterator().next();

        disableTypeFilterForHPC();
        getAppsWithSelectedCategory();
    }

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChangedEvent event) {
       /* tileView.onCommunitySelectionChanged(event);
        gridView.onCommunitySelectionChanged(event);

        Group selectedCommunity = event.getCommunitySelection();

        Preconditions.checkNotNull(selectedCommunity);
        if (!selectedCommunity.getId().equals(CommunitiesView.COMMUNITIES_ROOT)) {
            getCommunityApps(selectedCommunity);
        } else {
            loadApps(Lists.newArrayList());
        }*/
       listView.setLoadingMask(true);
        Group selectedCommunity = event.getCommunitySelection();
        if (!selectedCommunity.getId().equals(CommunitiesView.COMMUNITIES_ROOT)) {
            getCommunityApps(selectedCommunity);
        } else {
            listView.setApps(null);
        }
    }

    protected void disableTypeFilterForHPC() {
        /*if (appCategory != null && appCategory.getId().equals(deProperties.getDefaultHpcCategoryId())) {
            filter = AppTypeFilter.AGAVE;
            activeView.enableAppTypeFilter(false);
        } else {
            activeView.enableAppTypeFilter(true);
            if (filter != null && filter.equals(AppTypeFilter.AGAVE)) {
                filter = AppTypeFilter.ALL;
            }
        }
        activeView.setAppTypeFilter(filter);*/
    }

    void getCommunityApps(Group community) {
        listView.setLoadingMask(true);
        appService.getCommunityApps(community.getDisplayName(), filter, new AppsCallback<Splittable>() {
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
       // activeView.mask(appearance.getAppsLoadingMask());
        listView.setLoadingMask(true);
        appService.getAppsAsSplittable(appCategory, filter, new AppListCallback());
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
     /*   tileView.onOntologyHierarchySelectionChanged(event);
        gridView.onOntologyHierarchySelectionChanged(event);
*/
        selectedHierarchy = event.getSelectedHierarchy();
    //    activeView.enableAppTypeFilter(true);
        getAppsWithSelectedHierarchy();
    }

    protected void getAppsWithSelectedHierarchy() {
        if (selectedHierarchy != null) {
            listView.setLoadingMask(true);
            Avu avu = ontologyUtil.convertHierarchyToAvu(selectedHierarchy);
            String iri = selectedHierarchy.getIri();
            if (ontologyUtil.isUnclassified(selectedHierarchy)) {
                ontologyService.getUnclassifiedAppsInCategory(ontologyUtil.getUnclassifiedParentIri(
                        selectedHierarchy), avu, filter, new AppListCallback());
            } else {
                ontologyService.getAppsInCategory(iri, avu, filter, new AppListCallback());
            }
        }
    }

    @Override
    public void onAppCommentSelectedEvent(AppCommentSelectedEvent event) {
        final App app = event.getApp();
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
    public void onAppFavoriteSelected(AppFavoriteSelectedEvent event) {
        final App app = event.getApp();
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
    public void onAppNameSelected(AppNameSelectedEvent event) {
        doRunApp(event.getSelectedApp());
    }

    @Override
    public void onAppRatingDeselected(final AppRatingDeselected event) {
        final App appToUnRate = event.getApp();
        appUserService.deleteRating(appToUnRate, new DeleteRatingCallback(appToUnRate, eventBus));
    }

    @Override
    public void onAppRatingSelected(final AppRatingSelected event) {
        final App appToRate = event.getApp();
        appUserService.rateApp(appToRate, event.getScore(), new RateAppCallback(appToRate, eventBus));
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
/*        tileView.onAppSearchResultLoad(event);
        gridView.onAppSearchResultLoad(event);

        activeView.setSearchPattern(event.getSearchPattern());*/
        //    listStore.clear();
        //   listStore.addAll(event.getResults());
        listView.setLoadingMask(false);

    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
    /*    App app = event.getApp();
        if (listStore.findModel(app) != null) {
            listStore.update(app);
        }*/
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
                for (App app : event.getAppsToBeDeleted()) {
                    //listStore.remove(app);
                }
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
 /*     //  activeView.deselectAll();
        if (activeView == gridView) {
            activeView = tileView;
        } else {
            activeView = gridView;
        }
        cards.setActiveWidget(activeView);
        setTypeFilterPreferences();
 */
     if(activeView.equals(AppsListView.GRID_VIEW)) {
         activeView = AppsListView.TILE_VIEW;
     } else {
         activeView = AppsListView.GRID_VIEW;
     }
        listView.setViewType(activeView);
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
    public void onTypeFilterChanged(AppTypeFilterChangedEvent event) {
        this.filter = event.getFilter();
        if (selectedHierarchy != null) {
            getAppsWithSelectedHierarchy();
            return;
        }
        if (appCategory != null) {
            getAppsWithSelectedCategory();
        }

    }

    private class AppListCallback extends AppsCallback<Splittable> {
        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
/*            if (caught instanceof HttpRedirectException) {
                final String uri = ((HttpRedirectException)caught).getLocation();
                AgaveAuthPrompt prompt = new AgaveAuthPrompt(uri);
                prompt.show();
                prompt.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            listStore.clear();
                            gridView.setHeading(appearance.agaveAuthRequiredTitle());
                            tileView.setHeading(appearance.agaveAuthRequiredTitle());
                        }
                    }
                });
            } else {
                postToErrorHandler(caught);
                listStore.clear();
                gridView.setHeading(appearance.appLoadError());
                tileView.setHeading(appearance.appLoadError());
            }
            activeView.unmask();*/
        }

        @Override
        public void onSuccess(final Splittable apps) {
            loadApps(apps);
        }
    }

}
