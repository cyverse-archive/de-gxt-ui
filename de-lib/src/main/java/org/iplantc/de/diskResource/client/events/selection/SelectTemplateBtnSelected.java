package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT even that fires when the user hits the "Select Template" button in the MetadataView
 */
public class SelectTemplateBtnSelected
        extends GwtEvent<SelectTemplateBtnSelected.SelectTemplateBtnSelectedHandler> {
    public static interface SelectTemplateBtnSelectedHandler extends EventHandler {
        void onSelectTemplateBtnSelected(SelectTemplateBtnSelected event);
    }

    public interface HasSelectTemplateBtnSelectedHandlers {
        HandlerRegistration addSelectTemplateBtnSelectedHandler(SelectTemplateBtnSelectedHandler handler);
    }

    public static Type<SelectTemplateBtnSelectedHandler> TYPE =
            new Type<SelectTemplateBtnSelectedHandler>();

    public Type<SelectTemplateBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SelectTemplateBtnSelectedHandler handler) {
        handler.onSelectTemplateBtnSelected(this);
    }
}
