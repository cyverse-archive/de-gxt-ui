package org.iplantc.de.preferences.client;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.events.AddSlackWebhookClicked;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.events.ResetHpcTokenClicked;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author aramsey
 */
public interface PreferencesView extends IsWidget,
                                         PrefDlgRetryUserSessionClicked.HasPrefDlgRetryUserSessionClickedHandlers,
                                         ResetHpcTokenClicked.HasResetHpcLabelClickedHandlers,
                                         AddSlackWebhookClicked.HaAddSlackWebhookClickedHandlers {

    interface PreferencesViewAppearance {

        String defaultOutputFolderHelp();

        String done();

        String duplicateShortCutKey(String key);

        String notifyEmailHelp();

        String preferences();

        String notifyAnalysisEmail();

        String notifyImportEmail();

        String completeRequiredFieldsError();

        String rememberFileSectorPath();

        String rememberFileSectorPathHelp();

        String restoreDefaults();

        String saveSession();

        String defaultOutputFolder();

        String keyboardShortCut();

        String openAppsWindow();

        String kbShortcutMetaKey();

        String oneCharMax();

        String openDataWindow();

        String openAnalysesWindow();

        String openNotificationsWindow();

        String closeActiveWindow();

        String saveSessionHelp();

        String notifyEmail();

        String waitTime();

        String retrySessionConnection();

        SafeHtml sessionConnectionFailed();

        String preferencesFailure();

        String preferencesSuccess();

        String resetHpc();

        SafeHtml resetHpcPrompt();

        String hpcResetFailure();

        String resetHpcHelp();

        String webhooks();

        String addSlack();

        String webhooksPrompt();

        String dataNotification();

        String appsNotification();

        String analysesNotification();

        String toolsNotification();

        String permNotification();

        String hookTopic();

    }

    void userSessionSuccess();

    void userSessionFail();

    void initAndShow(UserSettings userSettings);

    UserSettings getValue();

    void setDefaultValues();

    void hide();

    boolean isValid();

    void flush();

    void saveUserSettings();

    void setWebhookValid(boolean valid);

    interface Presenter {

        void go(DesktopView.Presenter presenter, UserSettings userSettings);

        void setDefaultValues();

        void saveUserSettings();

        PreferencesView getView();

        void setViewDebugId(String baseId);
    }
}
