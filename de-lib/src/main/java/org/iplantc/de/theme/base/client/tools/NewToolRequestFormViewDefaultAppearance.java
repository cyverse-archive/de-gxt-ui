package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.resources.client.messages.IplantValidationMessages;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Created by sriram on 5/25/17.
 */
public class NewToolRequestFormViewDefaultAppearance implements NewToolRequestFormView.NewToolRequestFormViewAppearance {

    private final ToolMessages toolMessages;
    private final IplantValidationMessages validationMessages;
    private final IplantValidationConstants validationConstants;
    private final DiskResourceMessages diskResourceMessages;

    private final String BROWSER_FAKEPATH = "C:\\fakepath\\";

    public NewToolRequestFormViewDefaultAppearance() {
        this((ToolMessages)GWT.create(ToolMessages.class),
             GWT.<IplantValidationMessages>create(IplantValidationMessages.class),
             GWT.<IplantValidationConstants>create(IplantValidationConstants.class),
             GWT.<DiskResourceMessages>create(DiskResourceMessages.class));
        ;
    }

    public NewToolRequestFormViewDefaultAppearance(ToolMessages messages,
                                                   final IplantValidationMessages validationMessages,
                                                   final IplantValidationConstants validationConstants,
                                                   final DiskResourceMessages diskResourceMessages) {
        this.toolMessages = messages;
        this.validationMessages = validationMessages;
        this.validationConstants = validationConstants;
        this.diskResourceMessages = diskResourceMessages;
    }

    @Override
    public String newToolRequest() {
        return toolMessages.newToolRequest();
    }

    @Override
    public String makePublicRequest() {
        return toolMessages.makePublicRequest();
    }

    @Override
    public String newToolInstruction() {
        return toolMessages.newToolInstruction();
    }

    @Override
    public String makePublicInstruction() {
        return toolMessages.makePublicInstruction();
    }

    @Override
    public SafeHtml buildRequiredFieldLabel(final String label) {
        if (label == null) {
            return null;
        }

        return SafeHtmlUtils.fromTrustedString(
                "<span style='color:red; top:-5px;' >*</span> " + label); //$NON-NLS-1$
    }

    @Override
    public SafeHtml sameFileError(String filename) {
        return toolMessages.sameFileError(filename);
    }

    @Override
    public SafeHtml alert() {
        return toolMessages.alert();
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

    @Override
    public SafeHtml fileExists(String dupeFiles) {
        return toolMessages.fileExists(dupeFiles);
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
    public String getFileName(String filename) {
        if (Strings.isNullOrEmpty(filename) || !filename.contains(BROWSER_FAKEPATH)) {
            return filename;
        }
        return filename.substring(12);
    }
}
