package org.iplantc.de.tools.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 */
public class UseInNewAppEvent extends GwtEvent<UseInNewAppEvent.UseInNewAppEventHandler> {

    public interface UseInNewAppEventHandler extends EventHandler {
        void createNewApp(UseInNewAppEvent event);
    }

    public static final Type<UseInNewAppEventHandler> TYPE = new Type<>();

    @Override
    public Type<UseInNewAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UseInNewAppEventHandler handler) {
        handler.createNewApp(this);
    }

}

