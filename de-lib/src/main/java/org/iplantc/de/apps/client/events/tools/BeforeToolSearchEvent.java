package org.iplantc.de.apps.client.events.tools;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by Sriram on 3/11/15.
 *
 * @author sriram
 */
public class BeforeToolSearchEvent extends GwtEvent<BeforeToolSearchEvent.BeforeToolSearchEventHandler> {
    public static interface BeforeToolSearchEventHandler extends EventHandler {
        void onBeforeToolSearch(BeforeToolSearchEvent event);
    }

    public static interface HasBeforeToolSearchEventHandlers {
        HandlerRegistration addBeforeToolSearchEventHandler(BeforeToolSearchEventHandler handler);
    }

    public static final Type<BeforeToolSearchEventHandler> TYPE = new Type<>();

    public Type<BeforeToolSearchEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(BeforeToolSearchEventHandler handler) {
        handler.onBeforeToolSearch(this);
    }
}
