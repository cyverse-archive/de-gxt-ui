package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class RenameAnalysisSelected
        extends GwtEvent<RenameAnalysisSelected.RenameAnalysisSelectedHandler> {

    public static interface RenameAnalysisSelectedHandler extends EventHandler {
        void onRenameAnalysisSelected(RenameAnalysisSelected event);
    }

    public interface HasRenameAnalysisSelectedHandlers {
        HandlerRegistration addRenameAnalysisSelectedHandler(RenameAnalysisSelectedHandler handler);
    }

    public static Type<RenameAnalysisSelectedHandler> TYPE = new Type<RenameAnalysisSelectedHandler>();
    private Analysis analysis;
    private String newName;

    public RenameAnalysisSelected(Analysis analysis, String newName) {
        this.analysis = analysis;
        this.newName = newName;
    }

    public Type<RenameAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RenameAnalysisSelectedHandler handler) {
        handler.onRenameAnalysisSelected(this);
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public String getNewName() {
        return newName;
    }
}
