package org.iplantc.de.apps.integration.client.events;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class ShowToolInfoEvent extends GwtEvent<ShowToolInfoEvent.ShowToolInfoEventHandler> {

    public static interface ShowToolInfoEventHandler extends EventHandler {
        void onShowToolInfo(ShowToolInfoEvent event);
    }

    public interface HasShowToolInfoEventHandlers {
        HandlerRegistration addShowToolInfoEventHandlers(ShowToolInfoEventHandler handler);
    }

    public static Type<ShowToolInfoEventHandler> TYPE = new Type<ShowToolInfoEventHandler>();
    private Tool tool;

    public ShowToolInfoEvent(Tool tool) {
        this.tool = tool;
    }

    public Type<ShowToolInfoEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowToolInfoEventHandler handler) {
        handler.onShowToolInfo(this);
    }

    public Tool getTool() {
        return tool;
    }
}
