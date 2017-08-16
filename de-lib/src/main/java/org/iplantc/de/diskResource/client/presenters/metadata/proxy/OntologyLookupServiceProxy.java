package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import org.iplantc.de.client.models.ontologies.OntologyLookupServiceDoc;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResponse;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResults;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

/**
 * Ontology Lookup Service RPC proxy that calls the OLS with a given LoadConfig and LoadResult callback.
 *
 * @author psarando
 */
public class OntologyLookupServiceProxy extends RpcProxy<OntologyLookupServiceLoadConfig, PagingLoadResult<OntologyLookupServiceDoc>> {

    OntologyLookupServiceFacade svcFacade;

    @Inject
    public OntologyLookupServiceProxy(OntologyLookupServiceFacade svcFacade) {
        this.svcFacade = svcFacade;
    }

    @Override
    public void load(OntologyLookupServiceLoadConfig loadConfig, AsyncCallback<PagingLoadResult<OntologyLookupServiceDoc>> callback) {
        String queryText = loadConfig.getQuery();

        if (Strings.isNullOrEmpty(queryText)) {
            // nothing to search
            return;
        }

        svcFacade.searchOntologyLookupService(loadConfig, new AsyncCallback<OntologyLookupServiceResponse>() {
            @Override
            public void onSuccess(OntologyLookupServiceResponse response) {
                OntologyLookupServiceResults results = response.getResults();
                callback.onSuccess(new PagingLoadResultBean<>(results.getClasses(),
                                                              results.getTotal(),
                                                              results.getOffset()));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }
}
