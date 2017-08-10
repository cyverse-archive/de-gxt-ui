package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class CreateTeamSelected extends GwtEvent<CreateTeamSelected.CreateTeamSelectedHandler> {
    public static interface CreateTeamSelectedHandler extends EventHandler {
        void onCreateTeamSelected(CreateTeamSelected event);
    }

    public interface HasCreateTeamSelectedHandlers {
        HandlerRegistration addCreateTeamSelectedHandler(CreateTeamSelectedHandler handler);
    }

    public static Type<CreateTeamSelectedHandler> TYPE = new Type<CreateTeamSelectedHandler>();

    public Type<CreateTeamSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateTeamSelectedHandler handler) {
        handler.onCreateTeamSelected(this);
    }
}
