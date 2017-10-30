package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.viewer.MimeType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event fired when the user selects the Create New File button in the Data window
 */
public class CreateNewFileSelected extends GwtEvent<CreateNewFileSelected.CreateNewFileSelectedHandler> {
    public static interface CreateNewFileSelectedHandler extends EventHandler {
        void onCreateNewFileSelected(CreateNewFileSelected event);
    }

    public interface HasCreateNewFileSelectedHandlers {
        HandlerRegistration addCreateNewFileSelectedHandler(CreateNewFileSelectedHandler handler);
    }

    private Folder selectedFolder;
    private MimeType mimeType;

    public CreateNewFileSelected(Folder selectedFolder, MimeType mimeType) {
        this.selectedFolder = selectedFolder;
        this.mimeType = mimeType;
    }

    public static Type<CreateNewFileSelectedHandler> TYPE = new Type<CreateNewFileSelectedHandler>();

    public Type<CreateNewFileSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewFileSelectedHandler handler) {
        handler.onCreateNewFileSelected(this);
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public MimeType getMimeType() {
        return mimeType;
    }
}
