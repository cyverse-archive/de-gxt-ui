package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.tags.Tag;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that fires when a user removes a tag from a resource
 */
public class RemoveResourceTagSelected
        extends GwtEvent<RemoveResourceTagSelected.RemoveResourceTagSelectedHandler> {
    public static interface RemoveResourceTagSelectedHandler extends EventHandler {
        void onRemoveResourceTagSelected(RemoveResourceTagSelected event);
    }

    public interface HasRemoveResourceTagSelectedHandlers {
        HandlerRegistration addRemoveResourceTagSelectedHandler(RemoveResourceTagSelectedHandler handler);
    }

    private DiskResource resource;
    private Tag tag;

    public RemoveResourceTagSelected(DiskResource resource, Tag tag) {
        this.resource = resource;
        this.tag = tag;
    }

    public static Type<RemoveResourceTagSelectedHandler> TYPE =
            new Type<RemoveResourceTagSelectedHandler>();

    public Type<RemoveResourceTagSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveResourceTagSelectedHandler handler) {
        handler.onRemoveResourceTagSelected(this);
    }

    public DiskResource getResource() {
        return resource;
    }

    public Tag getTag() {
        return tag;
    }
}
