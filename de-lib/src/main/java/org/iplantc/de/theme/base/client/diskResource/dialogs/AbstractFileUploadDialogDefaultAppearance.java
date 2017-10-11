package org.iplantc.de.theme.base.client.diskResource.dialogs;

import org.iplantc.de.commons.client.views.dialogs.AbstractFileUploadDialog;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.messages.IplantValidationMessages;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.util.Format;

import java.util.List;

/**
 * @author jstroot
 */
public class AbstractFileUploadDialogDefaultAppearance implements AbstractFileUploadDialog.AbstractFileUploadDialogAppearance {

    public interface Templates extends SafeHtmlTemplates {
        @Template("<div title='{0}' style='color: #0098AA;width:100%;padding:5px;text-overflow:ellipsis;'>{1}</div>")
        SafeHtml destinationPathLabel(String destPath, String parentPath);
    }

    private final DiskResourceMessages diskResourceMessages;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final IplantErrorStrings iplantErrorStrings;
    private final Templates templates;
    private final IplantResources resources;
    private final IplantValidationMessages validationMessages;
    private final IplantValidationConstants validationConstants;

    public AbstractFileUploadDialogDefaultAppearance() {
        this(GWT.<DiskResourceMessages> create(DiskResourceMessages.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<Templates> create(Templates.class),
             GWT.<IplantResources>create(IplantResources.class),
             GWT.<IplantValidationMessages>create(IplantValidationMessages.class),
             GWT.<IplantValidationConstants>create(IplantValidationConstants.class));
    }

    AbstractFileUploadDialogDefaultAppearance(final DiskResourceMessages diskResourceMessages,
                                              final IplantDisplayStrings iplantDisplayStrings,
                                              final IplantErrorStrings iplantErrorStrings,
                                              final Templates templates,
                                              final IplantResources resources,
                                              final IplantValidationMessages validationMessages,
                                              final IplantValidationConstants validationConstants) {
        this.diskResourceMessages = diskResourceMessages;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantErrorStrings = iplantErrorStrings;
        this.templates = templates;
        this.resources = resources;
        this.validationMessages = validationMessages;
        this.validationConstants = validationConstants;
    }

    @Override
    public String confirmAction() {
        return iplantDisplayStrings.confirmAction();
    }

    @Override
    public SafeHtml fileExists(String filenames) {
        return iplantErrorStrings.fileExists(filenames);
    }

    @Override
    public String invalidFileName() {
        return diskResourceMessages.invalidFileName();
    }

    @Override
    public String fileNameValidationMsg() {
        return validationMessages.drNameValidationMsg(
                validationConstants.newlineToPrint() + validationConstants.tabToPrint()
                + validationConstants.restrictedDiskResourceNameChars()) + " Offending file(s): ";
    }

    @Override
    public String fileUploadMaxSizeWarning() {
        return diskResourceMessages.fileUploadMaxSizeWarning();
    }

    @Override
    public ImageResource arrowUndoIcon() {
        return resources.arrowUndoIcon();
    }

    @Override
    public SafeHtml fileUploadsFailed(List<String> strings) {
        return iplantErrorStrings.fileUploadsFailed(strings);
    }

    @Override
    public String closeConfirmMessage() {
        return iplantDisplayStrings.transferCloseConfirmMessage();
    }

    @Override
    public SafeHtml renderDestinationPathLabel(String destPath, String parentPath) {
        final String truncatedParent =
                fileUploadMaxSizeWarning() + " " + Format.ellipse(diskResourceMessages.uploadingToFolder(
                        parentPath), 50);
        return templates.destinationPathLabel(destPath, truncatedParent);
    }

    @Override
    public String reset() {
        return diskResourceMessages.reset();
    }

    @Override
    public String upload() {
        return iplantDisplayStrings.upload();
    }

    @Override
    public String fileUploadsSuccess(List<String> strings) {
        return diskResourceMessages.fileUploadsSuccess(strings);
    }

    @Override
    public SafeHtml fileSizeViolation(String filename) {
        return diskResourceMessages.fileSizeViolation(filename);
    }

    @Override
    public SafeHtml maxFileSizeExceed() {
        return diskResourceMessages.maxFileSizeExceed();
    }

    @Override
    public SafeHtml fileExistTitle() {
        return diskResourceMessages.fileExistTitle();
    }

}
