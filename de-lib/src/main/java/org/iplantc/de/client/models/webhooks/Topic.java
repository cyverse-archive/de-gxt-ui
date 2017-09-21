package org.iplantc.de.client.models.webhooks;

import org.iplantc.de.client.models.HasId;

/**
 * Created by sriram on 9/21/17.
 *
 * A model that describes webhook subscription topic
 */
public interface Topic extends HasId {

    String getTopic();

    void setTopic(String topic);
}
