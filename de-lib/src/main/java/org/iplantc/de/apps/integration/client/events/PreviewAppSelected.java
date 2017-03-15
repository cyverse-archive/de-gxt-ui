package org.iplantc.de.apps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class PreviewAppSelected extends GwtEvent<PreviewAppSelected.PreviewAppSelectedHandler> {

    public static interface PreviewAppSelectedHandler extends EventHandler {
        void onPreviewAppSelected(PreviewAppSelected event);
    }

    public interface HasPreviewAppSelectedHandlers {
        HandlerRegistration addPreviewAppSelectedHandler(PreviewAppSelectedHandler handler);
    }

    public static Type<PreviewAppSelectedHandler> TYPE = new Type<PreviewAppSelectedHandler>();

    public Type<PreviewAppSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PreviewAppSelectedHandler handler) {
        handler.onPreviewAppSelected(this);
    }
}
