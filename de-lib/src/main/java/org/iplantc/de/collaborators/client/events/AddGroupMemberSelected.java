package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AddGroupMemberSelected
        extends GwtEvent<AddGroupMemberSelected.AddGroupMemberSelectedHandler> {

    public static interface AddGroupMemberSelectedHandler extends EventHandler {
        void onAddGroupMemberSelected(AddGroupMemberSelected event);
    }

    public interface HasAddGroupMemberSelectedHandlers {
        HandlerRegistration addAddGroupMemberSelectedHandler(AddGroupMemberSelectedHandler handler);
    }

    public static Type<AddGroupMemberSelectedHandler> TYPE = new Type<AddGroupMemberSelectedHandler>();
    private Group group;
    private Subject subject;

    public AddGroupMemberSelected(Group group, Subject subject) {
        this.group = group;
        this.subject = subject;
    }

    public Type<AddGroupMemberSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddGroupMemberSelectedHandler handler) {
        handler.onAddGroupMemberSelected(this);
    }

    public Group getGroup() {
        return group;
    }

    public Subject getSubject() {
        return subject;
    }
}
