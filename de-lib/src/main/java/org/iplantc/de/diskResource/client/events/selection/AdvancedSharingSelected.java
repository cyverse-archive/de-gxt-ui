package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user clicks the Advanced Sharing button in the Data Links dialog
 */
public class AdvancedSharingSelected
        extends GwtEvent<AdvancedSharingSelected.AdvancedSharingSelectedHandler> {
    public static interface AdvancedSharingSelectedHandler extends EventHandler {
        void onAdvancedSharingSelected(AdvancedSharingSelected event);
    }

    public interface HasAdvancedSharingSelectedHandlers {
        HandlerRegistration addAdvancedSharingSelectedHandler(AdvancedSharingSelectedHandler handler);
    }

    private DiskResource selectedResource;

    public AdvancedSharingSelected(DiskResource selectedResource) {
        this.selectedResource = selectedResource;
    }

    public static Type<AdvancedSharingSelectedHandler> TYPE = new Type<AdvancedSharingSelectedHandler>();

    public Type<AdvancedSharingSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AdvancedSharingSelectedHandler handler) {
        handler.onAdvancedSharingSelected(this);
    }

    public DiskResource getSelectedResource() {
        return selectedResource;
    }
}
