package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 *
 * A GWT event that gets fired when the user selects a community
 */
public class CommunitySelectionChanged extends GwtEvent<CommunitySelectionChanged.CommunitySelectionChangedHandler> {

    public interface CommunitySelectionChangedHandler extends EventHandler {
        void onCommunitySelectionChanged(CommunitySelectionChanged event);
    }

    public interface HasCommunitySelectionChangedHandlers {
        HandlerRegistration addCommunitySelectionChangedHandler(CommunitySelectionChangedHandler handler);
    }

    public static Type<CommunitySelectionChangedHandler> TYPE = new Type<CommunitySelectionChangedHandler>();
    private Group community;

    public CommunitySelectionChanged(Group community) {
        this.community = community;
    }

    public Type<CommunitySelectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CommunitySelectionChangedHandler handler) {
        handler.onCommunitySelectionChanged(this);
    }

    public Group getCommunity() {
        return community;
    }
}
