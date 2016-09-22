package org.iplantc.de.apps.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class SelectedHierarchyNotFound
        extends GwtEvent<SelectedHierarchyNotFound.SelectedHierarchyNotFoundHandler> {
    public static interface SelectedHierarchyNotFoundHandler extends EventHandler {
        void onSelectedHierarchyNotFound(SelectedHierarchyNotFound event);
    }

    public interface HasSelectedHierarchyNotFoundHandlers {
        HandlerRegistration addSelectedHierarchyNotFoundHandler(SelectedHierarchyNotFoundHandler handler);
    }
    public static Type<SelectedHierarchyNotFoundHandler> TYPE =
            new Type<SelectedHierarchyNotFoundHandler>();

    public Type<SelectedHierarchyNotFoundHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SelectedHierarchyNotFoundHandler handler) {
        handler.onSelectedHierarchyNotFound(this);
    }
}
