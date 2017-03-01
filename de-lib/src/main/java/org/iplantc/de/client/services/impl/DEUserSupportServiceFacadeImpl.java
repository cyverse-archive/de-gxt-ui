package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.services.DEUserSupportServiceFacade;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Created by sriram on 12/1/16.
 */
public class DEUserSupportServiceFacadeImpl implements DEUserSupportServiceFacade {

    private final DEProperties deProperties;
    private final DiscEnvApiService deServiceFacade;

    @Inject
    public DEUserSupportServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                       final DEProperties deProperties) {
        this.deServiceFacade = deServiceFacade;
        this.deProperties = deProperties;
    }

    @Override
    public void submitSupportRequest(Splittable request, AsyncCallback<Void> callback) {
        String addr = deProperties.getSupportServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, addr, request.getPayload());
        deServiceFacade.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }
}
