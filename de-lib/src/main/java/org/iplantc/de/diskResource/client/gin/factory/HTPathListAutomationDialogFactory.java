package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.diskResource.client.views.dialogs.HTPathListAutomationDialog;

import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * Created by sriram on 7/26/17.
 */
public interface HTPathListAutomationDialogFactory {

    HTPathListAutomationDialog create(DiskResourceSelectorFieldFactory factory,
                                      @Assisted("infoTypes") List<InfoType> infoTypes);
}
