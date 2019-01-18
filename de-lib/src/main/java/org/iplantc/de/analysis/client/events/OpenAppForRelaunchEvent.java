package org.iplantc.de.analysis.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author jstroot
 */
public class OpenAppForRelaunchEvent extends GwtEvent<OpenAppForRelaunchEvent.OpenAppForRelaunchEventHandler> {
    public String getSystemId() {
        return systemId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public interface OpenAppForRelaunchEventHandler extends EventHandler {
        void onRequestOpenAppForRelaunch(OpenAppForRelaunchEvent event);
    }

    public static final Type<OpenAppForRelaunchEventHandler> TYPE = new Type<>();
    private final String systemId;
    private final String appId;
    private final String analysisId;

    public OpenAppForRelaunchEvent(String analysisId, String systemId, String appId) {
        this.systemId = systemId;
        this.appId = appId;
        this.analysisId = analysisId;
    }

    @Override
    public Type<OpenAppForRelaunchEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(OpenAppForRelaunchEventHandler handler) {
        handler.onRequestOpenAppForRelaunch(this);
    }
}
