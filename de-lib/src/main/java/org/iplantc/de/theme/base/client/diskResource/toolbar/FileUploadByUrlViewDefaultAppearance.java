package org.iplantc.de.theme.base.client.diskResource.toolbar;

import org.iplantc.de.diskResource.client.FileUploadByUrlView;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.util.Format;

/**
 * @author jstroot
 */
public class FileUploadByUrlViewDefaultAppearance implements FileUploadByUrlView.FileUploadByUrlViewAppearance {

    public interface Templates extends SafeHtmlTemplates {
        @Template("<div title='{0}' style='color: #0098AA;width:100%;padding:5px;text-overflow:ellipsis;'>{1}</div>")
        SafeHtml destinationPathLabel(String destPath, String uploadMsg);
    }

    private final DiskResourceMessages diskResourceMessages;
    private final IplantErrorStrings iplantErrorStrings;
    private Templates templates;

    public FileUploadByUrlViewDefaultAppearance() {
        this(GWT.<DiskResourceMessages> create(DiskResourceMessages.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<Templates> create(Templates.class));
    }

    FileUploadByUrlViewDefaultAppearance(final DiskResourceMessages diskResourceMessages,
                                         final IplantErrorStrings iplantErrorStrings,
                                         Templates templates) {
        this.diskResourceMessages = diskResourceMessages;
        this.iplantErrorStrings = iplantErrorStrings;
        this.templates = templates;
    }

    @Override
    public String importLabel() {
        return diskResourceMessages.importLabel();
    }

    @Override
    public String uploadingToFolder(String path) {
        return diskResourceMessages.uploadingToFolder(path);
    }

    @Override
    public String urlImport() {
        return diskResourceMessages.urlImport();
    }

    @Override
    public String urlPrompt() {
        return diskResourceMessages.urlPrompt();
    }

    @Override
    public String containerWidth() {
        return "475";
    }

    @Override
    public String containerHeight() {
        return "50";
    }

    @Override
    public SafeHtml destText(String destPath, String destName) {
        String uploadMsg = Format.ellipse(uploadingToFolder(destName), 50);
        return templates.destinationPathLabel(destPath, uploadMsg);
    }
}
