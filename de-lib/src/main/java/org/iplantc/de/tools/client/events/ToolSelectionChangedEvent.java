/**
 * 
 */
package org.iplantc.de.tools.client.events;

import org.iplantc.de.client.models.tool.Tool;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author sriram
 *
 */
public class ToolSelectionChangedEvent extends GwtEvent<ToolSelectionChangedEvent.ToolSelectionChangedEventHandler> {

    public interface ToolSelectionChangedEventHandler extends EventHandler {
        void onToolSelectionChanged(ToolSelectionChangedEvent event);
    }

    public interface HasToolSelectionChangedEventHandlers {
        HandlerRegistration addToolSelectionChangedEventHandler(ToolSelectionChangedEventHandler handler);
    }

    public static final Type<ToolSelectionChangedEventHandler> TYPE = new Type<>();

    private final Tool tool;

    public ToolSelectionChangedEvent(final Tool tool) {
        Preconditions.checkNotNull(tool);
        this.tool = tool;
    }

    public Tool getTool() {
        return tool;
    }

    @Override
    public Type<ToolSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ToolSelectionChangedEventHandler handler) {
        handler.onToolSelectionChanged(this);
    }

}
