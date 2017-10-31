package org.iplantc.de.client.models.webhooks;

import org.iplantc.de.client.models.HasId;

/**
 * Type of webhook. E.g: Slack, Zapier
 *
 * Created by sriram on 10/31/17.
 */
public interface WebhookType extends HasId {

    String getType();

    void setType(String type);

    String getTemplate();

    void setTemplate(String template);

}
