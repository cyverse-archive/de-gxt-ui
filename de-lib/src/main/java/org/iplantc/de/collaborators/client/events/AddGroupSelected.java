package org.iplantc.de.collaborators.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AddGroupSelected extends GwtEvent<AddGroupSelected.AddGroupSelectedHandler> {

    public static interface AddGroupSelectedHandler extends EventHandler {
        void onAddGroupSelected(AddGroupSelected event);
    }

    public interface HasAddGroupSelectedHandlers {
        HandlerRegistration addAddGroupSelectedHandler(AddGroupSelectedHandler handler);
    }

    public static Type<AddGroupSelectedHandler> TYPE = new Type<AddGroupSelectedHandler>();

    public Type<AddGroupSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddGroupSelectedHandler handler) {
        handler.onAddGroupSelected(this);
    }
}
