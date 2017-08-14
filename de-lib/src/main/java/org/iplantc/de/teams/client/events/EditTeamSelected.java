package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class EditTeamSelected extends GwtEvent<EditTeamSelected.EditTeamSelectedHandler> {
    public static interface EditTeamSelectedHandler extends EventHandler {
        void onEditTeamSelected(EditTeamSelected event);
    }

    public interface HasEditTeamSelectedHandlers {
        HandlerRegistration addEditTeamSelectedHandler(EditTeamSelectedHandler handler);
    }

    public static Type<EditTeamSelectedHandler> TYPE = new Type<EditTeamSelectedHandler>();
    private Group group;

    public EditTeamSelected(Group group){
        this.group = group;
    }

    public Type<EditTeamSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(EditTeamSelectedHandler handler) {
        handler.onEditTeamSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
