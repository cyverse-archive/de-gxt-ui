package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class TeamSaved extends GwtEvent<TeamSaved.TeamSavedHandler> {
    public static interface TeamSavedHandler extends EventHandler {
        void onTeamSaved(TeamSaved event);
    }

    public interface HasTeamSavedHandlers {
        HandlerRegistration addTeamSavedHandler(TeamSavedHandler handler);
    }

    public static Type<TeamSavedHandler> TYPE = new Type<TeamSavedHandler>();
    private Group group;

    public TeamSaved(Group group) {
        this.group = group;
    }

    public Type<TeamSavedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TeamSavedHandler handler) {
        handler.onTeamSaved(this);
    }

    public Group getGroup() {
        return group;
    }
}
