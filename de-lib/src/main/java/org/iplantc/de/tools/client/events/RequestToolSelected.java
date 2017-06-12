package org.iplantc.de.tools.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 *
 * @author sriram
 */
public class RequestToolSelected extends GwtEvent<RequestToolSelected.RequestToolSelectedHandler> {
    public static interface HasRequestToolSelectedHandlers {
        HandlerRegistration addRequestToolSelectedHandler(RequestToolSelectedHandler handler);
    }

    public static interface RequestToolSelectedHandler extends EventHandler {
        void onRequestToolSelected(RequestToolSelected event);
    }
    public static final Type<RequestToolSelectedHandler> TYPE = new Type<>();

    public Type<RequestToolSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RequestToolSelectedHandler handler) {
        handler.onRequestToolSelected(this);
    }
}
