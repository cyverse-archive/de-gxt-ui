package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 3/2/15.
 * @author jstroot
 */
@JsType
public interface AppDetailsView extends IsWidget, AppUpdatedEvent.AppUpdatedEventHandler {

    @JsType
    interface AppDetailsAppearance {
        @JsIgnore
        SafeHtml getAppDocError(Throwable caught);
        @JsIgnore
        SafeHtml saveAppDocError(Throwable caught);

        String getQuickLaunchesError();
    }

    @JsType
    interface Presenter {

        @JsIgnore
        void go(App app,
                String searchRegexPattern);

        @SuppressWarnings("unusable-by-js")
        void onAppFavoriteSelected(Splittable app,
                                   ReactSuccessCallback callback,
                                   ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void onAppRatingSelected(Splittable app,
                                 int score,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void onAppRatingDeSelected(Splittable app,
                                   ReactSuccessCallback callback,
                                   ReactErrorCallback errorCallback);

        void onClose();

        @SuppressWarnings("unusable-by-js")
        void getAppDoc(Splittable appSplittable,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback);

        void onSaveMarkdownSelected(String appId,
                                    String systemId,
                                    String doc,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback);

        void getQuickLaunches(String appId,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback);

        
        void deleteQuickLaunch(String quickLaunchId,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback);

        void useQuickLaunch(String quickLaunchId,
                                      String appId);

        void onRequestToCreateQuickLaunch(String appId);

    }

    void load(Presenter presenter);

    void onClose();
}


