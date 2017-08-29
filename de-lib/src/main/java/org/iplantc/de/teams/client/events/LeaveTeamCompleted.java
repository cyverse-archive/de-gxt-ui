package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that fires if the user has successfully left a team
 */
public class LeaveTeamCompleted extends GwtEvent<LeaveTeamCompleted.LeaveTeamCompletedHandler> {
    public static interface LeaveTeamCompletedHandler extends EventHandler {
        void onLeaveTeamCompleted(LeaveTeamCompleted event);
    }

    public interface HasLeaveTeamCompletedHandlers {
        HandlerRegistration addLeaveTeamCompletedHandler(LeaveTeamCompletedHandler handler);
    }

    public static Type<LeaveTeamCompletedHandler> TYPE = new Type<LeaveTeamCompletedHandler>();
    private Group team;

    public LeaveTeamCompleted(Group team) {
        this.team = team;
    }

    public Type<LeaveTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(LeaveTeamCompletedHandler handler) {
        handler.onLeaveTeamCompleted(this);
    }

    public Group getTeam() {
        return team;
    }
}
