package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that fires when the user selects the "View Parameters" button in the
 * Analysis window
 */
public class ViewAnalysisParamsSelected
        extends GwtEvent<ViewAnalysisParamsSelected.ViewAnalysisParamsSelectedHandler> {
    public static interface ViewAnalysisParamsSelectedHandler extends EventHandler {
        void onViewAnalysisParamsSelected(ViewAnalysisParamsSelected event);
    }

    public interface HasViewAnalysisParamsSelectedHandlers {
        HandlerRegistration addViewAnalysisParamsSelectedHandler(ViewAnalysisParamsSelectedHandler handler);
    }

    public static Type<ViewAnalysisParamsSelectedHandler> TYPE =
            new Type<ViewAnalysisParamsSelectedHandler>();

    private Analysis analysis;

    public Type<ViewAnalysisParamsSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    public ViewAnalysisParamsSelected(Analysis analysis) {
        this.analysis = analysis;
    }

    protected void dispatch(ViewAnalysisParamsSelectedHandler handler) {
        handler.onViewAnalysisParamsSelected(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
