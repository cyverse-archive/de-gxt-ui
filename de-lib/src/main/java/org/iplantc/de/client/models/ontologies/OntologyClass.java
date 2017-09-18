package org.iplantc.de.client.models.ontologies;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasLabel;

/**
 * An AutoBean model for an Ontology Class, which has a unique IRI, a label, and a description.
 *
 * @author psarando
 */
public interface OntologyClass extends HasLabel, HasDescription {
    String getIri();

    void setIri(String iri);
}
