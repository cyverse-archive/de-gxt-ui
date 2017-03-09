package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class CancelAnalysisSelected
        extends GwtEvent<CancelAnalysisSelected.CancelAnalysisSelectedHandler> {

    public static interface CancelAnalysisSelectedHandler extends EventHandler {
        void onCancelAnalysisSelected(CancelAnalysisSelected event);
    }

    public interface HasCancelAnalysisSelectedHandlers {
        HandlerRegistration addCancelAnalysisSelectedHandler(CancelAnalysisSelectedHandler handler);
    }

    public static Type<CancelAnalysisSelectedHandler> TYPE = new Type<CancelAnalysisSelectedHandler>();
    private List<Analysis> analysisList;

    public CancelAnalysisSelected(List<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

    public Type<CancelAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CancelAnalysisSelectedHandler handler) {
        handler.onCancelAnalysisSelected(this);
    }

    public List<Analysis> getAnalysisList() {
        return analysisList;
    }
}
