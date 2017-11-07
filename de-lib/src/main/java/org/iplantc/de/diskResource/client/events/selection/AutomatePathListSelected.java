package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.viewer.InfoType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the "Automate path list file creation" button in the
 * Data window
 */
public class AutomatePathListSelected
        extends GwtEvent<AutomatePathListSelected.AutomatePathListSelectedHandler> {

    private final InfoType pathListType;

    public InfoType getPathListType() {
        return pathListType;
    }

    public interface AutomatePathListSelectedHandler extends EventHandler {
        void onAutomatePathListSelected(AutomatePathListSelected event);
    }

    public interface HasAutomatePathListSelectedHandlers {
        HandlerRegistration addAutomatePathListSelectedHandler(AutomatePathListSelectedHandler handler);
    }

    public static Type<AutomatePathListSelectedHandler> TYPE =
            new Type<AutomatePathListSelectedHandler>();

    public AutomatePathListSelected(InfoType pathListType) {
        this.pathListType = pathListType;
    }

    public Type<AutomatePathListSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AutomatePathListSelectedHandler handler) {
        handler.onAutomatePathListSelected(this);
    }
}
