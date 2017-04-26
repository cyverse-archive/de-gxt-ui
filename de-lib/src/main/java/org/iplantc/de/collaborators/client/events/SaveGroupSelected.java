package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class SaveGroupSelected extends GwtEvent<SaveGroupSelected.SaveGroupSelectedHandler> {

    public static interface SaveGroupSelectedHandler extends EventHandler {
        void onSaveGroupSelected(SaveGroupSelected event);
    }

    public interface HasSaveGroupSelectedHandlers {
        HandlerRegistration addSaveGroupSelectedHandler(SaveGroupSelectedHandler handler);
    }

    public static Type<SaveGroupSelectedHandler> TYPE = new Type<SaveGroupSelectedHandler>();
    private Group group;
    private List<Collaborator> collaboratorList;

    public SaveGroupSelected(Group group, List<Collaborator> collaboratorList) {
        this.group = group;
        this.collaboratorList = collaboratorList;
    }

    public Type<SaveGroupSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SaveGroupSelectedHandler handler) {
        handler.onSaveGroupSelected(this);
    }

    public Group getGroup() {
        return group;
    }

    public List<Collaborator> getCollaboratorList() {
        return collaboratorList;
    }
}
