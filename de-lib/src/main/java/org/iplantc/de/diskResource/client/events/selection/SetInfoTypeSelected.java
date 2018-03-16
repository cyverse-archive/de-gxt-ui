package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by jstroot on 2/4/15.
 *
 * @author jstroot
 */
public class SetInfoTypeSelected extends GwtEvent<SetInfoTypeSelected.SetInfoTypeSelectedHandler> {

    public static interface SetInfoTypeSelectedHandler extends EventHandler {
        void onSetInfoTypeSelected(SetInfoTypeSelected event);
    }

    public static interface HasResetInfoTypeSelectedHandlers {
        HandlerRegistration addResetInfoTypeSelectedHandler(SetInfoTypeSelectedHandler handler);
    }

    public static final Type<SetInfoTypeSelectedHandler> TYPE = new Type<>();
    private final DiskResource diskResource;
    private final String infoType;

    public SetInfoTypeSelected(final DiskResource diskResource, final String infoType) {
        this.diskResource = diskResource;
        this.infoType = infoType;
    }

    public Type<SetInfoTypeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public DiskResource getDiskResource() {
        return diskResource;
    }

    protected void dispatch(SetInfoTypeSelectedHandler handler) {
        handler.onSetInfoTypeSelected(this);
    }

    public String getInfoType() {
        return infoType;
    }

}
