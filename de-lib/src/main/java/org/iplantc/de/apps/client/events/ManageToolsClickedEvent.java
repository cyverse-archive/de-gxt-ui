package org.iplantc.de.apps.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 4/20/17.
 */
public class ManageToolsClickedEvent
        extends GwtEvent<ManageToolsClickedEvent.ManageToolsClickedEventHandler> {


    public static Type<ManageToolsClickedEventHandler> TYPE = new Type<>();

    @Override
    public Type<ManageToolsClickedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public interface HasManageToolsClickedEventHandlers {
        HandlerRegistration addManageToolsClickedEventHandler(ManageToolsClickedEventHandler handler);
    }

    @Override
    protected void dispatch(ManageToolsClickedEventHandler handler) {
        handler.onManageToolsClicked(this);
    }

    public interface ManageToolsClickedEventHandler extends EventHandler {
        void onManageToolsClicked(ManageToolsClickedEvent event);
    }


}
