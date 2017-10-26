package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SaveMetadataToFileBtnSelected
        extends GwtEvent<SaveMetadataToFileBtnSelected.SaveMetadataToFileBtnSelectedHandler> {
    public static interface SaveMetadataToFileBtnSelectedHandler extends EventHandler {
        void onSaveMetadataToFileBtnSelected(SaveMetadataToFileBtnSelected event);
    }

    public interface HasSaveMetadataToFileBtnSelectedHandlers {
        HandlerRegistration addSaveMetadataToFileBtnSelectedHandler(SaveMetadataToFileBtnSelectedHandler handler);
    }

    public static Type<SaveMetadataToFileBtnSelectedHandler> TYPE =
            new Type<SaveMetadataToFileBtnSelectedHandler>();

    public Type<SaveMetadataToFileBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SaveMetadataToFileBtnSelectedHandler handler) {
        handler.onSaveMetadataToFileBtnSelected(this);
    }
}
