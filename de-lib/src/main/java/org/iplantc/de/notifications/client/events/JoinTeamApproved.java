package org.iplantc.de.notifications.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that gets fired when the user approves another user's request to join a team
 */
public class JoinTeamApproved extends GwtEvent<JoinTeamApproved.JoinTeamApprovedHandler> {
    public static interface JoinTeamApprovedHandler extends EventHandler {
        void onJoinTeamApproved(JoinTeamApproved event);
    }

    public interface HasJoinTeamApprovedHandlers {
        HandlerRegistration addJoinTeamApprovedHandler(JoinTeamApprovedHandler handler);
    }

    public static Type<JoinTeamApprovedHandler> TYPE = new Type<JoinTeamApprovedHandler>();

    public Type<JoinTeamApprovedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(JoinTeamApprovedHandler handler) {
        handler.onJoinTeamApproved(this);
    }
}
