package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that gets fired when the user selects "Open Trash" from the Data window
 */
public class OpenTrashFolderSelected
        extends GwtEvent<OpenTrashFolderSelected.OpenTrashFolderSelectedHandler> {
    public static interface OpenTrashFolderSelectedHandler extends EventHandler {
        void onOpenTrashFolderSelected(OpenTrashFolderSelected event);
    }

    public interface HasOpenTrashFolderSelectedHandlers {
        HandlerRegistration addOpenTrashFolderSelectedHandler(OpenTrashFolderSelectedHandler handler);
    }

    public static Type<OpenTrashFolderSelectedHandler> TYPE = new Type<OpenTrashFolderSelectedHandler>();

    public Type<OpenTrashFolderSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(OpenTrashFolderSelectedHandler handler) {
        handler.onOpenTrashFolderSelected(this);
    }
}
