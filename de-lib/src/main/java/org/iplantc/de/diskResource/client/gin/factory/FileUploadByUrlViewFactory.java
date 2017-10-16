package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.diskResource.client.FileUploadByUrlView;

public interface FileUploadByUrlViewFactory {
    FileUploadByUrlView create(Folder uploadDest);
}
