package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user clicks the Show Link button in the Data Links dialog
 */
public class ShowDataLinkSelected extends GwtEvent<ShowDataLinkSelected.ShowDataLinkSelectedHandler> {
    public static interface ShowDataLinkSelectedHandler extends EventHandler {
        void onShowDataLinkSelected(ShowDataLinkSelected event);
    }

    public interface HasShowDataLinkSelectedHandlers {
        HandlerRegistration addShowDataLinkSelectedHandler(ShowDataLinkSelectedHandler handler);
    }
    public static Type<ShowDataLinkSelectedHandler> TYPE = new Type<ShowDataLinkSelectedHandler>();
    private DiskResource selectedResource;

    public ShowDataLinkSelected(DiskResource selectedResource) {
        this.selectedResource = selectedResource;
    }

    public Type<ShowDataLinkSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowDataLinkSelectedHandler handler) {
        handler.onShowDataLinkSelected(this);
    }

    public DiskResource getSelectedResource() {
        return selectedResource;
    }
}
