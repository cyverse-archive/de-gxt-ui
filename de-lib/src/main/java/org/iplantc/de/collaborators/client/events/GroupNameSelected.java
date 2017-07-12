package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class GroupNameSelected extends GwtEvent<GroupNameSelected.GroupNameSelectedHandler> {

    public static interface GroupNameSelectedHandler extends EventHandler {
        void onGroupNameSelected(GroupNameSelected event);
    }

    public interface HasGroupNameSelectedHandlers {
        HandlerRegistration addGroupNameSelectedHandler(GroupNameSelectedHandler handler);
    }

    public static Type<GroupNameSelectedHandler> TYPE = new Type<GroupNameSelectedHandler>();
    private Subject subject;

    public GroupNameSelected(Subject subject) {
        this.subject = subject;
    }

    public Type<GroupNameSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(GroupNameSelectedHandler handler) {
        handler.onGroupNameSelected(this);
    }

    public Subject getSubject() {
        return subject;
    }
}
