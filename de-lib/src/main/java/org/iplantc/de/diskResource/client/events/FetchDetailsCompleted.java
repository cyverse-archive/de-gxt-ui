package org.iplantc.de.diskResource.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class FetchDetailsCompleted extends GwtEvent<FetchDetailsCompleted.FetchDetailsCompletedHandler> {
    public static interface FetchDetailsCompletedHandler extends EventHandler {
        void onFetchDetailsCompleted(FetchDetailsCompleted event);
    }

    public interface HasFetchDetailsCompletedHandlers {
        HandlerRegistration addFetchDetailsCompletedHandler(FetchDetailsCompletedHandler handler);
    }
    public static Type<FetchDetailsCompletedHandler> TYPE = new Type<FetchDetailsCompletedHandler>();

    public Type<FetchDetailsCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(FetchDetailsCompletedHandler handler) {
        handler.onFetchDetailsCompleted(this);
    }

}
