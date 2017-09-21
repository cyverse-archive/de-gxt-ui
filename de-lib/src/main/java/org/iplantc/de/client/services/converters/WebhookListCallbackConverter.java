package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.webhooks.WebhookList;
import org.iplantc.de.client.models.webhooks.WebhooksAutoBeanFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * Created by sriram on 9/21/17.
 */
public class WebhookListCallbackConverter extends AsyncCallbackConverter<String, WebhookList> {
    private final WebhooksAutoBeanFactory factory;

    public WebhookListCallbackConverter(AsyncCallback<WebhookList> callback,
                                        WebhooksAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }


    @Override
    protected WebhookList convertFrom(String object) {
        AutoBean<WebhookList> autoBean = AutoBeanCodex.decode(factory, WebhookList.class, object);
        return autoBean.as();
    }
}

