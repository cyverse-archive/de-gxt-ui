package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author aramsey
 */
public class ShareAnalysisSelected extends GwtEvent<ShareAnalysisSelected.ShareAnalysisSelectedHandler> {

    public static interface ShareAnalysisSelectedHandler extends EventHandler {
        void onShareAnalysisSelected(ShareAnalysisSelected event);
    }

    public interface HasShareAnalysisSelectedHandlers {
        HandlerRegistration addShareAnalysisSelectedHandler(ShareAnalysisSelectedHandler handler);
    }
    public static Type<ShareAnalysisSelectedHandler> TYPE = new Type<ShareAnalysisSelectedHandler>();
    private List<Analysis> analysisList;

    public ShareAnalysisSelected(List<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

    public Type<ShareAnalysisSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShareAnalysisSelectedHandler handler) {
        handler.onShareAnalysisSelected(this);
    }

    public List<Analysis> getAnalysisList() {
        return analysisList;
    }
}
