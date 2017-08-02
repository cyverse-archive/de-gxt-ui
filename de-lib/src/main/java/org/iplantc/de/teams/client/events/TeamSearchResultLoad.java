package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class TeamSearchResultLoad extends GwtEvent<TeamSearchResultLoad.TeamSearchResultLoadHandler> {

    public static interface TeamSearchResultLoadHandler extends EventHandler {
        void onTeamSearchResultLoad(TeamSearchResultLoad event);
    }

    public interface HasTeamSearchResultLoadHandlers {
        HandlerRegistration addTeamSearchResultLoadHandler(TeamSearchResultLoadHandler handler);
    }

    public static Type<TeamSearchResultLoadHandler> TYPE = new Type<TeamSearchResultLoadHandler>();
    private List<Group> searchResults;

    public Type<TeamSearchResultLoadHandler> getAssociatedType() {
        return TYPE;
    }

    public TeamSearchResultLoad(List<Group> searchResults){
        this.searchResults = searchResults;
    }

    protected void dispatch(TeamSearchResultLoadHandler handler) {
        handler.onTeamSearchResultLoad(this);
    }

    public List<Group> getSearchResults() {
        return searchResults;
    }
}
