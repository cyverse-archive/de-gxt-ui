package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user first clicks on the Create New Folder button in the Data window
 */
public class CreateNewFolderSelected
        extends GwtEvent<CreateNewFolderSelected.CreateNewFolderSelectedHandler> {
    public static interface CreateNewFolderSelectedHandler extends EventHandler {
        void onCreateNewFolderSelected(CreateNewFolderSelected event);
    }

    public interface HasCreateNewFolderSelectedHandlers {
        HandlerRegistration addCreateNewFolderSelectedHandler(CreateNewFolderSelectedHandler handler);
    }

    private Folder selectedFolder;

    public CreateNewFolderSelected(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public static Type<CreateNewFolderSelectedHandler> TYPE = new Type<CreateNewFolderSelectedHandler>();

    public Type<CreateNewFolderSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewFolderSelectedHandler handler) {
        handler.onCreateNewFolderSelected(this);
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }
}
