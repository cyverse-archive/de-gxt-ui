package org.iplantc.de.client.services;

import org.iplantc.de.client.models.identifiers.PermanentIdRequestType;
import org.iplantc.de.shared.DECallback;

public interface PermIdRequestUserServiceFacade {

    public final String PERMID_REQUEST = "org.iplantc.services.permIdRequests";

    void requestPermId(String uuid, PermanentIdRequestType type, DECallback<String> callback);

}
