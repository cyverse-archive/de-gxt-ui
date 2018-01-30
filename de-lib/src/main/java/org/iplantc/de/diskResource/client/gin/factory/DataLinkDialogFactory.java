package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.diskResource.client.views.dialogs.DataLinkDialog;

/**
 * Created by sriram on 1/26/18.
 */
public interface DataLinkDialogFactory {
    DataLinkDialog create(Boolean copyMultiLine);
}
