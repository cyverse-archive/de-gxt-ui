package org.iplantc.de.admin.desktop.client.toolRequest.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 5/31/17.
 */
public class AdminMakeToolPublicSelectedEvent
        extends GwtEvent<AdminMakeToolPublicSelectedEvent.AdminMakeToolPublicSelectedEventHandler> {
    private String toolId;

    public AdminMakeToolPublicSelectedEvent(String toolId) {
        this.toolId = toolId;
    }

    public String getToolId() {
        return toolId;
    }

    public interface AdminMakeToolPublicSelectedEventHandler extends EventHandler {
        void onAdminMakeToolPublicSelected(AdminMakeToolPublicSelectedEvent event);
    }

    public interface HasAdminMakeToolPublicEventHandlers {
        HandlerRegistration addAdminMakeToolPublicEventHandler(AdminMakeToolPublicSelectedEvent.AdminMakeToolPublicSelectedEventHandler handler);
    }

    public static GwtEvent.Type<AdminMakeToolPublicSelectedEvent.AdminMakeToolPublicSelectedEventHandler>
            TYPE = new GwtEvent.Type<>();

    public GwtEvent.Type<AdminMakeToolPublicSelectedEvent.AdminMakeToolPublicSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AdminMakeToolPublicSelectedEventHandler handler) {
          handler.onAdminMakeToolPublicSelected(this);
    }

}
