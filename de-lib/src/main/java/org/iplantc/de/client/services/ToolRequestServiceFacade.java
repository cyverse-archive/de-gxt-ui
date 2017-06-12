package org.iplantc.de.client.services;

import org.iplantc.de.client.models.toolRequests.NewToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Objects of this type can provide the tool request remote services.
 */
public interface ToolRequestServiceFacade {

    String TOOL_REQUESTS = "org.iplantc.services.toolRequests";

    /**
     * Asynchronously requests the installation of a tool.
     * 
     * @param request the tool installation request
     * @param callback the callback with the response from the provider
     */
    void requestInstallation(NewToolRequest request, AsyncCallback<ToolRequestDetails> callback);
    
}
