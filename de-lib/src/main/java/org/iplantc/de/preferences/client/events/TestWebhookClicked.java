package org.iplantc.de.preferences.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event description of test webhook clicked from preference dialog
 * Created by sriram on 9/5/17.
 */
public class TestWebhookClicked
        extends GwtEvent<TestWebhookClicked.TestWebhookClickedHandler> {


    private final String url;

    public TestWebhookClicked(String url)  {
       this.url = url;
    }

    public static Type<TestWebhookClickedHandler> TYPE = new Type<>();

    @Override
    public Type<TestWebhookClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TestWebhookClickedHandler testWebhookClickedHandler) {
        testWebhookClickedHandler.onTestClicked(this);
    }

    public String getUrl() {
        return url;
    }

    public interface TestWebhookClickedHandler extends EventHandler {
        void onTestClicked(TestWebhookClicked event);
    }

    public interface HasTestWebhookClickedHandlers {
        HandlerRegistration addTestWebhookClickedHandlers(TestWebhookClickedHandler handler);
    }
    
}
