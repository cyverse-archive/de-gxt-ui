package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class DeleteAnalysisSelected
        extends GwtEvent<DeleteAnalysisSelected.DeleteAnalysisSelectedHandler> {

    public static interface DeleteAnalysisSelectedHandler extends EventHandler {
        void onDeleteAnalysisSelected(DeleteAnalysisSelected event);
    }

    public interface HasDeleteAnalysisSelectedHandlers {
        HandlerRegistration addDeleteAnalysisSelectedHandler(DeleteAnalysisSelectedHandler handler);
    }

    public static Type<DeleteAnalysisSelectedHandler> TYPE = new Type<DeleteAnalysisSelectedHandler>();
    private List<Analysis> analyses;

    public DeleteAnalysisSelected(List<Analysis> analyses) {
        this.analyses = analyses;
    }

    public Type<DeleteAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DeleteAnalysisSelectedHandler handler) {
        handler.onDeleteAnalysisSelected(this);
    }

    public List<Analysis> getAnalyses() {
        return analyses;
    }
}
