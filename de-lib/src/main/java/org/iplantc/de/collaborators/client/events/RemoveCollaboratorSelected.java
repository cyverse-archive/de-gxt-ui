package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Collaborator;

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
    private List<Collaborator> collaborators;

    public RemoveCollaboratorSelected(List<Collaborator> collaborators) {

        this.collaborators = collaborators;
    }

    public Type<RemoveCollaboratorSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveCollaboratorSelectedHandler handler) {
        handler.onRemoveCollaboratorSelected(this);
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }
}
