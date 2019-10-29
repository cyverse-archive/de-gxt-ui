package org.iplantc.de.apps.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author jstroot
 */
public class AppInfoSelectedEvent extends GwtEvent<AppInfoSelectedEvent.AppInfoSelectedEventHandler> {



    public interface AppInfoSelectedEventHandler extends EventHandler {
        void onAppInfoSelected(AppInfoSelectedEvent event);
    }

    public interface HasAppInfoSelectedEventHandlers {
        HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEventHandler handler);
    }

    public static final Type<AppInfoSelectedEventHandler> TYPE = new Type<>();
    private final Splittable app;
    private final String appId;
    private final String systemId;
    private final boolean isPublic;
    private final boolean showQuickLaunchFirst;

    public AppInfoSelectedEvent(String appId,
                                String systemId,
                                boolean isPublic,
                                Splittable app) {
        this(appId, systemId, isPublic, false, app);
    }

    public AppInfoSelectedEvent(String appId,
                                String systemId,
                                boolean isPublic,
                                boolean showQuickLaunchFirst,
                                Splittable app) {
        this.app = app;
        this.appId = appId;
        this.systemId = systemId;
        this.isPublic = isPublic;
        this.showQuickLaunchFirst = showQuickLaunchFirst;
    }

    public Splittable getApp() {
        return app;
    }

    public String getAppId() {
        return appId;
    }

    public String getSystemId() {
        return systemId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isShowQuickLaunchFirst() {
        return showQuickLaunchFirst;
    }

    @Override
    public Type<AppInfoSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppInfoSelectedEventHandler handler) {
        handler.onAppInfoSelected(this);
    }
}
