package org.iplantc.de.analysis.client.events.selection;

import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;



/**
 * Created by sriram on 11/30/16.
 */
public class AnalysisUserSupportRequestedEvent extends GwtEvent<AnalysisUserSupportRequestedEvent.AnalysisUserSupportRequestedEventHandler> {

    public interface AnalysisUserSupportRequestedEventHandler extends EventHandler {
        void onUserSupportRequested(AnalysisUserSupportRequestedEvent event);
    }

    public static interface  HasAnalysisUserSupportRequestedEventHandlers {
        HandlerRegistration addAnalysisUserSupportRequestedEventHandler(AnalysisUserSupportRequestedEventHandler handler);
    }


    private final Analysis value;

    public AnalysisUserSupportRequestedEvent(final Analysis value) {
        this.value = value;
    }

    public static final GwtEvent.Type<AnalysisUserSupportRequestedEventHandler> TYPE = new GwtEvent.Type<>();


    @Override
    public Type<AnalysisUserSupportRequestedEventHandler> getAssociatedType() {
        return  TYPE;
    }

    public Analysis getValue() {
        return  value;
    }

    @Override
    protected void dispatch(AnalysisUserSupportRequestedEventHandler handler) {
        handler.onUserSupportRequested(this);
    }

}
