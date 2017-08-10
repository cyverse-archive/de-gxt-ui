package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class AddPublicUserSelected extends GwtEvent<AddPublicUserSelected.AddPublicUserSelectedHandler> {
    public static interface AddPublicUserSelectedHandler extends EventHandler {
        void onAddPublicUserSelected(AddPublicUserSelected event);
    }

    public interface HasAddPublicUserSelectedHandlers {
        HandlerRegistration addAddPublicUserSelectedHandler(AddPublicUserSelectedHandler handler);
    }

    public static Type<AddPublicUserSelectedHandler> TYPE = new Type<AddPublicUserSelectedHandler>();

    public Type<AddPublicUserSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddPublicUserSelectedHandler handler) {
        handler.onAddPublicUserSelected(this);
    }
}
