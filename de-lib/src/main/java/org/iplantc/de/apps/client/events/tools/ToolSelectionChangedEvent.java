/**
 * 
 */
package org.iplantc.de.apps.client.events.tools;

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

    public static interface HasToolSelectionChangedEventHandlers {
        HandlerRegistration addToolSelectionChangedEventHandler(ToolSelectionChangedEventHandler handler);
    }

    public static final Type<ToolSelectionChangedEventHandler> TYPE = new Type<>();

    private final List<Tool> ToolSelection;

    public ToolSelectionChangedEvent(final List<Tool> ToolSelection) {
        Preconditions.checkNotNull(ToolSelection);
        this.ToolSelection = ToolSelection;
    }

    public List<Tool> getToolSelection() {
        return ToolSelection;
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
