package org.iplantc.de.apps.client.events.tools;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 *  created by sriram.
 */
public class DeleteToolSelected extends GwtEvent<DeleteToolSelected.DeleteToolsSelectedHandler> {
    public static interface DeleteToolsSelectedHandler extends EventHandler {
        void onDeleteToolsSelected(DeleteToolSelected event);
    }

    public static interface HasDeleteToolsSelectedHandlers {
        HandlerRegistration addDeleteToolsSelectedHandler(DeleteToolsSelectedHandler handler);
    }

    public static final Type<DeleteToolsSelectedHandler> TYPE = new Type<>();

    public DeleteToolSelected() {
    }

    public Type<DeleteToolsSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteToolsSelectedHandler handler) {
        handler.onDeleteToolsSelected(this);
    }
}
