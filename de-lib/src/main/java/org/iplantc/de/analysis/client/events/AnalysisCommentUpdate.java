package org.iplantc.de.analysis.client.events;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AnalysisCommentUpdate extends GwtEvent<AnalysisCommentUpdate.AnalysisCommentUpdateHandler> {

    public static interface AnalysisCommentUpdateHandler extends EventHandler {
        void onAnalysisCommentUpdate(AnalysisCommentUpdate event);
    }

    public interface HasAnalysisCommentUpdateHandlers {
        HandlerRegistration addAnalysisCommentUpdateHandler(AnalysisCommentUpdateHandler handler);
    }

    private Analysis analysis;

    public AnalysisCommentUpdate(Analysis analysis) {

        this.analysis = analysis;
    }

    public static Type<AnalysisCommentUpdateHandler> TYPE = new Type<AnalysisCommentUpdateHandler>();

    public Type<AnalysisCommentUpdateHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AnalysisCommentUpdateHandler handler) {
        handler.onAnalysisCommentUpdate(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
