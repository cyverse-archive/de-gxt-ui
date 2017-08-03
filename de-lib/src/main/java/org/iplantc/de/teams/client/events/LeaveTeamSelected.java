package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class LeaveTeamSelected extends GwtEvent<LeaveTeamSelectedHandler> {
    public static Type<LeaveTeamSelectedHandler> TYPE = new Type<LeaveTeamSelectedHandler>();

    public Type<LeaveTeamSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(LeaveTeamSelectedHandler handler) {
        handler.onLeaveTeamSelected(this);
    }
}
