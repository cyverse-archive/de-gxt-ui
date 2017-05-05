package org.iplantc.de.tools.client.events;

import org.iplantc.de.client.models.tool.Tool;
import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * A GwtEvent used to notify listeners that Tool search results have been loaded from the search service.
 * 
 * @author sriram
 * 
 */
public class ToolSearchResultLoadEvent extends GwtEvent<ToolSearchResultLoadEvent.ToolSearchResultLoadEventHandler> {

    public interface ToolSearchResultLoadEventHandler extends EventHandler {

        void onToolSearchResultLoad(ToolSearchResultLoadEvent event);
    }

    public static interface HasToolSearchResultLoadEventHandlers {
        HandlerRegistration addToolSearchResultLoadEventHandler(ToolSearchResultLoadEventHandler handler);
    }


    public static final Type<ToolSearchResultLoadEventHandler> TYPE = new Type<>();
    private final String searchPattern;
    private final List<Tool> results;
    private final String searchText;

    public ToolSearchResultLoadEvent(final String searchText,
                                    final String searchPattern,
                                    final List<Tool> results) {
        Preconditions.checkNotNull(results);
        this.searchText = searchText;
        this.searchPattern = searchPattern;
        this.results = results;
    }

    @Override
    public Type<ToolSearchResultLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    public List<Tool> getResults() {
        return results;
    }

    public String getSearchText() {
        return searchText;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    @Override
    protected void dispatch(ToolSearchResultLoadEventHandler handler) {
        handler.onToolSearchResultLoad(this);
    }
}
