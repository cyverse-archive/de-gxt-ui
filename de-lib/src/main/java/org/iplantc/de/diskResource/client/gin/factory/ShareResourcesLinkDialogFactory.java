package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.diskResource.client.views.sharing.dialogs.ShareResourceLinkDialog;

/**
 * Created by sriram on 1/26/18.
 */
public interface ShareResourcesLinkDialogFactory {
    ShareResourceLinkDialog create(Boolean copyMultiLine);
}
