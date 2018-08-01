package org.iplantc.de.desktop.client;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.callbacks.ErrorCallback;
import org.iplantc.de.commons.client.views.window.configs.SavedWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.presenter.NotificationsCallback;
import org.iplantc.de.desktop.client.presenter.callbacks.NotificationMarkAsSeenCallback;
import org.iplantc.de.desktop.client.views.windows.WindowBase;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Window;

import java.util.List;
import java.util.Map;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * TODO JDS Change initial display time for user menu tooltips
 * TODO JDS Window layout, implement as 'one-shot' layouts. 'layout then done' no re-sizing
 *
 * Notifications, window events, layouts
 *
 * <ul>
 *     <li>Task bar buttons are controlled from the view.
 *     <li>Moved minimize functionality to WindowBase. Was previously in Desktop.minimizeWindow
 *     <li>Getting rid of window managers as it previously existed. Making more use of GXT's existing
 *         WindowManager.
 * </ul>
 *
 * @author jstroot
 */
@JsType
public interface DesktopView extends IsWidget {

    @JsType
    interface DesktopAppearance {

        String feedbackAlertValidationWarning();

        String completeRequiredFieldsError();
    }

    /**
     * This presenter is responsible for the following;
     * -- Initializing properties, user info, user settings.
     * -- Desktop window management.
     * -- Maintaining user session saving
     * -- Handling user desktop events for the desktop icons and user settings menu
     * <p>
     * <p>
     * TODO JDS Eventually, certain injected parameters will be injected via an AsyncProvider
     * This will provide us with split points through injection. Only items which are not
     * immediately necessary should be provided this way.
     */
    @JsType
    interface Presenter extends UserSettingsMenuPresenter, UnseenNotificationsPresenter {

        @JsIgnore
        void setDesktopContainer(Element desktopContainer);

        interface DesktopPresenterAppearance {

            String feedbackServiceFailure();

            String feedbackSubmitted();

            String fetchNotificationsError();

            String introWelcome();

            String loadSessionFailed();

            String loadingSession();

            String loadingSessionWaitNotice();

            String markAllAsSeenSuccess();

            String newNotificationsAlert();

            String permissionErrorMessage();

            String permissionErrorTitle();

            String saveSessionFailed();

            String saveSettings();

            String savingSession();

            String savingSessionWaitNotice();

            SafeHtml sessionRestoreCancelled();

            String systemInitializationError();

            String welcome();

            String requestHistoryError();

            String userPreferencesLoadError();

            String newApp();

            String sectionOne();

            SafeHtml webhookSaveError();

            /**
             * Error announcement text when user tries to edit a second app
             *
             * @return
             */
            String cannotEditTwoApps();
        }

        @JsIgnore
        List<SavedWindowConfig> getOrderedWindowConfigs();

        @JsIgnore
        List<WindowState> getWindowStates();

        @JsIgnore
        void go(Panel panel);

        void onAnalysesWinBtnSelect();

        void onAppsWinBtnSelect();

        void onDataWinBtnSelect();

        void onForumsBtnSelect();

        void onFeedbackSelect();

        @SuppressWarnings("unusuable-by-js")
        void onNotificationSelected(final Splittable notificationMessage,
                                    final NotificationMarkAsSeenCallback callback,
                                    final ErrorCallback errorCallback);

        void displayNotificationPopup(String message, String category, String analysisStatus);

        @JsIgnore
        void saveUserSettings(UserSettings value, boolean updateSilently);

        @JsIgnore
        void show(final WindowConfig config);

        @JsIgnore
        void show(final WindowConfig config, final boolean updateExistingWindow);

        @JsIgnore
        void submitUserFeedback(Splittable splittable, IsHideable isHideable);

        @JsIgnore
        void stickWindowToTop(Window window);

        @JsIgnore
        void doPeriodicSessionSave();

        @JsIgnore
        void doPeriodicWindowStateSave();

        @JsIgnore
        void restoreWindows(List<SavedWindowConfig> savedWindowConfigs);

        @JsIgnore
        void setUserSessionConnection(boolean connected);

        void onFaqSelect();

        /**
         * This method is called once a user has either approved or denied a request to join
         * a team the user manages.  This method will delete the join notification so the request
         * cannot accidentally be processed again.
         *
         * @param message
         */
        @JsIgnore
        void onJoinTeamRequestProcessed(NotificationMessage message);

        @SuppressWarnings("unusuable-by-js")
        void onTaskButtonClicked(Splittable windowConfig);
    }

    @JsType
    interface UserSettingsMenuPresenter {

        void onAboutClick();

        void onCollaboratorsClick();

        void onPreferencesClick();

        void onContactSupportClick();

        void onDocumentationClick();

        void onIntroClick();

        void doLogout(boolean sessionTimeout);
    }

    @JsType
    interface UnseenNotificationsPresenter {

        void doMarkAllSeen(boolean announce,
                           final NotificationMarkAsSeenCallback callback,
                           final ErrorCallback errorCallback);

        void doSeeAllNotifications();

        void doSeeNewNotifications();

        void getNotifications(NotificationsCallback callback, ErrorCallback errorCallback);
    }


    /**
     * @return the desktop container element used to constrain {@link WindowInterface} classes
     */
    @JsIgnore
    Element getDesktopContainer();

    @JsIgnore
    void setPresenter(Presenter presenter);

    @JsIgnore
    void renderView(Map<Splittable, WindowBase> windowConfigMap);

    @JsIgnore
    void renderView(boolean newUser, Map<Splittable, WindowBase> windowConfigMap);

    void onFeedbackBtnSelect();
}


