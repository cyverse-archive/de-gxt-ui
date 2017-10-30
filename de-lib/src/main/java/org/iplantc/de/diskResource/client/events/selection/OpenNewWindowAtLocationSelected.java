package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the New Data Window at this location button in the Data window
 */
public class OpenNewWindowAtLocationSelected
        extends GwtEvent<OpenNewWindowAtLocationSelected.OpenNewWindowAtLocationSelectedHandler> {
    public static interface OpenNewWindowAtLocationSelectedHandler extends EventHandler {
        void onOpenNewWindowAtLocationSelected(OpenNewWindowAtLocationSelected event);
    }

    public interface HasOpenNewWindowAtLocationSelectedHandlers {
        HandlerRegistration addOpenNewWindowAtLocationSelectedHandler(OpenNewWindowAtLocationSelectedHandler handler);
    }

    private Folder selectedFolder;

    public OpenNewWindowAtLocationSelected(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public static Type<OpenNewWindowAtLocationSelectedHandler> TYPE =
            new Type<OpenNewWindowAtLocationSelectedHandler>();

    public Type<OpenNewWindowAtLocationSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(OpenNewWindowAtLocationSelectedHandler handler) {
        handler.onOpenNewWindowAtLocationSelected(this);
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }
}
