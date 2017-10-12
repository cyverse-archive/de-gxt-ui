package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the "Automate HT path list file creation" button in the
 * Data window
 */
public class AutomateHTPathListSelected
        extends GwtEvent<AutomateHTPathListSelected.AutomateHTPathListSelectedHandler> {
    public static interface AutomateHTPathListSelectedHandler extends EventHandler {
        void onAutomateHTPathListSelected(AutomateHTPathListSelected event);
    }

    public interface HasAutomateHTPathListSelectedHandlers {
        HandlerRegistration addAutomateHTPathListSelectedHandler(AutomateHTPathListSelectedHandler handler);
    }

    public static Type<AutomateHTPathListSelectedHandler> TYPE =
            new Type<AutomateHTPathListSelectedHandler>();

    public Type<AutomateHTPathListSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AutomateHTPathListSelectedHandler handler) {
        handler.onAutomateHTPathListSelected(this);
    }
}
