package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.diskResource.client.BulkMetadataView;

import com.google.inject.assistedinject.Assisted;

public interface BulkMetadataViewFactory {
    BulkMetadataView create(@Assisted("mode") BulkMetadataView.BULK_MODE mode);
}
