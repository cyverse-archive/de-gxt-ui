package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * A GWT event that fires when the user clicks the Create button in the Data Links dialog
 */
public class CreateDataLinkSelected
        extends GwtEvent<CreateDataLinkSelected.CreateDataLinkSelectedHandler> {
    public static interface CreateDataLinkSelectedHandler extends EventHandler {
        void onCreateDataLinkSelected(CreateDataLinkSelected event);
    }

    public interface HasCreateDataLinkSelectedHandlers {
        HandlerRegistration addCreateDataLinkSelectedHandler(CreateDataLinkSelectedHandler handler);
    }
    public static Type<CreateDataLinkSelectedHandler> TYPE = new Type<CreateDataLinkSelectedHandler>();
    private List<DiskResource> selectedDiskResources;

    public CreateDataLinkSelected(List<DiskResource> selectedDiskResources) {
        this.selectedDiskResources = selectedDiskResources;
    }

    public Type<CreateDataLinkSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateDataLinkSelectedHandler handler) {
        handler.onCreateDataLinkSelected(this);
    }

    public List<DiskResource> getSelectedDiskResources() {
        return selectedDiskResources;
    }
}
