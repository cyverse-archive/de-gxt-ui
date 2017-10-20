package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface for the view displayed to the user when trying to apply metadata in bulk
 */
public interface BulkMetadataView extends IsWidget,
                                          IsMaskable {

    /**
     * An enum to represent the view mode, either uploading or selecting files from the data window
     */
    enum BULK_MODE {
        UPLOAD, SELECT
    }

    /**
     * An appearance class for the BulkMetadataView
     */
    interface BulkMetadataViewAppearance {
        String heading();

        SafeHtml selectMetadataFile();

        String selectTemplate();

        String applyBulkMetadata();

        String uploadMetadata();

        String formWidth();

        String formHeight();

        String dialogWidth();

        String dialogHeight();

        String reset();
    }

    /**
     * Returns true if the user filled out the BulkMetadataView in its entirety and correctly
     * @return
     */
    boolean isValid();

    /**
     * Returns the path
     * @return
     */
    String getSelectedPath();
}
