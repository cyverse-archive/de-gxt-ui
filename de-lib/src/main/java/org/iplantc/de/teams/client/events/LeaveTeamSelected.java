package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class LeaveTeamSelected extends GwtEvent<LeaveTeamSelected.LeaveTeamSelectedHandler> {

    public static interface LeaveTeamSelectedHandler extends EventHandler {
        void onLeaveTeamSelected(LeaveTeamSelected event);
    }

    public interface HasLeaveTeamSelectedHandlers {
        HandlerRegistration addLeaveTeamSelectedHandler(LeaveTeamSelectedHandler handler);
    }

    public static Type<LeaveTeamSelectedHandler> TYPE = new Type<LeaveTeamSelectedHandler>();
    private Group group;

    public Type<LeaveTeamSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public LeaveTeamSelected(Group group) {
        this.group = group;
    }

    protected void dispatch(LeaveTeamSelectedHandler handler) {
        handler.onLeaveTeamSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
