package org.iplantc.de.preferences.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event description of Add Slack webhook clicked from preference dialog
 * Created by sriram on 9/5/17.
 */
public class AddSlackWebhookClicked
        extends GwtEvent<AddSlackWebhookClicked.AddSlackWebhookClickedHandler> {


    private final String url;

    public AddSlackWebhookClicked(String url)  {
       this.url = url;
    }

    public static Type<AddSlackWebhookClickedHandler> TYPE = new Type<>();

    @Override
    public Type<AddSlackWebhookClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddSlackWebhookClickedHandler addSlackWebhookClickedHandler) {
        addSlackWebhookClickedHandler.onAddSlackClicked(this);
    }

    public String getUrl() {
        return url;
    }

    public interface AddSlackWebhookClickedHandler extends EventHandler {
        void onAddSlackClicked(AddSlackWebhookClicked event);
    }

    public interface HaAddSlackWebhookClickedHandlers {
        HandlerRegistration addAddSlackWebhookClickedHandlers(AddSlackWebhookClickedHandler handler);
    }
    
}
