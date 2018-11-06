package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 *
 * A GWT event that fires when the user selects a user to be added as a community admin
 */
public class AddCommunityAdminSelected extends GwtEvent<AddCommunityAdminSelected.AddCommunityAdminSelectedHandler> {

    public interface AddCommunityAdminSelectedHandler extends EventHandler {
        void onAddCommunityAdminSelected(AddCommunityAdminSelected event);
    }

    public interface HasAddCommunityAdminSelectedHandlers {
        HandlerRegistration addAddCommunityAdminSelectedHandler(AddCommunityAdminSelectedHandler handler);
    }

    public static Type<AddCommunityAdminSelectedHandler> TYPE = new Type<AddCommunityAdminSelectedHandler>();
    private Group community;
    private Subject admin;

    public Type<AddCommunityAdminSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public AddCommunityAdminSelected(Group community,
                                     Subject admin) {
        this.community = community;
        this.admin = admin;
    }

    protected void dispatch(AddCommunityAdminSelectedHandler handler) {
        handler.onAddCommunityAdminSelected(this);
    }

    public Group getCommunity() {
        return community;
    }

    public Subject getAdmin() {
        return admin;
    }
}
