package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class DeleteGroupSelected extends GwtEvent<DeleteGroupSelected.DeleteGroupSelectedHandler> {

    public static interface DeleteGroupSelectedHandler extends EventHandler {
        void onDeleteGroupSelected(DeleteGroupSelected event);
    }

    public interface HasDeleteGroupSelectedHandlers {
        HandlerRegistration addDeleteGroupSelectedHandler(DeleteGroupSelectedHandler handler);
    }

    public static Type<DeleteGroupSelectedHandler> TYPE = new Type<DeleteGroupSelectedHandler>();
    private Group group;

    public DeleteGroupSelected(Group group) {
        this.group = group;
    }

    public Type<DeleteGroupSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteGroupSelectedHandler handler) {
        handler.onDeleteGroupSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
