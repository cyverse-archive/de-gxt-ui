package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AnalysisJobInfoSelected
        extends GwtEvent<AnalysisJobInfoSelected.AnalysisJobInfoSelectedHandler> {
    public static interface AnalysisJobInfoSelectedHandler extends EventHandler {
        void onAnalysisJobInfoSelected(AnalysisJobInfoSelected event);
    }
    public interface HasAnalysisJobInfoSelectedHandlers {
        HandlerRegistration addAnalysisJobInfoSelectedHandler(AnalysisJobInfoSelectedHandler handler);
    }
    public static Type<AnalysisJobInfoSelectedHandler> TYPE = new Type<AnalysisJobInfoSelectedHandler>();
    private Analysis analysis;

    public AnalysisJobInfoSelected(Analysis analysis) {
        this.analysis = analysis;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public Type<AnalysisJobInfoSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AnalysisJobInfoSelectedHandler handler) {
        handler.onAnalysisJobInfoSelected(this);
    }
}
