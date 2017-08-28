package org.iplantc.de.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

/**
 * @author aramsey
 */
public interface OauthServiceFacade {

    void getRedirectUris(AsyncCallback<Map<String,String>> callback);

    /**
     * Delete Users Hpc OAuth token
     *
     * @param callback callback object
     */
    void deleteHpcToken(AsyncCallback<Void> callback);
}
