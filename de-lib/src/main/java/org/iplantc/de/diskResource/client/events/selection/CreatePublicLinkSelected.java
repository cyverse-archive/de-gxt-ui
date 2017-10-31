package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * A GWT event that fires when the user clicks on the Create Public Link button in the Data window
 */

public class CreatePublicLinkSelected
        extends GwtEvent<CreatePublicLinkSelected.CreatePublicLinkSelectedHandler> {
    public interface CreatePublicLinkSelectedHandler extends EventHandler {
        void onCreatePublicLinkSelected(CreatePublicLinkSelected event);
    }

    public interface HasCreatePublicLinkSelectedHandlers {
        HandlerRegistration addCreatePublicLinkSelectedHandler(CreatePublicLinkSelectedHandler handler);
    }

    private List<DiskResource> selectedDiskResources;

    public CreatePublicLinkSelected(List<DiskResource> selectedDiskResources) {
        this.selectedDiskResources = selectedDiskResources;
    }

    public static Type<CreatePublicLinkSelectedHandler> TYPE =
            new Type<CreatePublicLinkSelectedHandler>();

    public Type<CreatePublicLinkSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreatePublicLinkSelectedHandler handler) {
        handler.onCreatePublicLinkSelected(this);
    }

    public List<DiskResource> getSelectedDiskResources() {
        return selectedDiskResources;
    }
}
