package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class DeleteMembersSelected extends GwtEvent<DeleteMembersSelected.DeleteMembersSelectedHandler> {

    public static interface DeleteMembersSelectedHandler extends EventHandler {
        void onDeleteMembersSelected(DeleteMembersSelected event);
    }

    public interface HasDeleteMembersSelectedHandlers {
        HandlerRegistration addDeleteMembersSelectedHandler(DeleteMembersSelectedHandler handler);
    }

    public static Type<DeleteMembersSelectedHandler> TYPE = new Type<DeleteMembersSelectedHandler>();
    private Group group;
    private List<Subject> subjects;

    public DeleteMembersSelected(Group group, List<Subject> subjects) {
        this.group = group;
        this.subjects = subjects;
    }

    public Group getGroup() {
        return group;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public Type<DeleteMembersSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteMembersSelectedHandler handler) {
        handler.onDeleteMembersSelected(this);
    }
}
