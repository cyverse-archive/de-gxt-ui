package org.iplantc.de.apps.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating that a "Quick launch" action has been initiated.
 *
 * @author sriram
 *
 */
public class QuickLaunchEvent extends GwtEvent<QuickLaunchEvent.QuickLaunchEventHandler> {

    public static final GwtEvent.Type<QuickLaunchEventHandler> TYPE = new GwtEvent.Type<>();

    private String quickLaunchId;
    private String appId;

    public QuickLaunchEvent(String quickLaunchId,
                            String appId) {
        this.quickLaunchId = quickLaunchId;
        this.appId = appId;
    }

    @Override
    public Type<QuickLaunchEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(QuickLaunchEventHandler handler) {
       handler.onQuickLaunch(this);
    }

    public String getQuickLaunchId() {
        return quickLaunchId;
    }

    public String getAppId() {
        return appId;
    }

    public interface QuickLaunchEventHandler extends EventHandler {
           void onQuickLaunch(QuickLaunchEvent event);
    }

}
