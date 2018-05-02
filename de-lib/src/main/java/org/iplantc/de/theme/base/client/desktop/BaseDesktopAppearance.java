package org.iplantc.de.theme.base.client.desktop;

import com.google.gwt.core.client.GWT;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

/**
 * @author jstroot
 */
public class BaseDesktopAppearance implements DesktopView.DesktopAppearance {


    private final DesktopMessages desktopMessages;
    private final IplantDisplayStrings displayStrings;
    private final DesktopContextualHelpMessages help;

    BaseDesktopAppearance(final IplantDisplayStrings iplantDisplayStrings,
                          final DesktopMessages desktopMessages,
                          final DesktopContextualHelpMessages desktopContextualHelpMessages) {
        displayStrings = iplantDisplayStrings;
        this.desktopMessages = desktopMessages;
        this.help = desktopContextualHelpMessages;
    }

    public BaseDesktopAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<DesktopMessages> create(DesktopMessages.class),
             GWT.<DesktopContextualHelpMessages> create(DesktopContextualHelpMessages.class));
    }

    @Override
    public String feedbackAlertValidationWarning() {
        return displayStrings.warning();
    }

    @Override
    public String completeRequiredFieldsError() {
        return displayStrings.completeRequiredFieldsError();
    }

    @Override
    public String notifications() {
        return displayStrings.notifications();
    }

    @Override
    public String preferences() {
        return desktopMessages.preferences();
    }

    @Override
    public String collaboration() {
        return displayStrings.collaboration();
    }

    @Override
    public String introduction() {
        return desktopMessages.introduction();
    }

    @Override
    public String documentation() {
        return displayStrings.documentation();
    }

    @Override
    public String contactSupport() {
        return desktopMessages.contactSupport();
    }

    @Override
    public String about() {
        return desktopMessages.about();
    }

    @Override
    public String logout() {
        return displayStrings.logout();
    }

    @Override
    public String iconHomepageDataTip() {
        return help.iconHomepageDataTip();
    }

    @Override
    public String forum() {
        return desktopMessages.forum();
    }

    @Override
    public String faqs() {
        return desktopMessages.faqs();
    }

    @Override
    public String feedback() {
        return desktopMessages.feedback();
    }

    @Override
    public String iconHomepageAnalysesTip() {
        return help.iconHomepageAnalysesTip();
    }

    @Override
    public String iconHomepageAppsTip() {
        return help.iconHomepageAppsTip();
    }
}
