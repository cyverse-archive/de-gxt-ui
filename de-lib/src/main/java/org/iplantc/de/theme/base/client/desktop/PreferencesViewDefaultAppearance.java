package org.iplantc.de.theme.base.client.desktop;

import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author jstroot
 */
public class PreferencesViewDefaultAppearance implements PreferencesView.PreferencesViewAppearance {

    interface Templates extends XTemplates {
        @XTemplates.XTemplate("<span style='color: red;'>{label}</span>")
        SafeHtml redText(String label);
    }

    private final IplantDisplayStrings displayStrings;
    private final DesktopContextualHelpMessages help;
    private final DesktopMessages desktopMessages;
    private Templates templates;
    private final IplantResources resources;

    PreferencesViewDefaultAppearance(final IplantDisplayStrings displayStrings,
                                     final DesktopContextualHelpMessages help,
                                     final DesktopMessages desktopMessages,
                                     final IplantResources resources,
                                     Templates templates) {
        this.displayStrings = displayStrings;
        this.help = help;
        this.desktopMessages = desktopMessages;
        this.templates = templates;
        this.resources = resources;
    }

    PreferencesViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<DesktopContextualHelpMessages> create(DesktopContextualHelpMessages.class),
             GWT.<DesktopMessages> create(DesktopMessages.class),
             GWT.create(IplantResources.class),
             GWT.<Templates> create(Templates.class));
    }

    @Override
    public String defaultOutputFolderHelp() {
        return help.defaultOutputFolderHelp();
    }

    @Override
    public String done() {
        return displayStrings.done();
    }

    @Override
    public String duplicateShortCutKey(String key) {
        return desktopMessages.duplicateShortCutKey(key);
    }

    @Override
    public String notifyEmailHelp() {
        return help.notifyEmailHelp();
    }

    @Override
    public String preferences() {
        return desktopMessages.preferences();
    }

    @Override
    public String notifyAnalysisEmail() {
        return desktopMessages.notifyAnalysisEmail();
    }

    @Override
    public String notifyImportEmail() {
        return desktopMessages.notifyImportEmail();
    }

    @Override
    public String completeRequiredFieldsError() {
        return displayStrings.completeRequiredFieldsError();
    }

    @Override
    public String rememberFileSectorPath() {
        return desktopMessages.rememberFileSectorPath();
    }

    @Override
    public String rememberFileSectorPathHelp() {
        return help.rememberFileSelectorPathHelp();
    }

    @Override
    public String restoreDefaults() {
        return desktopMessages.restoreDefaults();
    }

    @Override
    public String saveSession() {
        return desktopMessages.saveSession();
    }

    @Override
    public String defaultOutputFolder() {
        return desktopMessages.defaultOutputFolder();
    }

    @Override
    public String keyboardShortCut() {
        return desktopMessages.keyboardShortcuts();
    }

    @Override
    public String openAppsWindow() {
        return desktopMessages.openAppsWindow();
    }

    @Override
    public String kbShortcutMetaKey() {
        return desktopMessages.keyboardShortcutMetaKey();
    }

    @Override
    public String oneCharMax() {
        return desktopMessages.oneCharMax();
    }

    @Override
    public String openDataWindow() {
        return desktopMessages.openDataWindow();
    }

    @Override
    public String openAnalysesWindow() {
        return desktopMessages.openAnalysesWindow();
    }

    @Override
    public String openNotificationsWindow() {
        return desktopMessages.openNotificationsWindow();
    }

    @Override
    public String closeActiveWindow() {
        return desktopMessages.closeActiveWindow();
    }

    @Override
    public String saveSessionHelp() {
        return help.saveSessionHelp();
    }

    @Override
    public String notifyEmail() {
        return help.notifyEmail();
    }

    @Override
    public String waitTime() {
        return desktopMessages.waitTime();
    }

    public String retrySessionConnection() {
        return desktopMessages.retrySessionConnection();
    }

    @Override
    public SafeHtml sessionConnectionFailed() {
        return templates.redText(desktopMessages.sessionConnectionFailed());
    }

    @Override
    public String preferencesFailure() {
        return desktopMessages.preferencesFailure();
    }

    @Override
    public String preferencesSuccess() {
        return desktopMessages.preferencesSuccess();
    }

    @Override
    public String resetHpc() {
        return desktopMessages.resetHpc();
    }

    @Override
    public SafeHtml resetHpcPrompt() {
        return desktopMessages.resetHpcPrompt();
    }

    @Override
    public String hpcResetFailure() {
        return desktopMessages.hpcResetFailure();
    }

    @Override
    public String resetHpcHelp() {
        return help.resetHpcHelp();
    }

    @Override
    public String webhooks() {
        return desktopMessages.webhooks();
    }

    @Override
    public String webhooksPrompt() {
        return desktopMessages.webhooksPrompt();
    }

    @Override
    public String dataNotification() {
        return desktopMessages.dataNotification();
    }

    @Override
    public String appsNotification() {
        return desktopMessages.appsNotification();
    }

    @Override
    public String analysesNotification() {
        return desktopMessages.analysesNotification();
    }

    @Override
    public String toolsNotification() {
        return desktopMessages.toolsNotification();
    }

    @Override
    public String permNotification() {
        return desktopMessages.permNotification();
    }

    @Override
    public String hookTopic() {
        return desktopMessages.hookTopic();
    }

    @Override
    public String test() {
        return desktopMessages.test();
    }

    @Override
    public String teamNotification() {
        return desktopMessages.teamNotification();
    }

    @Override
    public String testWebhookFail() {
        return desktopMessages.testWebhookFail();
    }

    @Override
    public String testWebhookSuccess() {
        return desktopMessages.testWebhookSuccess();
    }

    @Override
    public String mustSelectATopic() {
        return desktopMessages.mustSelectATopic();
    }

    @Override
    public String data() {
        return desktopMessages.data();
    }

    @Override
    public String apps() {
        return desktopMessages.apps();
    }

    @Override
    public String analysis() {
        return desktopMessages.analysis();
    }

    @Override
    public String permIdRequest() {
        return desktopMessages.permIdRequest();
    }

    @Override
    public String team() {
        return desktopMessages.team();
    }

    @Override
    public String toolRequest() {
        return desktopMessages.toolRequest();
    }

    @Override
    public ImageResource deleteIcon() {
        return resources.delete();
    }

    @Override
    public String validUrl() {
        return desktopMessages.validUrl();
    }

    @Override
    public String width() {
        return "500";
    }

    @Override
    public String height() {
        return "550";
    }

    @Override
    public String emailSettings() {
        return desktopMessages.emailSettings();
    }

    @Override
    public String general() {
        return desktopMessages.general();
    }

    @Override
    public String notification() {
        return desktopMessages.notification();
    }

    @Override
    public String webhookTypeFailure() {
        return desktopMessages.webhookTypeFailure();
    }
}
