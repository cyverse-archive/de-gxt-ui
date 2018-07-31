package org.iplantc.de.theme.base.client.desktop;

import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;

/**
 * @author jstroot
 */
public class BaseDesktopAppearance implements DesktopView.DesktopAppearance {


    private final IplantDisplayStrings displayStrings;
    private final DesktopContextualHelpMessages help;

    BaseDesktopAppearance(final IplantDisplayStrings iplantDisplayStrings,
                          final DesktopContextualHelpMessages desktopContextualHelpMessages) {
        displayStrings = iplantDisplayStrings;
        this.help = desktopContextualHelpMessages;
    }

    public BaseDesktopAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
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

}
