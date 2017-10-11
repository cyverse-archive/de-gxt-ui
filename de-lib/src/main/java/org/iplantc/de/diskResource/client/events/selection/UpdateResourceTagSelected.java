package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.tags.Tag;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user opts to create or add a tag to a resource
 */
public class UpdateResourceTagSelected
        extends GwtEvent<UpdateResourceTagSelected.UpdateResourceTagSelectedHandler> {
    public static interface UpdateResourceTagSelectedHandler extends EventHandler {
        void onUpdateResourceTagSelected(UpdateResourceTagSelected event);
    }

    public interface HasUpdateResourceTagSelectedHandlers {
        HandlerRegistration addUpdateResourceTagSelectedHandler(UpdateResourceTagSelectedHandler handler);
    }

    private DiskResource diskResource;
    private Tag tag;

    public UpdateResourceTagSelected(DiskResource diskResource, Tag tag) {
        this.diskResource = diskResource;
        this.tag = tag;
    }

    public static Type<UpdateResourceTagSelectedHandler> TYPE =
            new Type<UpdateResourceTagSelectedHandler>();

    public Type<UpdateResourceTagSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(UpdateResourceTagSelectedHandler handler) {
        handler.onUpdateResourceTagSelected(this);
    }

    public DiskResource getDiskResource() {
        return diskResource;
    }

    public Tag getTag() {
        return tag;
    }
}
