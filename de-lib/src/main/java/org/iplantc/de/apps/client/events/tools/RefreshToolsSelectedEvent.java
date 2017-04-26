package org.iplantc.de.apps.client.events.tools;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author sriram
 */
public class RefreshToolsSelectedEvent
        extends GwtEvent<RefreshToolsSelectedEvent.RefreshToolsSelectedEventHandler> {

    public static interface RefreshToolsSelectedEventHandler extends EventHandler {
        void onRefreshToolsSelected(RefreshToolsSelectedEvent event);
    }

    public interface HasRefreshToolsSelectedEventHandlers {
        HandlerRegistration addRefreshToolsSelectedEventHandler(RefreshToolsSelectedEventHandler handler);
    }

    public static Type<RefreshToolsSelectedEventHandler> TYPE =
            new Type<RefreshToolsSelectedEventHandler>();

    public Type<RefreshToolsSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RefreshToolsSelectedEventHandler handler) {
        handler.onRefreshToolsSelected(this);
    }
}
