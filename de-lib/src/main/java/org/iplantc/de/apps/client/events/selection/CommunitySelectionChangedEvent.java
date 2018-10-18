package org.iplantc.de.apps.client.events.selection;

import org.iplantc.de.client.models.groups.Group;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 *
 * A GWT event that fires when the user has selected a different community in the Apps window
 */
public class CommunitySelectionChangedEvent extends GwtEvent<CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler> {

    public interface CommunitySelectionChangedEventHandler extends EventHandler {
        void onCommunitySelectionChanged(CommunitySelectionChangedEvent event);
    }

    public interface HasCommunitySelectionChangedEventHandlers {
        HandlerRegistration addCommunitySelectedEventHandler(CommunitySelectionChangedEventHandler handler);
    }

    public static final Type<CommunitySelectionChangedEventHandler> TYPE = new Type<>();

    private final List<Group> communitySelection;
    private List<String> path;

    public CommunitySelectionChangedEvent(final List<Group> communitySelection,
                                          List<String> path) {
        this.path = path;
        Preconditions.checkNotNull(communitySelection);
        this.communitySelection = communitySelection;
    }

    public List<Group> getCommunitySelection() {
        return communitySelection;
    }

    @Override
    public Type<CommunitySelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CommunitySelectionChangedEventHandler handler) {
        handler.onCommunitySelectionChanged(this);
    }

    public List<String> getPath() {
        return path;
    }
}
