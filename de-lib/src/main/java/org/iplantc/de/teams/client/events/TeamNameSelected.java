package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class TeamNameSelected extends GwtEvent<TeamNameSelected.TeamNameSelectedHandler> {

    public static interface TeamNameSelectedHandler extends EventHandler {
        void onTeamNameSelected(TeamNameSelected event);
    }

    public interface HasTeamNameSelectedHandlers {
        HandlerRegistration addTeamNameSelectedHandler(TeamNameSelectedHandler handler);
    }

    public static Type<TeamNameSelectedHandler> TYPE = new Type<TeamNameSelectedHandler>();
    private Group group;

    public TeamNameSelected(Group group) {
        this.group = group;
    }

    public Type<TeamNameSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TeamNameSelectedHandler handler) {
        handler.onTeamNameSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
