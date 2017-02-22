package org.iplantc.de.analysis.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class RefreshAnalysesSelected
        extends GwtEvent<RefreshAnalysesSelected.RefreshAnalysesSelectedHandler> {

    public static interface RefreshAnalysesSelectedHandler extends EventHandler {
        void onRefreshAnalysesSelected(RefreshAnalysesSelected event);
    }

    public interface HasRefreshAnalysesSelectedHandlers {
        HandlerRegistration addRefreshAnalysesSelectedHandler(RefreshAnalysesSelectedHandler handler);
    }
    public static Type<RefreshAnalysesSelectedHandler> TYPE = new Type<RefreshAnalysesSelectedHandler>();

    public Type<RefreshAnalysesSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RefreshAnalysesSelectedHandler handler) {
        handler.onRefreshAnalysesSelected(this);
    }
}
