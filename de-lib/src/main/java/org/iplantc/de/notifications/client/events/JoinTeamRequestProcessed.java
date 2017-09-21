package org.iplantc.de.notifications.client.events;

import org.iplantc.de.client.models.notifications.NotificationMessage;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that will fire once an admin has approved or denied a team join request
 */
public class JoinTeamRequestProcessed
        extends GwtEvent<JoinTeamRequestProcessed.JoinTeamRequestProcessedHandler> {
    public static interface JoinTeamRequestProcessedHandler extends EventHandler {
        void onJoinTeamRequestProcessed(JoinTeamRequestProcessed event);
    }

    public interface HasJoinTeamRequestProcessedHandlers {
        HandlerRegistration addJoinTeamRequestProcessedHandler(JoinTeamRequestProcessedHandler handler);
    }

    public static Type<JoinTeamRequestProcessedHandler> TYPE =
            new Type<JoinTeamRequestProcessedHandler>();

    private NotificationMessage message;

    public JoinTeamRequestProcessed(NotificationMessage message) {
        this.message = message;
    }

    public Type<JoinTeamRequestProcessedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(JoinTeamRequestProcessedHandler handler) {
        handler.onJoinTeamRequestProcessed(this);
    }

    public NotificationMessage getMessage() {
        return message;
    }
}
