package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the New Tabular Data file button in the Data window
 */
public class CreateNewDelimitedFileSelected
        extends GwtEvent<CreateNewDelimitedFileSelected.CreateNewDelimitedFileSelectedHandler> {
    public static interface CreateNewDelimitedFileSelectedHandler extends EventHandler {
        void onCreateNewDelimitedFileSelected(CreateNewDelimitedFileSelected event);
    }

    public interface HasCreateNewDelimitedFileSelectedHandlers {
        HandlerRegistration addCreateNewDelimitedFileSelectedHandler(CreateNewDelimitedFileSelectedHandler handler);
    }

    public static Type<CreateNewDelimitedFileSelectedHandler> TYPE =
            new Type<CreateNewDelimitedFileSelectedHandler>();

    public Type<CreateNewDelimitedFileSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateNewDelimitedFileSelectedHandler handler) {
        handler.onCreateNewDelimitedFileSelected(this);
    }
}
