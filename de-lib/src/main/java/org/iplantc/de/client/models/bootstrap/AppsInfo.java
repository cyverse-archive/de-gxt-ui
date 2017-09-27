package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasSplittableError;
import org.iplantc.de.client.models.webhooks.Webhook;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 2/27/17.
 */
public interface AppsInfo extends HasSplittableError {

    @AutoBean.PropertyName("system_ids")
    SystemIds getSystemsIds();

    Workspace getWorkspace();

    List<Webhook> getWebhooks();
}
