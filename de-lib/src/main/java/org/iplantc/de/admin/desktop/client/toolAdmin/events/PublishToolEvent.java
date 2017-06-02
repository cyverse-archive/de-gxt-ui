package org.iplantc.de.admin.desktop.client.toolAdmin.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 6/1/17.
 */
public class PublishToolEvent extends GwtEvent<PublishToolEvent.PublishToolEventHandler> {

    public static Type<PublishToolEventHandler> TYPE = new Type<>();

    private String toolId;

    public PublishToolEvent(String toolId) {
        this.toolId = toolId;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public interface PublishToolEventHandler extends EventHandler {
        void onPublish(PublishToolEvent event);
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PublishToolEventHandler eventHandler) {
        eventHandler.onPublish(this);
    }

    public interface HasPublishToolEventHandlers {
        HandlerRegistration addPublishToolEventHandler(PublishToolEventHandler handler);
    }
}
