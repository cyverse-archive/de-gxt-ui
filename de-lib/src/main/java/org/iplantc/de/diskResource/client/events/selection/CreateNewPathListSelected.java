package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT even that fires when the user selects the Create HT Path List button in the Data window
 */
public class CreateNewPathListSelected
        extends GwtEvent<CreateNewPathListSelected.CreateNewPathListSelectedHandler> {
    public static interface CreateNewPathListSelectedHandler extends EventHandler {
        void onCreateNewPathListSelected(CreateNewPathListSelected event);
    }

    public interface HasCreateNewPathListSelectedHandlers {
        HandlerRegistration addCreateNewPathListSelectedHandler(CreateNewPathListSelectedHandler handler);
    }

    public static Type<CreateNewPathListSelectedHandler> TYPE =
            new Type<CreateNewPathListSelectedHandler>();

    public Type<CreateNewPathListSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewPathListSelectedHandler handler) {
        handler.onCreateNewPathListSelected(this);
    }
}
