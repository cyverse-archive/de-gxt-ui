package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.RootLevelMap;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.Map;

/**
 * @author aramsey
 */
public class OauthServiceFacadeImpl implements OauthServiceFacade {

    private final String OAUTH = "org.iplantc.services.oauth";

    private final DiscEnvApiService deServiceFacade;
    private CommonModelAutoBeanFactory factory;
    private final DEClientConstants constants;

    @Inject
    public OauthServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                  CommonModelAutoBeanFactory factory,
                                  final DEClientConstants constants) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
        this.constants = constants;
    }

    @Override
    public void getRedirectUris(AsyncCallback<Map<String, String>> callback) {
        String address = OAUTH + "/redirect-uris";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper, new AsyncCallbackConverter<String, Map<String, String>>(callback) {
            @Override
            protected Map<String, String> convertFrom(String object) {
                RootLevelMap rootLevelMap = AutoBeanCodex.decode(factory,
                                                       RootLevelMap.class,
                                                       RootLevelMap.Payload.get(object)).as();
                return rootLevelMap.getRootMap();
            }
        });
    }

    @Override
    public void deleteHpcToken(AsyncCallback<Void> callback) {
        String address = OAUTH + "/token-info/"
                         + constants.hpcSystemId();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deServiceFacade.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }
}
