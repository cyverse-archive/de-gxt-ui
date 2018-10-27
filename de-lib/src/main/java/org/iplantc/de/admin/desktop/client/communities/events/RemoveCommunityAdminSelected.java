package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 *
 * A GWT event that fires when the user selects to remove a community admin
 */
public class RemoveCommunityAdminSelected extends GwtEvent<RemoveCommunityAdminSelected.RemoveCommunityAdminSelectedHandler> {
    public interface RemoveCommunityAdminSelectedHandler extends EventHandler {
        void onRemoveAdminSelected(RemoveCommunityAdminSelected event);
    }

    public interface HasRemoveCommunityAdminSelectedHandlers {
        HandlerRegistration addRemoveCommunityAdminSelectedHandler(RemoveCommunityAdminSelectedHandler handler);
    }

    private Group community;
    private Subject admin;

    public RemoveCommunityAdminSelected(Group community, Subject admin) {
        this.community = community;
        this.admin = admin;
    }

    public static Type<RemoveCommunityAdminSelectedHandler> TYPE = new Type<RemoveCommunityAdminSelectedHandler>();

    public Type<RemoveCommunityAdminSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveCommunityAdminSelectedHandler handler) {
        handler.onRemoveAdminSelected(this);
    }

    public Subject getAdmin() {
        return admin;
    }

    public Group getCommunity() {
        return community;
    }
}
