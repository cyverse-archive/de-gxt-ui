package org.iplantc.de.analysis.client.events;

import org.iplantc.de.client.models.analysis.AnalysisFilter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AnalysisFilterChanged extends GwtEvent<AnalysisFilterChanged.AnalysisFilterChangedHandler> {

    public static interface AnalysisFilterChangedHandler extends EventHandler {
        void onAnalysisFilterChanged(AnalysisFilterChanged event);
    }

    public interface HasAnalysisFilterChangedHandlers {
        HandlerRegistration addAnalysisFilterChangedHandler(AnalysisFilterChangedHandler handler);
    }
    public static Type<AnalysisFilterChangedHandler> TYPE = new Type<AnalysisFilterChangedHandler>();
    private AnalysisFilter filter;

    public AnalysisFilterChanged(AnalysisFilter filter) {
        this.filter = filter;
    }

    public Type<AnalysisFilterChangedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AnalysisFilterChangedHandler handler) {
        handler.onAnalysisFilterChanged(this);
    }

    public AnalysisFilter getFilter() {
        return filter;
    }
}
