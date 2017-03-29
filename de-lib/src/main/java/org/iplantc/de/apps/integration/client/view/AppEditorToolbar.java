package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.PreviewAppSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;
import org.iplantc.de.apps.integration.client.events.SaveAppSelected;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppEditorToolbar extends IsWidget,
                                          ArgumentOrderSelected.HasArgumentOrderSelectedHandlers,
                                          PreviewJsonSelected.HasPreviewJsonSelectedHandlers,
                                          PreviewAppSelected.HasPreviewAppSelectedHandlers,
                                          SaveAppSelected.HasSaveAppSelectedHandlers {

}
