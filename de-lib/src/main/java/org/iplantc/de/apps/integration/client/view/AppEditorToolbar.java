package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppEditorToolbar extends IsWidget,
                                          ArgumentOrderSelected.HasArgumentOrderSelectedHandlers {

    public interface Presenter {

        void onPreviewJsonClicked();

        void onPreviewUiClicked();

        /**
         * Submits the changed app to the server.
         */
        void onSaveClicked();

    }

    void setPresenter(AppEditorToolbar.Presenter presenter);

}
