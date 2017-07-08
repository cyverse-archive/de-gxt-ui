package org.iplantc.de.teams.client.events;

import org.iplantc.de.teams.client.models.TeamsFilter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 *
 * @author aramsey
 */
public class TeamFilterSelectionChanged extends GwtEvent<TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler> {
    public static interface TeamFilterSelectionChangedHandler extends EventHandler {
        void onTeamFilterSelectionChanged(TeamFilterSelectionChanged event);
    }

    public interface HasTeamFilterSelectionChangedHandlers {
        HandlerRegistration addTeamFilterSelectionChangedHandler(TeamFilterSelectionChangedHandler handler);
    }

    public static Type<TeamFilterSelectionChangedHandler> TYPE = new Type<TeamFilterSelectionChangedHandler>();
    private TeamsFilter filter;

    public Type<TeamFilterSelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public TeamFilterSelectionChanged(TeamsFilter filter) {
        this.filter = filter;
    }

    protected void dispatch(TeamFilterSelectionChangedHandler handler) {
        handler.onTeamFilterSelectionChanged(this);
    }

    public TeamsFilter getFilter() {
        return filter;
    }
}
