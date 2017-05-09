package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class GroupSaved extends GwtEvent<GroupSaved.GroupSavedHandler> {

    public static interface GroupSavedHandler extends EventHandler {
        void onGroupSaved(GroupSaved event);
    }

    public interface HasGroupSavedHandlers {
        HandlerRegistration addGroupSavedHandler(GroupSavedHandler handler);
    }

    public static Type<GroupSavedHandler> TYPE = new Type<GroupSavedHandler>();
    private List<Group> groups;

    public GroupSaved(List<Group> groups) {
        this.groups = groups;
    }

    public Type<GroupSavedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(GroupSavedHandler handler) {
        handler.onGroupSaved(this);
    }

    public List<Group> getGroups() {
        return groups;
    }
}
