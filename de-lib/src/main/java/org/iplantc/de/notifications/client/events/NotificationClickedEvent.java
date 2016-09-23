package org.iplantc.de.notifications.client.events;

import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.notifications.client.events.NotificationClickedEvent.NotificationClickedEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;



/**
 * Created by sriram on 9/20/16.
 */
public class NotificationClickedEvent extends GwtEvent<NotificationClickedEventHandler> {

    public interface NotificationClickedEventHandler extends EventHandler {
          void onNotificationClicked(NotificationClickedEvent event);
    }

    public NotificationMessage getMessage() {
        return message;
    }

    private NotificationMessage message;

    public NotificationClickedEvent(NotificationMessage message) {
       this.message = message;
    }

    public static final GwtEvent.Type<NotificationClickedEventHandler> TYPE = new GwtEvent.Type<NotificationClickedEventHandler>();


    @Override
    public Type<NotificationClickedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NotificationClickedEventHandler handler) {
        handler.onNotificationClicked(this);
    }

}
