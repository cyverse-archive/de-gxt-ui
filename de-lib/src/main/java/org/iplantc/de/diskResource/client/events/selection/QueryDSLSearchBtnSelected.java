package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the "Search" button in the Query DSL Form
 */
public class QueryDSLSearchBtnSelected extends GwtEvent<QueryDSLSearchBtnSelected.QueryDSLSearchBtnSelectedHandler> {
    public interface QueryDSLSearchBtnSelectedHandler extends EventHandler {
        void onQueryDSLSearchBtnSelected(QueryDSLSearchBtnSelected event);
    }

    public interface HasQueryDSLSearchBtnSelectedHandlers {
        HandlerRegistration addQueryDSLSearchBtnSelectedHandler(QueryDSLSearchBtnSelectedHandler handler);
    }

    public static Type<QueryDSLSearchBtnSelectedHandler> TYPE = new Type<QueryDSLSearchBtnSelectedHandler>();
    private QueryDSLTemplate template;

    public QueryDSLSearchBtnSelected(QueryDSLTemplate template) {
        this.template = template;
    }

    public Type<QueryDSLSearchBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(QueryDSLSearchBtnSelectedHandler handler) {
        handler.onQueryDSLSearchBtnSelected(this);
    }

    public QueryDSLTemplate getTemplate() {
        return template;
    }
}
