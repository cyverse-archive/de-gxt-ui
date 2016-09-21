package org.iplantc.de.client.models.apps;

import org.iplantc.de.client.models.HasSettableId;

public interface AppRefLink extends HasSettableId {

    void setRefLink(String refLink);

    String getRefLink();

}
