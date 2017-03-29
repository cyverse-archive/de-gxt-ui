package org.iplantc.de.apps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class SaveAppSelected extends GwtEvent<SaveAppSelected.SaveAppSelectedHandler> {

    public static interface SaveAppSelectedHandler extends EventHandler {
        void onSaveAppSelected(SaveAppSelected event);
    }

    public interface HasSaveAppSelectedHandlers {
        HandlerRegistration addSaveAppSelectedHandler(SaveAppSelectedHandler handler);
    }

    public static Type<SaveAppSelectedHandler> TYPE = new Type<SaveAppSelectedHandler>();

    public Type<SaveAppSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SaveAppSelectedHandler handler) {
        handler.onSaveAppSelected(this);
    }
}
