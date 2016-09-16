package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.dataLink.DataLink;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class DeleteDataLinkSelected
        extends GwtEvent<DeleteDataLinkSelected.DeleteDataLinkSelectedHandler> {
    public static interface DeleteDataLinkSelectedHandler extends EventHandler {
        void onDeleteDataLinkSelected(DeleteDataLinkSelected event);
    }

    public interface HasDeleteDataLinkSelectedHandlers {
        HandlerRegistration addDeleteDataLinkSelectedHandler(DeleteDataLinkSelectedHandler handler);
    }
    public static Type<DeleteDataLinkSelectedHandler> TYPE = new Type<DeleteDataLinkSelectedHandler>();
    private DataLink link;

    public DeleteDataLinkSelected(DataLink link) {
        this.link = link;
    }

    public Type<DeleteDataLinkSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteDataLinkSelectedHandler handler) {
        handler.onDeleteDataLinkSelected(this);
    }

    public DataLink getLink() {
        return link;
    }
}
