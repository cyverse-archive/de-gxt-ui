package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that fires if the user has successfully left a team
 */
public class LeaveTeamCompleted extends GwtEvent<LeaveTeamCompleted.LeaveTeamCompletedHandler> {
    public interface LeaveTeamCompletedHandler extends EventHandler {
        void onLeaveTeamCompleted(LeaveTeamCompleted event);
    }

    public interface HasLeaveTeamCompletedHandlers {
        HandlerRegistration addLeaveTeamCompletedHandler(LeaveTeamCompletedHandler handler);
    }

    public static Type<LeaveTeamCompletedHandler> TYPE = new Type<LeaveTeamCompletedHandler>();

    public LeaveTeamCompleted() {
    }

    public Type<LeaveTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(LeaveTeamCompletedHandler handler) {
        handler.onLeaveTeamCompleted(this);
    }
}
