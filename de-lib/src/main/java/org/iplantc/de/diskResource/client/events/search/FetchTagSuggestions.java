package org.iplantc.de.diskResource.client.events.search;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that gets fired when the user has typed enough characters into the SearchView to
 * start a search for suggested tags
 */
public class FetchTagSuggestions extends GwtEvent<FetchTagSuggestions.FetchTagSuggestionsHandler> {

    public interface FetchTagSuggestionsHandler extends EventHandler {
        void onFetchTagSuggestions(FetchTagSuggestions event);
    }

    public interface HasFetchTagSuggestionsHandlers {
        HandlerRegistration addFetchTagSuggestionsHandler(FetchTagSuggestionsHandler handler);
    }

    private String searchTerm;

    public String getSearchTerm() {
        return searchTerm;
    }

    public FetchTagSuggestions(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public static Type<FetchTagSuggestionsHandler> TYPE = new Type<FetchTagSuggestionsHandler>();

    public Type<FetchTagSuggestionsHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(FetchTagSuggestionsHandler handler) {
        handler.onFetchTagSuggestions(this);
    }
}
