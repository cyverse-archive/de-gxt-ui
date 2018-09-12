package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author mian
 */
public class CompleteAnalysisSelected
        extends GwtEvent<CompleteAnalysisSelected.CompleteAnalysisSelectedHandler> {

    public static interface CompleteAnalysisSelectedHandler extends EventHandler {
        void onCompleteAnalysisSelected(CompleteAnalysisSelected event);
    }

    public interface HasCompleteAnalysisSelectedHandlers {
        HandlerRegistration addCompleteAnalysisSelectedHandler(CompleteAnalysisSelectedHandler handler);
    }

    public static Type<CompleteAnalysisSelectedHandler> TYPE = new Type<CompleteAnalysisSelectedHandler>();
    private List<Analysis> analysisList;

    public CompleteAnalysisSelected(List<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

    public Type<CompleteAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CompleteAnalysisSelectedHandler handler) {
        handler.onCompleteAnalysisSelected(this);
    }

    public List<Analysis> getAnalysisList() {
        return analysisList;
    }
}
