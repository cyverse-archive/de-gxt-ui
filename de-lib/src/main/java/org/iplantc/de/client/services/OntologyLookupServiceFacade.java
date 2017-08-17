package org.iplantc.de.client.services;

import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResponse;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Service Facade interface for EBI's Ontology Lookup Service:
 * http://www.ebi.ac.uk/ols/docs/api
 *
 * @author psarando
 */
public interface OntologyLookupServiceFacade {

    /**
     * Searches for the user's term in the OLS, fetching paged results with the given load config.
     *
     * @param loadConfig The load config for fetching paged search results.
     * @param callback The OLS search response callback.
     */
    void searchOntologyLookupService(OntologyLookupServiceLoadConfig loadConfig, AsyncCallback<OntologyLookupServiceResponse> callback);
}
