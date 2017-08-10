package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class TeamInfoButtonSelected extends GwtEvent<TeamInfoButtonSelected.TeamInfoButtonSelectedHandler> {

    public static interface TeamInfoButtonSelectedHandler extends EventHandler {
        void onTeamInfoButtonSelected(TeamInfoButtonSelected event);
    }

    public interface HasTeamInfoButtonSelectedHandlers {
        HandlerRegistration addTeamInfoButtonSelectedHandler(TeamInfoButtonSelectedHandler handler);
    }

    public static Type<TeamInfoButtonSelectedHandler> TYPE = new Type<TeamInfoButtonSelectedHandler>();
    private Group group;

    public TeamInfoButtonSelected(Group group) {
        this.group = group;
    }

    public Type<TeamInfoButtonSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TeamInfoButtonSelectedHandler handler) {
        handler.onTeamInfoButtonSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
