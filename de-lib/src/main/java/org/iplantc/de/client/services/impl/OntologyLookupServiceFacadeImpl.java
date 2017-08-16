package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;

import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResponse;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;
import java.util.stream.Collectors;

public class OntologyLookupServiceFacadeImpl implements OntologyLookupServiceFacade {
    private final String OLS_BASE_URL = "org.iplantc.services.ontology-lookup-service.base";

    @Inject OntologyAutoBeanFactory factory;
    @Inject private DiscEnvApiService deService;

    @Override
    public void searchOntologyLookupService(OntologyLookupServiceLoadConfig loadConfig, AsyncCallback<OntologyLookupServiceResponse> callback) {
        List<String> queryParams = Lists.newArrayList("rows=" + loadConfig.getLimit(),
                                                      "start=" + loadConfig.getOffset());

        if (loadConfig.getFilters() != null) {
            queryParams.addAll(loadConfig.getFilters()
                                         .stream()
                                         .filter(config -> !Strings.isNullOrEmpty(config.getValue()))
                                         .map(config -> (config.getField() + "="
                                                         + URL.encodeQueryString(config.getValue())))
                                         .collect(Collectors.toList()));
        }

        String queryString = Joiner.on('&').join(queryParams);

        String address = OLS_BASE_URL + "?" + queryString;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, OntologyLookupServiceResponse>(callback) {
            @Override
            protected OntologyLookupServiceResponse convertFrom(String response) {
                return AutoBeanCodex.decode(factory, OntologyLookupServiceResponse.class, response).as();
            }
        });
    }
}
