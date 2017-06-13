package org.iplantc.de.tools.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class EditToolSelected extends GwtEvent<EditToolSelected.EditToolSelectedHandler> {
    public static interface EditToolSelectedHandler extends EventHandler {
        void onEditToolSelected(EditToolSelected event);
    }

    public static interface HasEditToolSelectedHandlers {
        HandlerRegistration addEditToolSelectedHandler(EditToolSelectedHandler handler);
    }

    public static final Type<EditToolSelectedHandler> TYPE = new Type<>();

    public EditToolSelected() {
    }


    public Type<EditToolSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(EditToolSelectedHandler handler) {
        handler.onEditToolSelected(this);
    }
}
