package org.iplantc.de.notifications.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 9/20/16.
 */
public class NotificationToolbarMarkAsSeenClickedEvent extends GwtEvent<NotificationToolbarMarkAsSeenClickedEvent.NotificationToolbarMarkAsSeenClickedEventHandler> {
    public static Type<NotificationToolbarMarkAsSeenClickedEventHandler> TYPE =
            new Type<NotificationToolbarMarkAsSeenClickedEventHandler>();

    public Type<NotificationToolbarMarkAsSeenClickedEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(NotificationToolbarMarkAsSeenClickedEventHandler handler) {
        handler.onNotificationToolbarMarkAsSeenClicked(this);
    }

    public static interface NotificationToolbarMarkAsSeenClickedEventHandler extends EventHandler {
        void onNotificationToolbarMarkAsSeenClicked(NotificationToolbarMarkAsSeenClickedEvent event);
    }

    public interface HasNotificationToolbarMarkAsSeenClickedEventHandlers {
        HandlerRegistration addNotificationToolbarMarkAsSeenClickedEventHandler(NotificationToolbarMarkAsSeenClickedEventHandler handler);
    }
}
