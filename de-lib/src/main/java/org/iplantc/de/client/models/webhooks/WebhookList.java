package org.iplantc.de.client.models.webhooks;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 9/21/17.
 */
public interface WebhookList {

    @AutoBean.PropertyName("webhooks")
    List<Webhook> getWebhooks();

    @AutoBean.PropertyName("webhooks")
    void setWebhooks(List<Webhook> hooks);
}
