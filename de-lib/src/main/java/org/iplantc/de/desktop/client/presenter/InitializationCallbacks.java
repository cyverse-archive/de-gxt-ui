package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.dialogs.AgaveAuthPrompt;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.exceptions.HttpException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.sencha.gxt.data.shared.ListStore;
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
        private final Provider<ErrorHandler> errorHandlerProvider;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private final UserInfo userInfo;
        private final DesktopPresenterImpl presenter;
        private final UserSessionServiceFacade userSessionService;
        private final UserPreferencesCallback userPreferencesCallback;
        Logger LOG = Logger.getLogger(BootstrapCallback.class.getName());

        public BootstrapCallback(DesktopPresenterImpl presenter,
                                 UserInfo userInfo,
                                 Provider<ErrorHandler> errorHandlerProvider,
                                 DesktopView.Presenter.DesktopPresenterAppearance appearance,
                                 UserSessionServiceFacade userSessionService,
                                 UserPreferencesCallback userPreferencesCallback) {

            this.presenter = presenter;
            this.userInfo = userInfo;
            this.errorHandlerProvider = errorHandlerProvider;
            this.appearance = appearance;
            this.userSessionService = userSessionService;
            this.userPreferencesCallback = userPreferencesCallback;
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
            } else {
                if (userInfo.hasAgaveRedirect()) {
                    AgaveAuthPrompt prompt = getAgavePrompt();
                    prompt.show();
                    presenter.stickWindowToTop(prompt);
                }
            }
            userSessionService.getUserPreferences(userPreferencesCallback);
        }

        ConfirmMessageBox getIntroConfirmation() {
            return new ConfirmMessageBox(appearance.welcome(), appearance.introWelcome());
        }

        AgaveAuthPrompt getAgavePrompt() {
            return AgaveAuthPrompt.getInstance();
        }

    }

    static class GetInitialNotificationsCallback implements AsyncCallback<NotificationList> {
        private final DesktopView view;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private final IplantAnnouncer announcer;

        public GetInitialNotificationsCallback(final DesktopView view,
                                               final DesktopView.Presenter.DesktopPresenterAppearance appearance,
                                               final IplantAnnouncer announcer) {
            this.view = view;
            this.appearance = appearance;
            this.announcer = announcer;
        }

        @Override
        public void onFailure(Throwable caught) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.fetchNotificationsError(),
                                                           true,
                                                           5000));
            view.setNotificationConnection(false);
        }

        @Override
        public void onSuccess(NotificationList result) {
            view.setNotificationConnection(true);
            if(result != null) {
                GWT.log("unseen count ^^^^^^" + result.getUnseenTotal());
                view.setUnseenNotificationCount(Integer.parseInt(result.getUnseenTotal()));

                ListStore<NotificationMessage> store = view.getNotificationStore();
                for (Notification n : result.getNotifications()) {
                    store.add(n.getMessage());
                }
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
            final UserPreferencesCallback userPreferencesCallback = new UserPreferencesCallback(presenter,
                                                                                                panel,
                                                                                                userSettings,
                                                                                                announcer,
                                                                                                appearance);
            userSessionService.bootstrap(new BootstrapCallback(presenter,
                                                               userInfo,
                                                               errorHandlerProvider,
                                                               appearance,
                                                               userSessionService,
                                                               userPreferencesCallback));
        }
    }

    static class UserPreferencesCallback implements AsyncCallback<UserSetting> {
        private final DesktopPresenterImpl presenter;
        private final Panel panel;
        private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
        private final UserSettings userSettings;
        private IplantAnnouncer announcer;

        @Inject
        public UserPreferencesCallback(DesktopPresenterImpl presenter,
                                       Panel panel,
                                       UserSettings userSettings,
                                       IplantAnnouncer announcer,
                                       DesktopView.Presenter.DesktopPresenterAppearance appearance) {
            this.presenter = presenter;
            this.panel = panel;
            this.userSettings = userSettings;
            this.announcer = announcer;
            this.appearance = appearance;
        }

        @Override
        public void onFailure(Throwable caught) {
            announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString(appearance.userPreferencesLoadError()),
                                                           false,
                                                           5000));
            userSettings.setUserSettings(null);
            presenter.postBootstrap(panel);
        }

        @Override
        public void onSuccess(UserSetting result) {
            userSettings.setUserSettings(result);
            presenter.postBootstrap(panel);
        }
    }
}
