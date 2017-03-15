package org.iplantc.de.apps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class PreviewJsonSelected extends GwtEvent<PreviewJsonSelected.PreviewJsonSelectedHandler> {

    public static interface PreviewJsonSelectedHandler extends EventHandler {
        void onPreviewJsonSelected(PreviewJsonSelected event);
    }

    public interface HasPreviewJsonSelectedHandlers {
        HandlerRegistration addPreviewJsonSelectedHandler(PreviewJsonSelectedHandler handler);
    }

    public static Type<PreviewJsonSelectedHandler> TYPE = new Type<PreviewJsonSelectedHandler>();

    public Type<PreviewJsonSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PreviewJsonSelectedHandler handler) {
        handler.onPreviewJsonSelected(this);
    }
}
