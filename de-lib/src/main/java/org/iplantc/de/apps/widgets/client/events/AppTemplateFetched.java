package org.iplantc.de.apps.widgets.client.events;

import org.iplantc.de.client.models.apps.integration.AppTemplate;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class AppTemplateFetched extends GwtEvent<AppTemplateFetched.AppTemplateFetchedHandler> {
    public static interface AppTemplateFetchedHandler extends EventHandler {
        void onAppTemplateFetched(AppTemplateFetched event);
    }

    public interface HasAppTemplateFetchedHandlers {
        HandlerRegistration addAppTemplateFetchedHandler(AppTemplateFetchedHandler handler);
    }

    private AppTemplate template;

    public AppTemplateFetched(AppTemplate template) {
        this.template = template;

    }

    public static Type<AppTemplateFetchedHandler> TYPE = new Type<AppTemplateFetchedHandler>();

    public Type<AppTemplateFetchedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AppTemplateFetchedHandler handler) {
        handler.onAppTemplateFetched(this);
    }


    public AppTemplate getTemplate() {
        return template;
    }
}
