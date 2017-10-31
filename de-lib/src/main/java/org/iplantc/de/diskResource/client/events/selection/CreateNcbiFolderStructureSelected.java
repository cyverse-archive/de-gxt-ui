package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the Create NCBI Sra Folder Structure button in the Data window
 */
public class CreateNcbiFolderStructureSelected
        extends GwtEvent<CreateNcbiFolderStructureSelected.CreateNcbiFolderStructureSelectedHandler> {
    public interface CreateNcbiFolderStructureSelectedHandler extends EventHandler {
        void onCreateNcbiFolderStructureSelected(CreateNcbiFolderStructureSelected event);
    }

    public interface HasCreateNcbiFolderStructureSelectedHandlers {
        HandlerRegistration addCreateNcbiFolderStructureSelectedHandler(CreateNcbiFolderStructureSelectedHandler handler);
    }

    private Folder selectedFolder;

    public CreateNcbiFolderStructureSelected(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public static Type<CreateNcbiFolderStructureSelectedHandler> TYPE =
            new Type<CreateNcbiFolderStructureSelectedHandler>();

    public Type<CreateNcbiFolderStructureSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNcbiFolderStructureSelectedHandler handler) {
        handler.onCreateNcbiFolderStructureSelected(this);
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }
}
