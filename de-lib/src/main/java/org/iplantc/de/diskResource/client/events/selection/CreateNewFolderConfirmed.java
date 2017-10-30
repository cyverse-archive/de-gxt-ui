package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that gets fired after the user has opted to create a new folder and provided a folder name
 */
public class CreateNewFolderConfirmed
        extends GwtEvent<CreateNewFolderConfirmed.CreateNewFolderConfirmedHandler> {
    public static interface CreateNewFolderConfirmedHandler extends EventHandler {
        void onCreateNewFolderConfirmed(CreateNewFolderConfirmed event);
    }

    public interface HasCreateNewFolderConfirmedHandlers {
        HandlerRegistration addCreateNewFolderConfirmedHandler(CreateNewFolderConfirmedHandler handler);
    }

    private Folder parentFolder;
    private String folderName;

    public CreateNewFolderConfirmed(Folder parentFolder, String folderName) {
        this.parentFolder = parentFolder;
        this.folderName = folderName;
    }

    public static Type<CreateNewFolderConfirmedHandler> TYPE = new Type<CreateNewFolderConfirmedHandler>();

    public Type<CreateNewFolderConfirmedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewFolderConfirmedHandler handler) {
        handler.onCreateNewFolderConfirmed(this);
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public String getFolderName() {
        return folderName;
    }
}
