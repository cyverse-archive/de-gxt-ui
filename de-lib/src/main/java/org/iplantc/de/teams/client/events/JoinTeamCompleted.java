package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class JoinTeamCompleted extends GwtEvent<JoinTeamCompleted.JoinTeamCompletedHandler> {
    public static interface JoinTeamCompletedHandler extends EventHandler {
        void onJoinTeamCompleted(JoinTeamCompleted event);
    }

    public interface HasJoinTeamCompletedHandlers {
        HandlerRegistration addJoinTeamCompletedHandler(JoinTeamCompletedHandler handler);
    }

    public static Type<JoinTeamCompletedHandler> TYPE = new Type<JoinTeamCompletedHandler>();
    private Group team;

    public JoinTeamCompleted(Group team){
        this.team = team;
    }

    public Type<JoinTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(JoinTeamCompletedHandler handler) {
        handler.onJoinTeamCompleted(this);
    }

    public Group getTeam() {
        return team;
    }
}
