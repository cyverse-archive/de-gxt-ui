package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.dialogs.AgaveAuthPrompt;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.exceptions.HttpException;

import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the callbacks used by the {@link DesktopPresenterImpl} during DE initialization.
 * @author jstroot
 */
class InitializationCallbacks {
    static class BootstrapCallback implements AsyncCallback<String> {
        private UserSettings userSettings;
        private IplantAnnouncer announcer;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private Panel panel;
        private final UserInfo userInfo;
        private final DesktopPresenterImpl presenter;
        private final DEProperties deProps = DEProperties.getInstance();
        Logger LOG = Logger.getLogger(BootstrapCallback.class.getName());

        public BootstrapCallback(DesktopPresenterImpl presenter,
                                 Panel panel,
                                 UserInfo userInfo,
                                 UserSettings userSettings,
                                 IplantAnnouncer announcer,
                                 DesktopView.Presenter.DesktopPresenterAppearance appearance) {

            this.presenter = presenter;
            this.panel = panel;
            this.userInfo = userInfo;
            this.userSettings = userSettings;
            this.announcer = announcer;
            this.appearance = appearance;
        }

        @Override
        public void onFailure(Throwable caught) {
            LOG.log(Level.SEVERE, caught.getMessage());
            Integer statusCode = null;
            if (caught instanceof HttpException) {
                statusCode = ((HttpException)caught).getStatusCode();
            }
            presenter.onBootstrapError(statusCode);
        }

        @Override
        public void onSuccess(String result) {
            userInfo.init(result);

            if (userInfo.hasUserProfileError()) {
                presenter.onBootstrapError(null);
                return;
            }

            if (userInfo.isNewUser()) {
                ConfirmMessageBox box = getIntroConfirmation();
                box.addDialogHideHandler(new DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().toString().equalsIgnoreCase("yes")) {
                            LOG.fine("new user tour");
                            presenter.onIntroClick();
                        }

                    }
                });
                box.show();
            }
            checkUserPreferences(presenter, panel, userInfo, userSettings, announcer, appearance);
            if (userInfo.hasAgaveRedirect() && userSettings.isEnableHPCPrompt()) {
                AgaveAuthPrompt prompt = getAgavePrompt();
                prompt.show();
                presenter.stickWindowToTop(prompt);
            }
            IntercomFacade.login(userInfo.getUsername(),
                                 userInfo.getEmail(),
                                 deProps.getIntercomAppId(),
                                 deProps.getCompanyId(),
                                 deProps.getCompanyName());
        }

        ConfirmMessageBox getIntroConfirmation() {
            return new ConfirmMessageBox(SafeHtmlUtils.fromString(appearance.welcome()), SafeHtmlUtils.fromTrustedString(appearance.introWelcome()));
        }

        AgaveAuthPrompt getAgavePrompt() {
            return AgaveAuthPrompt.getInstance();
        }

    }

    static class GetInitialNotificationsCallback implements AsyncCallback<NotificationList> {
        private final DesktopView view;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private final IplantAnnouncer announcer;
        private final NotificationsCallback callback;
        private final ReactErrorCallback errorCallback;

        public GetInitialNotificationsCallback(final DesktopView view,
                                               final DesktopView.Presenter.DesktopPresenterAppearance appearance,
                                               final IplantAnnouncer announcer,
                                               final NotificationsCallback callback,
                                               final ReactErrorCallback errorCallback) {
            this.view = view;
            this.appearance = appearance;
            this.announcer = announcer;
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Throwable caught) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.fetchNotificationsError(),
                                                           true,
                                                           5000));
             if(errorCallback != null) {
                 errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
             }
        }

        @Override
        public void onSuccess(NotificationList result) {
            Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result));
            if(callback != null) {
                callback.onFetchNotifications(sp);
            }
        }

    }

    static class PropertyServiceCallback implements AsyncCallback<HashMap<String, String>> {
        private final DEProperties deProps;
        private final Provider<ErrorHandler> errorHandlerProvider;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private final Panel panel;
        private final DesktopPresenterImpl presenter;
        private final UserInfo userInfo;
        private final UserSessionServiceFacade userSessionService;
        private final UserSettings userSettings;
        private final IplantAnnouncer announcer;
        Logger LOG = Logger.getLogger(PropertyServiceCallback.class.getName());

        public PropertyServiceCallback(DEProperties deProperties,
                                       UserInfo userInfo,
                                       UserSettings userSettings,
                                       UserSessionServiceFacade userSessionService,
                                       Provider<ErrorHandler> errorHandlerProvider,
                                       DesktopView.Presenter.DesktopPresenterAppearance appearance,
                                       IplantAnnouncer announcer,
                                       Panel panel,
                                       DesktopPresenterImpl presenter) {
            this.deProps = deProperties;
            this.userInfo = userInfo;
            this.userSettings = userSettings;
            this.userSessionService = userSessionService;
            this.errorHandlerProvider = errorHandlerProvider;
            this.appearance = appearance;
            this.announcer = announcer;
            this.panel = panel;
            this.presenter = presenter;
        }

        @Override
        public void onFailure(Throwable caught) {
            LOG.log(Level.SEVERE, caught.getMessage());
            Integer statusCode = null;
            if (caught instanceof HttpException) {
                statusCode = ((HttpException)caught).getStatusCode();
            }
            presenter.onBootstrapError(statusCode);
        }

        @Override
        public void onSuccess(HashMap<String, String> result) {
            deProps.initialize(result);
            userSessionService.bootstrap(new BootstrapCallback(presenter,
                                                               panel,
                                                               userInfo,
                                                               userSettings,
                                                               announcer,
                                                               appearance));
        }
    }

    static void checkUserPreferences(DesktopPresenterImpl presenter,
                                     Panel panel,
                                     UserInfo userInfo,
                                     UserSettings userSettings,
                                     IplantAnnouncer announcer,
                                     DesktopView.Presenter.DesktopPresenterAppearance appearance) {
        if (userInfo.hasPreferencesError()) {
            if (!userInfo.isNewUser()) {
                announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString(appearance.userPreferencesLoadError()),
                                                               false,
                                                               5000));
            }
            userSettings.setUserSettings(null);
        } else {
            userSettings.setUserSettings(userInfo.getUserPreferences());
        }
        presenter.postBootstrap(panel);
    }
}
