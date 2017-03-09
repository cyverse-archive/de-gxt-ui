package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class RelaunchAnalysisSelected
        extends GwtEvent<RelaunchAnalysisSelected.RelaunchAnalysisSelectedHandler> {

    public static interface RelaunchAnalysisSelectedHandler extends EventHandler {
        void onRelaunchAnalysisSelected(RelaunchAnalysisSelected event);
    }

    public interface HasRelaunchAnalysisSelectedHandlers {
        HandlerRegistration addRelaunchAnalysisSelectedHandler(RelaunchAnalysisSelectedHandler handler);
    }

    public static Type<RelaunchAnalysisSelectedHandler> TYPE =
            new Type<RelaunchAnalysisSelectedHandler>();
    private Analysis analysis;

    public RelaunchAnalysisSelected(Analysis analysis) {

        this.analysis = analysis;
    }

    public Type<RelaunchAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RelaunchAnalysisSelectedHandler handler) {
        handler.onRelaunchAnalysisSelected(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
