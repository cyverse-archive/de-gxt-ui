package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * AutoBean model for Ontology Lookup Service search/autocomplete responses.
 *
 * @author psarando
 */
public interface OntologyLookupServiceResponse {

    @PropertyName("response")
    OntologyLookupServiceResults getResults();
}
