package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author aramsey
 */
public class GroupNameSelected extends GwtEvent<GroupNameSelected.GroupNameSelectedHandler> {

    public static interface GroupNameSelectedHandler extends EventHandler {
        void onGroupNameSelected(GroupNameSelected event);
    }

    public static Type<GroupNameSelectedHandler> TYPE = new Type<GroupNameSelectedHandler>();
    private Group group;

    public GroupNameSelected(Group group) {
        this.group = group;
    }

    public Type<GroupNameSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(GroupNameSelectedHandler handler) {
        handler.onGroupNameSelected(this);
    }

    public Group getGroup() {
        return group;
    }
}
