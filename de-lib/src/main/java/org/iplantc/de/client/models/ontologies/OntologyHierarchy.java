package org.iplantc.de.client.models.ontologies;

import java.util.List;

/**
 * A hierarchy of Ontology Classes.
 *
 * Ontology hierarchy service responses will return the root of a hierarchy under the `hierarchy` key.
 * The root and all classes under it will list their sub-classes under a `subclasses` key.
 *
 * @author aramsey
 */
public interface OntologyHierarchy extends OntologyClass {

    OntologyHierarchy getHierarchy();

    List<OntologyHierarchy> getSubclasses();

    void setSubclasses(List<OntologyHierarchy> subclasses);
}
