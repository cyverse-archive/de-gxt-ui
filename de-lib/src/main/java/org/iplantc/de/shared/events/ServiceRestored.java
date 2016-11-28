package org.iplantc.de.shared.events;

import org.iplantc.de.client.models.WindowType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author aramsey
 */
public class ServiceRestored extends GwtEvent<ServiceRestored.ServiceRestoredHandler> {
    public static Type<ServiceRestoredHandler> TYPE = new Type<ServiceRestoredHandler>();
    private WindowType windowType;

    public ServiceRestored(WindowType windowType) {
        this.windowType = windowType;
    }

    public Type<ServiceRestoredHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ServiceRestoredHandler handler) {
        handler.onServiceRestored(this);
    }

    public WindowType getWindowType() {
        return windowType;
    }

    public static interface ServiceRestoredHandler extends EventHandler {
        void onServiceRestored(ServiceRestored event);
    }
}
