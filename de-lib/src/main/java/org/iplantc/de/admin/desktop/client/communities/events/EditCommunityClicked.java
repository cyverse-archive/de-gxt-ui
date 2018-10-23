package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT even that fires when the user clicks the Edit Community button
 */
public class EditCommunityClicked extends GwtEvent<EditCommunityClicked.EditCommunityClickedHandler> {
    public interface EditCommunityClickedHandler extends EventHandler {
        void onEditCommunityClicked(EditCommunityClicked event);
    }

    public interface HasEditCommunityClickedHandlers {
        HandlerRegistration addEditCommunityClickedHandler(EditCommunityClickedHandler handler);
    }

    public static Type<EditCommunityClickedHandler> TYPE = new Type<EditCommunityClickedHandler>();
    private Group community;

    public Type<EditCommunityClickedHandler> getAssociatedType() {
        return TYPE;
    }


    public EditCommunityClicked(Group community) {
        this.community = community;
    }

    protected void dispatch(EditCommunityClickedHandler handler) {
        handler.onEditCommunityClicked(this);
    }

    public Group getCommunity() {
        return community;
    }
}
