package org.iplantc.de.apps.client.presenter.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.apps.client.gin.factory.AppDetailsViewFactory;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.apps.AppFeedback;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AppsCallback;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author jstroot
 */
public class AppDetailsViewPresenterImpl implements AppDetailsView.Presenter {

    @Inject AppUserServiceFacade appUserService;
    @Inject Provider<AppDetailsViewFactory> viewFactoryProvider;
    @Inject IplantAnnouncer announcer;
    @Inject AppDetailsView.AppDetailsAppearance appearance;
    @Inject EventBus eventBus;
    @Inject
    AppAutoBeanFactory appAutoBeanFactory;

    HandlerManager handlerManager;

    private AppDetailsView view;
    private OntologyHierarchiesView.OntologyHierarchiesAppearance ontologyHierarchiesAppearance;


    class AppRateCallback extends RateAppCallback {

        private final ReactSuccessCallback callback;
        private final ReactErrorCallback errorCallback;

        AppRateCallback(App app,
                        EventBus eventBus,
                        ReactSuccessCallback callback,
                        ReactErrorCallback errorCallback) {
            super(app, eventBus);
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Integer statusCode,
                              Throwable exception) {
            super.onFailure(statusCode, exception);
            if (errorCallback != null) {
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
            }

        }

        @Override
        public void onSuccess(AppFeedback result) {
            super.onSuccess(result);
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    class AppDeleteRateCallback extends DeleteRatingCallback {
        private final ReactSuccessCallback callback;
        private final ReactErrorCallback errorCallback;

        AppDeleteRateCallback(App app,
                              EventBus eventBus,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback) {
            super(app, eventBus);
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Integer statusCode,
                              Throwable exception) {
            super.onFailure(statusCode, exception);
            if (errorCallback != null) {
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
            }

        }

        @Override
        public void onSuccess(AppFeedback result) {
            super.onSuccess(result);
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }


    @Inject
    AppDetailsViewPresenterImpl(OntologyHierarchiesView.OntologyHierarchiesAppearance ontologyHierarchiesAppearance) {
        this.ontologyHierarchiesAppearance = ontologyHierarchiesAppearance;
    }


    @Override
    public void go(final App app,
                   final String searchRegexPattern,
                   TreeStore<OntologyHierarchy> hierarchyTreeStore,
                   TreeStore<AppCategory> categoryTreeStore) {
        Preconditions.checkState(view == null, "Cannot call go(..) more than once");

        view = viewFactoryProvider.get().create(app, searchRegexPattern, hierarchyTreeStore, categoryTreeStore);
        view.addSaveMarkdownSelectedHandler(AppDetailsViewPresenterImpl.this);

        eventBus.addHandler(AppUpdatedEvent.TYPE, view);

        // If the App has a wiki url, return before fetching app doc.
        if (Strings.isNullOrEmpty(app.getWikiUrl())) {

        }
        view.load(this);
    }

    @Override
    public void getAppDoc(Splittable appSplittable,
                          ReactSuccessCallback callback,
                          ReactErrorCallback errorCallback) {

        App app = AutoBeanCodex.decode(appAutoBeanFactory, App.class, appSplittable).as();

        appUserService.getAppDoc(app, new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode,
                                  Throwable caught) {
                // warn only for public app
                if (app.isPublic()) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.getAppDocError(caught)));
                }
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(final Splittable result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void onAppFavoriteSelected(Splittable appSplittable,
                                      ReactSuccessCallback callback,
                                      ReactErrorCallback errorCallback) {
        App app = AutoBeanCodex.decode(appAutoBeanFactory, App.class, appSplittable).as();

        appUserService.favoriteApp(app, !app.isFavorite(), new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode,
                                  Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(ontologyHierarchiesAppearance.favServiceFailure()));
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Void result) {
                if (callback != null) {
                    callback.onSuccess(null);
                }

                app.setFavorite(!app.isFavorite());
                eventBus.fireEvent(new AppFavoritedEvent(app));
                eventBus.fireEvent(new AppUpdatedEvent(app));
            }
        });
    }

    @Override
    public void onAppRatingSelected(Splittable appSplittable,
                                    int score,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback) {
        App app = AutoBeanCodex.decode(appAutoBeanFactory, App.class, appSplittable).as();
        appUserService.rateApp(app, score, new AppRateCallback(app, eventBus, callback, errorCallback));
    }

    @Override
    public void onAppRatingDeSelected(Splittable appSplittable,
                                      ReactSuccessCallback callback,
                                      ReactErrorCallback errorCallback) {
        App app = AutoBeanCodex.decode(appAutoBeanFactory, App.class, appSplittable).as();
        appUserService.deleteRating(app,
                                    new AppDeleteRateCallback(app, eventBus, callback, errorCallback));
    }

    @Override
    public void onClose() {
        view.onClose();
    }

    @Override
    public void onSaveMarkdownSelected(final SaveMarkdownSelected event) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(event.getEditorContent()));

        appUserService.saveAppDoc(event.getApp(),
                                  event.getEditorContent(), new AppsCallback<AppDoc>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                event.getMaskable().unmask();
                announcer.schedule(new ErrorAnnouncementConfig(appearance.saveAppDocError(caught)));
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final AppDoc result) {
                event.getMaskable().unmask();
               // appDoc = result;
            }
        });
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppFavoriteSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {

        return ensureHandlers().addHandler(AppRatingDeselected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return ensureHandlers().addHandler(AppRatingSelected.TYPE, handler);
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }
}
