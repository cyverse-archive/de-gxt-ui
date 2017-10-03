package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * OntologyClass model for an Ontology Lookup Service search result.
 *
 * @author psarando
 */
public interface OntologyLookupServiceDoc extends MetadataTermSearchResult {
    @PropertyName("ontology_prefix")
    String getOntologyPrefix();
}
