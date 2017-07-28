package org.iplantc.de.theme.base.client.diskResource.dialogs;

import org.iplantc.de.diskResource.client.views.dialogs.HTPathListAutomationDialog;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Created by sriram on 7/27/17.
 */
public class HTPathListAutomationDefaultAppearance
        implements HTPathListAutomationDialog.HTPathListAutomationAppearance {

    private final DiskResourceMessages diskResourceMessages =
            GWT.<DiskResourceMessages>create(DiskResourceMessages.class);

    @Override
    public String inputLbl() {
        return diskResourceMessages.inputLbl();
    }

    @Override
    public String folderPathOnlyLbl() {
        return diskResourceMessages.folderPathOnlyLbl();
    }

    @Override
    public String selectorEmptyText() {
        return diskResourceMessages.selectorEmptyText();
    }

    @Override
    public SafeHtml patternMatchLbl() {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant(diskResourceMessages.patternMatchLbl());
        return builder.toSafeHtml();
    }

    @Override
    public String infoTypeLbl() {
        return diskResourceMessages.infoTypeLbl();
    }

    @Override
    public String destLbl() {
        return diskResourceMessages.destLbl();
    }

    @Override
    public String patternMatchEmptyText() {
        return diskResourceMessages.patternMatchEmptyText();
    }

    @Override
    public String heading() {
        return diskResourceMessages.Htheading();
    }

    @Override
    public String loading() {
        return diskResourceMessages.loading();
    }

    @Override
    public String processing() {
        return diskResourceMessages.processing();
    }

    @Override
    public String requestSuccess() {
        return diskResourceMessages.requestSuccess();
    }

    @Override
    public String requestFailed() {
        return diskResourceMessages.requestFailed();
    }

    @Override
    public SafeHtml formatRequiredFieldLbl(String label) {
        if (label == null) {
            return null;
        }

        return SafeHtmlUtils.fromTrustedString(
                "<span style='color:red; top:-5px;' >*</span> " + label); //$NON-NLS-1$
    }

    @Override
    public String folderPathOnlyPrompt() {
        return diskResourceMessages.folderPathOnlyPrompt();
    }

    @Override
    public String validationMessage() {
        return diskResourceMessages.validationMessage();
    }

    @Override
    public String dialogHeight() {
        return "700px";
    }

    @Override
    public String dialogWidth() {
        return "800px";
    }

    @Override
    public String listHeight() {
        return "75px";
    }

    @Override
    public String destSelectorWidth() {
        return "700px";
    }

    @Override
    public String select() {
        return diskResourceMessages.select();
    }
}
