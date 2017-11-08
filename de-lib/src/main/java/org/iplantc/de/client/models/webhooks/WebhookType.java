package org.iplantc.de.client.models.webhooks;

/**
 * Type of webhook. E.g: Slack, Zapier
 *
 * Created by sriram on 10/31/17.
 */
public interface WebhookType {

    String getType();

    void setType(String type);

}
