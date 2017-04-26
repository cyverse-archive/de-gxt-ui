package org.iplantc.de.apps.client.events.tools;

import org.iplantc.de.client.models.tool.Tool;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class DeleteToolSelected extends GwtEvent<DeleteToolSelected.DeleteToolsSelectedHandler> {
    public static interface DeleteToolsSelectedHandler extends EventHandler {
        void onDeleteToolsSelected(DeleteToolSelected event);
    }

    public static interface HasDeleteToolsSelectedHandlers {
        HandlerRegistration addDeleteToolsSelectedHandler(DeleteToolsSelectedHandler handler);
    }

    public static final Type<DeleteToolsSelectedHandler> TYPE = new Type<>();
    private final List<Tool> ToolsToBeDeleted;

    public DeleteToolSelected(final List<Tool> ToolsToBeDeleted) {
        Preconditions.checkNotNull(ToolsToBeDeleted);
        Preconditions.checkArgument(!ToolsToBeDeleted.isEmpty());
        this.ToolsToBeDeleted = ToolsToBeDeleted;
    }

    public List<Tool> getToolsToBeDeleted() {
        return ToolsToBeDeleted;
    }

    public Type<DeleteToolsSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteToolsSelectedHandler handler) {
        handler.onDeleteToolsSelected(this);
    }
}
