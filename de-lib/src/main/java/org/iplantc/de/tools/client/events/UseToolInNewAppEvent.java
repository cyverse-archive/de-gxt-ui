package org.iplantc.de.tools.client.events;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author sriram
 */
public class UseToolInNewAppEvent extends GwtEvent<UseToolInNewAppEvent.UseToolInNewAppEventHandler> {

    private final Tool tool;

    public interface UseToolInNewAppEventHandler extends EventHandler {
        void useToolInNewApp(UseToolInNewAppEvent event);
    }

    public UseToolInNewAppEvent(Tool selectedTool) {
        this.tool = selectedTool;
    }

    public Tool getTool() {
        return tool;
    }

    public static final Type<UseToolInNewAppEventHandler> TYPE = new Type<>();

    @Override
    public Type<UseToolInNewAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UseToolInNewAppEventHandler handler) {
        handler.useToolInNewApp(this);
    }

}
