package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that captures users action to copy a file / folder 's path
 *
 * Created by sriram on 11/2/17.
 */
public class CopyPathSelected extends GwtEvent<CopyPathSelected.CopyPathSelectedEventHandler> {

    public static interface HasCopyPathSelectedEventHandlers {
        HandlerRegistration addHasCopyPathSelectedEventHandlers(CopyPathSelectedEventHandler handler);
    }

    public interface CopyPathSelectedEventHandler extends EventHandler {
        void onCopyPathSelected(CopyPathSelected event);
    }

    public static final Type<CopyPathSelectedEventHandler> TYPE = new Type<>();
    private final DiskResource dr;

    public CopyPathSelected( DiskResource dr) {
        this.dr = dr;
    }

    @Override
    public Type<CopyPathSelected.CopyPathSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CopyPathSelected.CopyPathSelectedEventHandler handler) {
        handler.onCopyPathSelected(this);
    }

    public DiskResource getDiskResource() {
        return dr;
    }
}
