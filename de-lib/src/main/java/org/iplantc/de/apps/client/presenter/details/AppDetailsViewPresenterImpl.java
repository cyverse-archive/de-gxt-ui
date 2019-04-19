package org.iplantc.de.apps.client.presenter.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.QuickLaunchEvent;
import org.iplantc.de.apps.client.gin.factory.AppDetailsViewFactory;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppFeedback;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.QuickLaunchServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.shared.AppsCallback;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author jstroot
 */
public class AppDetailsViewPresenterImpl implements AppDetailsView.Presenter {

    @Inject AppUserServiceFacade appUserService;
    @Inject
    QuickLaunchServiceFacade qlService;
    @Inject Provider<AppDetailsViewFactory> viewFactoryProvider;
    @Inject IplantAnnouncer announcer;
    @Inject AppDetailsView.AppDetailsAppearance appearance;
    @Inject EventBus eventBus;
    @Inject
    AppAutoBeanFactory appAutoBeanFactory;
    @Inject private IplantValidationConstants valConstants;

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
                   final String searchRegexPattern) {
        Preconditions.checkState(view == null, "Cannot call go(..) more than once");

        view = viewFactoryProvider.get().create(app, searchRegexPattern);
        eventBus.addHandler(AppUpdatedEvent.TYPE, view);
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
    public void getQuickLaunches(String appId,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback) {
        qlService.listQuickLaunches(appId, new AppsCallback<Splittable>() {

            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                ErrorHandler.postReact(exception.getMessage(), exception);
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, exception.getMessage());
                }
            }

            @Override
            public void onSuccess(Splittable result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });

    }

    @Override
    public void deleteQuickLaunch(String quickLaunchId,
                                  ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {
        qlService.deleteQuickLaunch(quickLaunchId, new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode,
                                  Throwable exception) {
                ErrorHandler.postReact(exception.getMessage(), exception);
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, exception.getMessage());
                }
            }

            @Override
            public void onSuccess(Splittable result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void getAppInfoForQuickLaunch(String quickLaunchId,
                                         String appId) {
        eventBus.fireEvent(new QuickLaunchEvent(quickLaunchId, appId));
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
    public void onSaveMarkdownSelected(String appId,
                                       String systemId,
                                       String doc,
                                       ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback) {
        appUserService.saveAppDoc(appId, systemId, doc, new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.saveAppDocError(caught)));
                ErrorHandler.post(caught);
                if(errorCallback !=null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(final Splittable result) {
               if(callback != null) {
                   callback.onSuccess(null);
               }
            }
        });
    }
}
