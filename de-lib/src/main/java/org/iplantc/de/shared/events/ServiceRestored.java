package org.iplantc.de.shared.events;

import org.iplantc.de.client.models.WindowType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class ServiceRestored extends GwtEvent<ServiceRestored.ServiceRestoredHandler> {
    public static Type<ServiceRestoredHandler> TYPE = new Type<ServiceRestoredHandler>();
    private List<WindowType> windowTypes;

    public ServiceRestored(List<WindowType> windowTypes) {
        this.windowTypes = windowTypes;
    }

    public Type<ServiceRestoredHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ServiceRestoredHandler handler) {
        handler.onServiceRestored(this);
    }

    public List<WindowType> getWindowTypes() {
        return windowTypes;
    }

    public static interface ServiceRestoredHandler extends EventHandler {
        void onServiceRestored(ServiceRestored event);
    }
}
