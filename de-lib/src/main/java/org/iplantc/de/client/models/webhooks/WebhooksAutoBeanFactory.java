package org.iplantc.de.client.models.webhooks;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * AutoBeanFactory to create models
 * Created by sriram on 9/21/17.
 */
public interface WebhooksAutoBeanFactory extends AutoBeanFactory {

    AutoBean<WebhookList> getWebhookList();

    AutoBean<Webhook> getWebhook();

    AutoBean<Topic> getTopic();

}
