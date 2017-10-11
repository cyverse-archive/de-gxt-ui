package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.diskResource.client.BulkMetadataView;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that gets fired when the user selects one of the options under Apply Bulk Metadata in the Data window
 */
public class BulkMetadataSelected extends GwtEvent<BulkMetadataSelected.BulkMetadataSelectedHandler> {
    public static interface BulkMetadataSelectedHandler extends EventHandler {
        void onBulkMetadataSelected(BulkMetadataSelected event);
    }

    public interface HasBulkMetadataSelectedHandlers {
        HandlerRegistration addBulkMetadataSelectedHandler(BulkMetadataSelectedHandler handler);
    }

    private BulkMetadataView.BULK_MODE mode;

    public BulkMetadataSelected(BulkMetadataView.BULK_MODE mode) {
        this.mode = mode;
    }

    public static Type<BulkMetadataSelectedHandler> TYPE = new Type<BulkMetadataSelectedHandler>();

    public Type<BulkMetadataSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(BulkMetadataSelectedHandler handler) {
        handler.onBulkMetadataSelected(this);
    }

    public BulkMetadataView.BULK_MODE getMode() {
        return mode;
    }
}
