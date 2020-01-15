package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that fires after a user has successfully joined a team because they had optin privileges
 */
public class JoinTeamCompleted extends GwtEvent<JoinTeamCompleted.JoinTeamCompletedHandler> {
    public interface JoinTeamCompletedHandler extends EventHandler {
        void onJoinTeamCompleted(JoinTeamCompleted event);
    }

    public interface HasJoinTeamCompletedHandlers {
        HandlerRegistration addJoinTeamCompletedHandler(JoinTeamCompletedHandler handler);
    }

    public static Type<JoinTeamCompletedHandler> TYPE = new Type<JoinTeamCompletedHandler>();

    public JoinTeamCompleted(){
    }

    public Type<JoinTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(JoinTeamCompletedHandler handler) {
        handler.onJoinTeamCompleted(this);
    }
}
