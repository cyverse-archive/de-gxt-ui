package org.iplantc.de.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Created by sriram on 12/1/16.
 */
public interface DEUserSupportServiceFacade {
    /**
     * Submits support request on behalf of the user.
     *
     * @param request the request in the form of a JSON object.
     * @param callback executed when the RPC call completes.
     */
    void submitSupportRequest(Splittable request, AsyncCallback<Void> callback);
}
