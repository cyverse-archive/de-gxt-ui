package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;

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
    private Splittable template;

    public QueryDSLSearchBtnSelected(Splittable template) {
        this.template = template;
    }

    public Type<QueryDSLSearchBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(QueryDSLSearchBtnSelectedHandler handler) {
        handler.onQueryDSLSearchBtnSelected(this);
    }

    public Splittable getTemplate() {
        return template;
    }
}
