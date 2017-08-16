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

    void searchOntologyLookupService(OntologyLookupServiceLoadConfig loadConfig, AsyncCallback<OntologyLookupServiceResponse> callback);
}
