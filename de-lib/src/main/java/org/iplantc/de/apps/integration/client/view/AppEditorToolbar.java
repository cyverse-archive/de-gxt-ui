package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.PreviewAppSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppEditorToolbar extends IsWidget,
                                          ArgumentOrderSelected.HasArgumentOrderSelectedHandlers,
                                          PreviewJsonSelected.HasPreviewJsonSelectedHandlers,
                                          PreviewAppSelected.HasPreviewAppSelectedHandlers {

    public interface Presenter {

        /**
         * Submits the changed app to the server.
         */
        void onSaveClicked();

    }

    void setPresenter(AppEditorToolbar.Presenter presenter);

}
