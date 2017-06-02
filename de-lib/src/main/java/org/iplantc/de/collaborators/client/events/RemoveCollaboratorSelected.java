package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class RemoveCollaboratorSelected
        extends GwtEvent<RemoveCollaboratorSelected.RemoveCollaboratorSelectedHandler> {
    public static interface RemoveCollaboratorSelectedHandler extends EventHandler {
        void onRemoveCollaboratorSelected(RemoveCollaboratorSelected event);
    }

    public interface HasRemoveCollaboratorSelectedHandlers {
        HandlerRegistration addRemoveCollaboratorSelectedHandler(RemoveCollaboratorSelectedHandler handler);
    }

    public static Type<RemoveCollaboratorSelectedHandler> TYPE =
            new Type<RemoveCollaboratorSelectedHandler>();
    private List<Subject> subjects;

    public RemoveCollaboratorSelected(List<Subject> subjects) {

        this.subjects = subjects;
    }

    public Type<RemoveCollaboratorSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveCollaboratorSelectedHandler handler) {
        handler.onRemoveCollaboratorSelected(this);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
