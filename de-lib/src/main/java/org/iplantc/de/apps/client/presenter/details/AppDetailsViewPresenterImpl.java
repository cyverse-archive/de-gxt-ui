package org.iplantc.de.apps.client.presenter.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.apps.client.gin.factory.AppDetailsViewFactory;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AppsCallback;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
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
    private AppDoc appDoc;

    @Inject
    AppDetailsViewPresenterImpl() {
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
            // Prefetch Docs
            appUserService.getAppDoc(app, new AppsCallback<AppDoc>() {
                @Override
                public void onFailure(Integer statusCode,
                                      Throwable caught) {
                    // warn only for public app
                    if (app.isPublic()) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.getAppDocError(caught)));
                    }
                }

                @Override
                public void onSuccess(final AppDoc result) {
                    appDoc = result;
                }
            });
        }
        view.load(this);
    }

    @Override
    public void onAppFavoriteSelected(Splittable app) {
        fireEvent(new AppFavoriteSelectedEvent(AutoBeanCodex.decode(appAutoBeanFactory, App.class, app)
                                                            .as()));
    }

    @Override
    public void onAppRatingSelected(Splittable app, int score) {
        fireEvent(new AppRatingSelected(AutoBeanCodex.decode(appAutoBeanFactory, App.class, app).as(),
                                        score));
    }

    @Override
    public void onAppRatingDeSelected(Splittable app) {
        fireEvent(new AppRatingDeselected(AutoBeanCodex.decode(appAutoBeanFactory, App.class, app)
                                                            .as()));
    }

    @Override
    public void onClose() {
        view.onClose();
    }

    @Override
    public void onAppDetailsDocSelected() {
            Preconditions.checkNotNull(appDoc, "AppDoc should have been pre-fetched in go(..) method!");
            view.showDoc(appDoc);
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
                appDoc = result;
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
