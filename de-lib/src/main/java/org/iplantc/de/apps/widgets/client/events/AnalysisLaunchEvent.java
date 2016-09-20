package org.iplantc.de.apps.widgets.client.events;

import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.client.models.HasId;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.iplantc.de.client.models.HasQualifiedId;

public class AnalysisLaunchEvent extends GwtEvent<AnalysisLaunchEventHandler> {

    public interface AnalysisLaunchEventHandler extends EventHandler {
        void onAnalysisLaunch(AnalysisLaunchEvent analysisLaunchEvent);
    }

    public static GwtEvent.Type<AnalysisLaunchEventHandler> TYPE = new GwtEvent.Type<AnalysisLaunchEvent.AnalysisLaunchEventHandler>();
    private final HasQualifiedId at;

    public AnalysisLaunchEvent(HasQualifiedId at) {
        this.at = at;
    }

    public HasQualifiedId getAppTemplateId() {
        return at;
    }

    @Override
    public GwtEvent.Type<AnalysisLaunchEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisLaunchEventHandler handler) {
        handler.onAnalysisLaunch(this);
    }
}
