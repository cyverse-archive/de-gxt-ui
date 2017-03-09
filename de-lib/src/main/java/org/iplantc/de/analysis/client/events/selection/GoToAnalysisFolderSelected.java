package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class GoToAnalysisFolderSelected
        extends GwtEvent<GoToAnalysisFolderSelected.GoToAnalysisFolderSelectedHandler> {

    public static interface GoToAnalysisFolderSelectedHandler extends EventHandler {
        void onGoToAnalysisFolderSelected(GoToAnalysisFolderSelected event);
    }

    public interface HasGoToAnalysisFolderSelectedHandlers {
        HandlerRegistration addGoToAnalysisFolderSelectedHandler(GoToAnalysisFolderSelectedHandler handler);
    }

    public static Type<GoToAnalysisFolderSelectedHandler> TYPE =
            new Type<GoToAnalysisFolderSelectedHandler>();
    private Analysis analysis;

    public GoToAnalysisFolderSelected(Analysis analysis) {
        this.analysis = analysis;
    }

    public Type<GoToAnalysisFolderSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(GoToAnalysisFolderSelectedHandler handler) {
        handler.onGoToAnalysisFolderSelected(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
