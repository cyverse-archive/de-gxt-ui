package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that gets fired after the user has opted to create a new folder and provided a folder name
 */
public class CreateNewFolderSelected
        extends GwtEvent<CreateNewFolderSelected.CreateNewFolderSelectedHandler> {
    public static interface CreateNewFolderSelectedHandler extends EventHandler {
        void onCreateNewFolderSelected(CreateNewFolderSelected event);
    }

    public interface HasCreateNewFolderSelectedHandlers {
        HandlerRegistration addCreateNewFolderSelectedHandler(CreateNewFolderSelectedHandler handler);
    }

    private Folder parentFolder;
    private String folderName;

    public CreateNewFolderSelected(Folder parentFolder, String folderName) {
        this.parentFolder = parentFolder;
        this.folderName = folderName;
    }

    public static Type<CreateNewFolderSelectedHandler> TYPE = new Type<CreateNewFolderSelectedHandler>();

    public Type<CreateNewFolderSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewFolderSelectedHandler handler) {
        handler.onCreateNewFolderSelected(this);
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public String getFolderName() {
        return folderName;
    }
}
