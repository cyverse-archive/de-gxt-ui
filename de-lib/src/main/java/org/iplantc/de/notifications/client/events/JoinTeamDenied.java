package org.iplantc.de.notifications.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that is fired when a user denies another user's request to join a team
 */
public class JoinTeamDenied extends GwtEvent<JoinTeamDenied.JoinTeamDeniedHandler> {
    public static interface JoinTeamDeniedHandler extends EventHandler {
        void onJoinTeamDenied(JoinTeamDenied event);
    }

    public interface HasJoinTeamDeniedHandlers {
        HandlerRegistration addJoinTeamDeniedHandler(JoinTeamDeniedHandler handler);
    }

    public static Type<JoinTeamDeniedHandler> TYPE = new Type<JoinTeamDeniedHandler>();

    public Type<JoinTeamDeniedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(JoinTeamDeniedHandler handler) {
        handler.onJoinTeamDenied(this);
    }
}
