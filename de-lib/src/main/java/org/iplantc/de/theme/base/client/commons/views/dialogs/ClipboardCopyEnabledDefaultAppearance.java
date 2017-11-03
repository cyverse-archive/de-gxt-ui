package org.iplantc.de.theme.base.client.commons.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;

import com.google.gwt.core.client.GWT;

/**
 * Appearance implementation for Clipboard dialog
 *
 * Created by sriram on 11/3/17.
 */
public class ClipboardCopyEnabledDefaultAppearance implements ClipboardCopyEnabledDialog.Appearance {


    private ClipboardCopyDisplayStrings displayStrings;


    public ClipboardCopyEnabledDefaultAppearance() {
        this(GWT.create(ClipboardCopyDisplayStrings.class));
    }

    public ClipboardCopyEnabledDefaultAppearance(ClipboardCopyDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;

    }

    @Override
    public String copyPasteInstructions() {
        return displayStrings.copyPasteInstructions();
    }


    @Override
    public String copyToClipboard() {
        return displayStrings.copyToClipboard();
    }

    @Override
    public String textboxWidth() {
        return "375px";
    }

    @Override
    public String textboxHeight() {
        return "20px";
    }

    @Override
    public String errorCopying() {
        return displayStrings.errorCopying();
    }
}
