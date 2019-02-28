package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.toolRequests.NewToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestAutoBeanFactory;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.services.ToolRequestServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Uses the backend services to provide the tool request services.
 * 
 */
public final class ToolRequestServiceFacadeImpl implements ToolRequestServiceFacade {

    @Inject private ToolRequestAutoBeanFactory factory;
    @Inject private DiscEnvApiService deServiceFacade;

    @Inject
    public ToolRequestServiceFacadeImpl(){ }

    @Override
    public void requestInstallation(final Splittable request,
                                    final AsyncCallback<ToolRequestDetails> callback) {
        final String address = TOOL_REQUESTS;
        final String body = request.getPayload();
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, address, body);
        final AsyncCallback<String> convCB = new AsyncCallbackConverter<String, ToolRequestDetails>(callback) {
            @Override
            protected ToolRequestDetails convertFrom(final String json) {
                return AutoBeanCodex.decode(factory, ToolRequestDetails.class, json).as();
            }};
        deServiceFacade.getServiceData(wrapper, convCB);
    }

}
