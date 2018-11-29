package org.iplantc.de.admin.desktop.client.communities.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user clicks the Add Community button
 */
public class AddCommunityClicked extends GwtEvent<AddCommunityClicked.AddCommunityClickedHandler> {
    public interface AddCommunityClickedHandler extends EventHandler {
        void onAddCommunityClicked(AddCommunityClicked event);
    }

    public interface HasAddCommunityClickedHandlers {
        HandlerRegistration addAddCommunityClickedHandler(AddCommunityClickedHandler handler);
    }

    public static Type<AddCommunityClickedHandler> TYPE = new Type<AddCommunityClickedHandler>();

    public Type<AddCommunityClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddCommunityClickedHandler handler) {
        handler.onAddCommunityClicked(this);
    }


}
