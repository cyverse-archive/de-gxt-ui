package org.iplantc.de.apps.client.events.selection;

import org.iplantc.de.client.models.apps.App;

import com.google.common.base.Preconditions;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 7/25/17.
 *
 * @author sriram
 */
public class PublishAppSelected extends GwtEvent<PublishAppSelected.PublishAppSelectedHandler> {
    public static final Type<PublishAppSelectedHandler> TYPE = new Type<>();
    private final App app;

    public PublishAppSelected(final App app) {
        Preconditions.checkNotNull(app);
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public Type<PublishAppSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PublishAppSelectedHandler handler) {
        handler.onPublishAppSelected(this);
    }

    public interface PublishAppSelectedHandler extends EventHandler {
        void onPublishAppSelected(PublishAppSelected event);
    }

    public interface HasPublishAppSelectedHandlers {
        HandlerRegistration addPublishAppSelectedHandler(PublishAppSelectedHandler handler);
    }
}
