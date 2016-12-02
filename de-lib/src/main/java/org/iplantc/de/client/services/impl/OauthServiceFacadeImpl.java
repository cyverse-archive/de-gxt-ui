package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.RootLevelMap;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
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

    @Inject
    public OauthServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                  CommonModelAutoBeanFactory factory) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
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
}
