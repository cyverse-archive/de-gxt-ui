package org.iplantc.de.admin.desktop.client.toolAdmin.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AddToolSelectedEvent extends GwtEvent<AddToolSelectedEvent.AddToolSelectedEventHandler> {

    public interface AddToolSelectedEventHandler extends EventHandler {
        void onAddToolSelected(AddToolSelectedEvent event);
    }

    public interface HasAddToolSelectedEventHandlers {
        HandlerRegistration addAddToolSelectedEventHandler(AddToolSelectedEventHandler handler);
    }

    public static Type<AddToolSelectedEventHandler> TYPE = new Type<>();

    public Type<AddToolSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddToolSelectedEventHandler handler) {
        handler.onAddToolSelected(this);
    }

}
