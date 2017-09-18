package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * AutoBean model for Ontology Lookup Service search/autocomplete results.
 * Includes the list of search results in its `docs` key.
 *
 * @author psarando
 */
public interface OntologyLookupServiceResults {
    @PropertyName("numFound")
    int getTotal();

    @PropertyName("start")
    int getOffset();

    @PropertyName("docs")
    List<OntologyLookupServiceDoc> getClasses();
}
