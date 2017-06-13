package org.iplantc.de.tools.client.events;

import org.iplantc.de.apps.client.models.ToolFilter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ToolFilterChanged extends GwtEvent<ToolFilterChanged.ToolFilterChangedHandler> {

    public static interface ToolFilterChangedHandler extends EventHandler {
        void onToolFilterChanged(ToolFilterChanged event);
    }

    public interface HasToolFilterChangedHandlers {
        HandlerRegistration addToolFilterChangedHandler(ToolFilterChangedHandler handler);
    }
    public static Type<ToolFilterChangedHandler> TYPE = new Type<ToolFilterChangedHandler>();
    private ToolFilter filter;

    public ToolFilterChanged(ToolFilter filter) {
        this.filter = filter;
    }

    public Type<ToolFilterChangedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ToolFilterChangedHandler handler) {
        handler.onToolFilterChanged(this);
    }

    public ToolFilter getFilter() {
        return filter;
    }
}