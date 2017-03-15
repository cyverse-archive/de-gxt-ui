package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppEditorToolbar extends IsWidget,
                                          ArgumentOrderSelected.HasArgumentOrderSelectedHandlers,
                                          PreviewJsonSelected.HasPreviewJsonSelectedHandlers {

    public interface Presenter {

        void onPreviewUiClicked();

        /**
         * Submits the changed app to the server.
         */
        void onSaveClicked();

    }

    void setPresenter(AppEditorToolbar.Presenter presenter);

}
