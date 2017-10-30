package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user has selected the Request DOI button in the Data window
 */
public class RequestDOISelected extends GwtEvent<RequestDOISelected.RequestDOISelectedHandler> {
    public static interface RequestDOISelectedHandler extends EventHandler {
        void onRequestDOISelected(RequestDOISelected event);
    }

    public interface HasRequestDOISelectedHandlers {
        HandlerRegistration addRequestDOISelectedHandler(RequestDOISelectedHandler handler);
    }

    private String resourceId;

    public RequestDOISelected(String resourceId) {
        this.resourceId = resourceId;
    }

    public static Type<RequestDOISelectedHandler> TYPE = new Type<RequestDOISelectedHandler>();

    public Type<RequestDOISelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RequestDOISelectedHandler handler) {
        handler.onRequestDOISelected(this);
    }

    public String getResourceId() {
        return resourceId;
    }
}
