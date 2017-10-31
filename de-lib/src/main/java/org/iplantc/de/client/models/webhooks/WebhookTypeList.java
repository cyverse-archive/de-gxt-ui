package org.iplantc.de.client.models.webhooks;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * A list of webhook type
 *
 * Created by sriram on 10/31/17.
 */
public interface WebhookTypeList {

    @AutoBean.PropertyName("webhooktypes")
    List<WebhookType> getTypes();
}
