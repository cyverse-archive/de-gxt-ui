package org.iplantc.de.apps.client.events;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * An event indicating that a "Create Quick launch" action has been initiated.
 *
 * @author sriram
 */
public class RequestCreateQuickLaunchEvent
        extends GwtEvent<RequestCreateQuickLaunchEvent.RequestCreateQuickLaunchEventHandler> {

    public static final GwtEvent.Type<RequestCreateQuickLaunchEventHandler> TYPE = new GwtEvent.Type<>();
    private String appId;

    public RequestCreateQuickLaunchEvent(String appId) {
        this.appId = appId;
    }

    @Override
    public Type<RequestCreateQuickLaunchEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestCreateQuickLaunchEventHandler handler) {
        handler.onRequestCreateQuickLaunch(this);
    }

    public interface RequestCreateQuickLaunchEventHandler extends EventHandler {
        void onRequestCreateQuickLaunch(RequestCreateQuickLaunchEvent event);
    }

    public String getAppId() {
        return appId;
    }

}
