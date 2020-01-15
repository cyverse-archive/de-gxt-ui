package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class TeamSaved extends GwtEvent<TeamSaved.TeamSavedHandler> {
    public interface TeamSavedHandler extends EventHandler {
        void onTeamSaved(TeamSaved event);
    }

    public interface HasTeamSavedHandlers {
        HandlerRegistration addTeamSavedHandler(TeamSavedHandler handler);
    }

    public static Type<TeamSavedHandler> TYPE = new Type<TeamSavedHandler>();

    public TeamSaved() {
    }

    public Type<TeamSavedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(TeamSavedHandler handler) {
        handler.onTeamSaved(this);
    }
}
