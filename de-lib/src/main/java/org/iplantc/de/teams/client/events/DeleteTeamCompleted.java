package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DeleteTeamCompleted extends GwtEvent<DeleteTeamCompleted.DeleteTeamCompletedHandler> {
    public static interface DeleteTeamCompletedHandler extends EventHandler {
        void onDeleteTeamCompleted(DeleteTeamCompleted event);
    }

    public interface HasDeleteTeamCompletedHandlers {
        HandlerRegistration addDeleteTeamCompletedHandler(DeleteTeamCompletedHandler handler);
    }

    public static Type<DeleteTeamCompletedHandler> TYPE = new Type<DeleteTeamCompletedHandler>();
    private Group team;

    public DeleteTeamCompleted(Group team) {
        this.team = team;
    }

    public Type<DeleteTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteTeamCompletedHandler handler) {
        handler.onDeleteTeamCompleted(this);
    }

    public Group getTeam() {
        return team;
    }
}
