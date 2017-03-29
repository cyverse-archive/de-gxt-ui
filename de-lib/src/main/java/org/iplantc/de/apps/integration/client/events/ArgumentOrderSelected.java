package org.iplantc.de.apps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class ArgumentOrderSelected extends GwtEvent<ArgumentOrderSelected.ArgumentOrderSelectedHandler> {

    public static interface ArgumentOrderSelectedHandler extends EventHandler {
        void onArgumentOrderSelected(ArgumentOrderSelected event);
    }

    public interface HasArgumentOrderSelectedHandlers {
        HandlerRegistration addArgumentOrderSelectedHandler(ArgumentOrderSelectedHandler handler);
    }

    public static Type<ArgumentOrderSelectedHandler> TYPE = new Type<ArgumentOrderSelectedHandler>();

    public Type<ArgumentOrderSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ArgumentOrderSelectedHandler handler) {
        handler.onArgumentOrderSelected(this);
    }
}
