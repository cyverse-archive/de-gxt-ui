package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * A GWT event that fires when the user drags and drops a disk resource to a new location
 */
public class DNDDiskResourcesCompleted
        extends GwtEvent<DNDDiskResourcesCompleted.DNDDiskResourcesCompletedHandler> {
    public static interface DNDDiskResourcesCompletedHandler extends EventHandler {
        void onDNDDiskResourcesCompleted(DNDDiskResourcesCompleted event);
    }

    public interface HasDNDDiskResourcesCompletedHandlers {
        HandlerRegistration addDNDDiskResourcesCompletedHandler(DNDDiskResourcesCompletedHandler handler);
    }

    public static Type<DNDDiskResourcesCompletedHandler> TYPE =
            new Type<DNDDiskResourcesCompletedHandler>();

    private Folder targetFolder;
    private List<DiskResource> resources;

    public DNDDiskResourcesCompleted(Folder targetFolder, List<DiskResource> resources) {
        this.targetFolder = targetFolder;
        this.resources = resources;
    }

    public Type<DNDDiskResourcesCompletedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DNDDiskResourcesCompletedHandler handler) {
        handler.onDNDDiskResourcesCompleted(this);
    }

    public Folder getTargetFolder() {
        return targetFolder;
    }

    public List<DiskResource> getResources() {
        return resources;
    }
}
