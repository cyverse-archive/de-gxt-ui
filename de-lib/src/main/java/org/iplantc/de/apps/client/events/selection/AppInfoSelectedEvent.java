package org.iplantc.de.apps.client.events.selection;

import org.iplantc.de.client.models.apps.App;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

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
    private final App app;
    private final boolean showQuickLaunchFirst;

    public AppInfoSelectedEvent(final App app) {
        this(app, false);
    }

    public AppInfoSelectedEvent(App app, boolean showQuickLaunchFirst) {
        Preconditions.checkNotNull(app);
        this.app = app;
        this.showQuickLaunchFirst = showQuickLaunchFirst;
    }

    public App getApp() {
        return app;
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
