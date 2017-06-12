package org.iplantc.de.apps.client.events.tools;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram
 *
 * @author sriram
 */
public class AddNewToolSelected extends GwtEvent<AddNewToolSelected.NewToolSelectedHandler> {
    public static interface NewToolSelectedHandler extends EventHandler {
        void onNewToolSelected(AddNewToolSelected event);
    }

    public static interface HasNewToolSelectedHandlers {
        HandlerRegistration addNewToolSelectedHandler(NewToolSelectedHandler handler);
    }
    public static final Type<NewToolSelectedHandler> TYPE = new Type<>();

    public Type<NewToolSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(NewToolSelectedHandler handler) {
        handler.onNewToolSelected(this);
    }
}
