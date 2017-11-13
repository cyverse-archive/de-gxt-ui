package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the New Data Window button in the Data window
 */
public class OpenNewWindowSelected extends GwtEvent<OpenNewWindowSelected.OpenNewWindowSelectedHandler> {
    public interface OpenNewWindowSelectedHandler extends EventHandler {
        void onOpenNewWindowSelected(OpenNewWindowSelected event);
    }

    public interface HasOpenNewWindowSelectedHandlers {
        HandlerRegistration addOpenNewWindowSelectedHandler(OpenNewWindowSelectedHandler handler);
    }

    public static Type<OpenNewWindowSelectedHandler> TYPE = new Type<OpenNewWindowSelectedHandler>();

    public Type<OpenNewWindowSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(OpenNewWindowSelectedHandler handler) {
        handler.onOpenNewWindowSelected(this);
    }
}
