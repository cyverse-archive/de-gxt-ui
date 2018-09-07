package org.iplantc.de.analysis.client.events;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 * An event that fires when the user clicks on the Interactive (external-link) icon in the Analysis
 * window.  This will take the user to the externally accessible VICE url.
 */
public class InteractiveIconClicked extends GwtEvent<InteractiveIconClicked.InteractiveIconClickedHandler> {
    public interface InteractiveIconClickedHandler extends EventHandler {
        void onInteractiveIconClicked(InteractiveIconClicked event);
    }

    public interface HasInteractiveIconClickedHandlers {
        HandlerRegistration addInteractiveIconClickedHandler(InteractiveIconClickedHandler handler);
    }

    private Analysis analysis;

    public InteractiveIconClicked(Analysis analysis) {
        this.analysis = analysis;
    }

    public static Type<InteractiveIconClickedHandler> TYPE = new Type<InteractiveIconClickedHandler>();

    public Type<InteractiveIconClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(InteractiveIconClickedHandler handler) {
        handler.onInteractiveIconClicked(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
