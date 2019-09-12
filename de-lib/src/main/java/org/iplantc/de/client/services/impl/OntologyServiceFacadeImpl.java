package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;

import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.services.converters.AvuListCallbackConverter;
import org.iplantc.de.client.services.converters.OntologyHierarchyCallbackConverter;
import org.iplantc.de.client.services.converters.OntologyHierarchyListCallbackConverter;
import org.iplantc.de.client.services.converters.SplittableDECallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * @author aramsey
 */
public class OntologyServiceFacadeImpl implements OntologyServiceFacade {

    private final String APPS_HIERARCHIES = "org.iplantc.services.apps.hierarchies";
    private final String APPS = "org.iplantc.services.apps";
    @Inject OntologyAutoBeanFactory factory;
    @Inject AvuAutoBeanFactory avuFactory;
    @Inject private DiscEnvApiService deService;
    @Inject AppServiceFacade.AppServiceAutoBeanFactory svcFactory;

    @Override
    public void getRootHierarchies(DECallback<List<OntologyHierarchy>> callback) {
        String address = APPS_HIERARCHIES;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new OntologyHierarchyListCallbackConverter(callback, factory));
    }

    @Override
    public void getFilteredHierarchies(String rootIri, Avu avu, DECallback<OntologyHierarchy> callback) {
        String address = APPS_HIERARCHIES + "/" + URL.encodeQueryString(rootIri) + "?attr=" + URL.encodeQueryString(avu.getAttribute());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new OntologyHierarchyCallbackConverter(callback, factory));
    }

    @Override
    public void getAppsInCategory(String iri,
                                  Avu avu,
                                  AppTypeFilter filter,
                                  DECallback<Splittable> callback) {
        String address = APPS_HIERARCHIES + "/" + URL.encodeQueryString(iri) + "/apps?attr=" + URL.encodeQueryString(avu.getAttribute());
        if (filter != null && (!filter.equals(AppTypeFilter.ALL))) {
            address = address + "&app-type=" + filter.getFilterString();
        }
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SplittableDECallbackConverter(callback));
    }

    @Override
    public void getUnclassifiedAppsInCategory(String iri,
                                              Avu avu,
                                              AppTypeFilter filter,
                                              DECallback<Splittable> callback) {
        String address = APPS_HIERARCHIES + "/" + URL.encodeQueryString(iri) + "/unclassified?attr=" + URL.encodeQueryString(avu.getAttribute());
        if (filter != null && (!filter.equals(AppTypeFilter.ALL))) {
            address = address + "&app-type=" + filter.getFilterString();
        }
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SplittableDECallbackConverter(callback));
    }

    @Override
    public void getAppAVUs(App app, AsyncCallback<List<Avu>> callback) {
        String address = APPS + "/" + app.getId() + "/metadata";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AvuListCallbackConverter(callback, avuFactory));
    }
}
