package org.iplantc.de.client.models.ontologies;

import org.iplantc.de.client.models.HasId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface OntologyLookupServiceDoc extends OntologyClass, HasId {
    @PropertyName("ontology_prefix")
    String getOntologyPrefix();
}
