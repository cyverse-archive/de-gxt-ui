package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * A GWT event that fires when the user selects the Edit File button in the Data window
 */
public class EditFileSelected extends GwtEvent<EditFileSelected.EditFileSelectedHandler> {
    public interface EditFileSelectedHandler extends EventHandler {
        void onEditFileSelected(EditFileSelected event);
    }

    public interface HasEditFileSelectedHandlers {
        HandlerRegistration addEditFileSelectedHandler(EditFileSelectedHandler handler);
    }

    private List<DiskResource> selectedDiskResources;

    public EditFileSelected(List<DiskResource> selectedDiskResources) {
        this.selectedDiskResources = selectedDiskResources;
    }

    public static Type<EditFileSelectedHandler> TYPE = new Type<EditFileSelectedHandler>();

    public Type<EditFileSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(EditFileSelectedHandler handler) {
        handler.onEditFileSelected(this);
    }

    public List<DiskResource> getSelectedDiskResources() {
        return selectedDiskResources;
    }
}
