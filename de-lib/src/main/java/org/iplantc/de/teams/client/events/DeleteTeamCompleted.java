package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DeleteTeamCompleted extends GwtEvent<DeleteTeamCompleted.DeleteTeamCompletedHandler> {
    public interface DeleteTeamCompletedHandler extends EventHandler {
        void onDeleteTeamCompleted(DeleteTeamCompleted event);
    }

    public interface HasDeleteTeamCompletedHandlers {
        HandlerRegistration addDeleteTeamCompletedHandler(DeleteTeamCompletedHandler handler);
    }

    public static Type<DeleteTeamCompletedHandler> TYPE = new Type<DeleteTeamCompletedHandler>();

    public DeleteTeamCompleted() {
    }

    public Type<DeleteTeamCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteTeamCompletedHandler handler) {
        handler.onDeleteTeamCompleted(this);
    }

}
