package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;

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
    private Subject group;

    public DeleteGroupSelected(Subject group) {
        this.group = group;
    }

    public Type<DeleteGroupSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteGroupSelectedHandler handler) {
        handler.onDeleteGroupSelected(this);
    }

    public Subject getGroup() {
        return group;
    }
}
