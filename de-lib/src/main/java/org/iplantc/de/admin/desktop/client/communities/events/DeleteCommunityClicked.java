package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 *
 * A GWT event that fires when the user clicks the Delete button in the Community tab
 */
public class DeleteCommunityClicked extends GwtEvent<DeleteCommunityClicked.DeleteCommunityClickedHandler> {
    public interface DeleteCommunityClickedHandler extends EventHandler {
        void onDeleteCommunityClicked(DeleteCommunityClicked event);
    }

    public interface HasDeleteCommunityClickedHandlers {
        HandlerRegistration addDeleteCommunityClickedHandler(DeleteCommunityClickedHandler handler);
    }

    public static Type<DeleteCommunityClickedHandler> TYPE = new Type<DeleteCommunityClickedHandler>();
    private Group selectedCommunity;

    public Type<DeleteCommunityClickedHandler> getAssociatedType() {
        return TYPE;
    }

    public DeleteCommunityClicked(Group selectedCommunity) {
        this.selectedCommunity = selectedCommunity;
    }

    protected void dispatch(DeleteCommunityClickedHandler handler) {
        handler.onDeleteCommunityClicked(this);
    }

    public Group getSelectedCommunity() {
        return selectedCommunity;
    }
}
