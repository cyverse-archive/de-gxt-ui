package org.iplantc.de.theme.base.client.diskResource.metadata;

import org.iplantc.de.diskResource.client.BulkMetadataView;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public class BulkMetadataViewDefaultAppearance implements BulkMetadataView.BulkMetadataViewAppearance {

    public interface Templates extends SafeHtmlTemplates {
        @Template("<span style='color:red; top:-5px;'>*</span>{0}")
        SafeHtml requiredLabel(String label);
    }

    private final DiskResourceMessages diskResourceMessages;
    private Templates templates;

    public BulkMetadataViewDefaultAppearance() {
        this(GWT.<DiskResourceMessages> create(DiskResourceMessages.class),
             GWT.<Templates> create(Templates.class));
    }

    public BulkMetadataViewDefaultAppearance(DiskResourceMessages diskResourceMessages,
                                             Templates templates) {

        this.diskResourceMessages = diskResourceMessages;
        this.templates = templates;
    }

    @Override
    public String heading() {
        return diskResourceMessages.bulkMetadataHeading();
    }

    @Override
    public SafeHtml selectMetadataFile() {
        return templates.requiredLabel(diskResourceMessages.selectMetadataFile());
    }

    @Override
    public String selectTemplate() {
        return diskResourceMessages.selectTemplate();
    }

    @Override
    public String applyBulkMetadata() {
        return diskResourceMessages.applyBulkMetadata();
    }

    @Override
    public String uploadMetadata() {
        return diskResourceMessages.uploadMetadata();
    }

    @Override
    public String formWidth() {
        return "475";
    }

    @Override
    public String formHeight() {
        return "28";
    }

    @Override
    public String dialogWidth() {
        return "600px";
    }

    @Override
    public String dialogHeight() {
        return "200px";
    }

    @Override
    public String reset() {
        return diskResourceMessages.reset();
    }
}
