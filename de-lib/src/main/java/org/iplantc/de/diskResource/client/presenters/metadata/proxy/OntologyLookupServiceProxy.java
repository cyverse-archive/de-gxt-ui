package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import org.iplantc.de.client.models.ontologies.MetadataTermSearchResult;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResponse;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResults;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

/**
 * {@link MetadataTermSearchProxy} that calls the Ontology Lookup Service.
 *
 * @author psarando
 */
public class OntologyLookupServiceProxy extends MetadataTermSearchProxy {

    OntologyLookupServiceFacade svcFacade;

    @Inject
    public OntologyLookupServiceProxy(OntologyLookupServiceFacade svcFacade) {
        this.svcFacade = svcFacade;
    }

    @Override
    public void load(MetadataTermLoadConfig loadConfig,
                     AsyncCallback<PagingLoadResult<MetadataTermSearchResult>> callback) {
        String queryText = loadConfig.getQuery();

        if (Strings.isNullOrEmpty(queryText)) {
            // nothing to search
            callback.onSuccess(new PagingLoadResultBean<>());

            return;
        }

        OntologyLookupServiceLoadConfig olsLoadConfig = (OntologyLookupServiceLoadConfig)loadConfig;
        svcFacade.searchOntologyLookupService(olsLoadConfig, new AsyncCallback<OntologyLookupServiceResponse>() {
            @Override
            public void onSuccess(OntologyLookupServiceResponse response) {
                OntologyLookupServiceResults results = response.getResults();
                List<MetadataTermSearchResult> resultList = Lists.newArrayList(results.getClasses());
                callback.onSuccess(new PagingLoadResultBean<>(resultList, results.getTotal(), results.getOffset()));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }
}
