package org.iplantc.de.client.models.webhooks;

import org.iplantc.de.client.models.HasId;

import java.util.List;

/**
 * Created by sriram on 9/21/17.
 *
 * A model that describes a webhook
 */
public interface Webhook extends HasId {

    void setId(String id);

    String getUrl();

    void setUrl(String url);

    void setType(WebhookType type);

    WebhookType getType();

    List<String> getTopics();

    void setTopics(List<String> topics);

}
