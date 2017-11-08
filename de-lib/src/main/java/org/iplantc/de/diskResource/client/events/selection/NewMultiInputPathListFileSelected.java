package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 11/8/17.
 */
public class NewMultiInputPathListFileSelected
        extends GwtEvent<NewMultiInputPathListFileSelected.NewMultiInputPathListFileSelectedHandler> {


    public interface HasNewMultiInputPathListSelectedHandlers {
        HandlerRegistration addNewMultiInputPathListFileSelectedHandler(NewMultiInputPathListFileSelectedHandler handler);
    }


    public static Type<NewMultiInputPathListFileSelectedHandler> TYPE =
            new Type<NewMultiInputPathListFileSelectedHandler>();

    public Type<NewMultiInputPathListFileSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(NewMultiInputPathListFileSelectedHandler handler) {
        handler.onNewMultiInputPathListFileSelected(this);
    }

    /**
     * Created by sriram on 11/8/17.
     */
    public interface NewMultiInputPathListFileSelectedHandler extends EventHandler {

        /**
         * Handler for NewMultiInputPathListSelected event
          * @param event
         */
      void onNewMultiInputPathListFileSelected(NewMultiInputPathListFileSelected event);

    }
}
