package org.iplantc.de.client.models.ontologies;

import java.util.List;

/**
 * @author aramsey
 */
public interface OntologyHierarchy extends OntologyClass {

    OntologyHierarchy getHierarchy();

    List<OntologyHierarchy> getSubclasses();

    void setSubclasses(List<OntologyHierarchy> subclasses);
}
