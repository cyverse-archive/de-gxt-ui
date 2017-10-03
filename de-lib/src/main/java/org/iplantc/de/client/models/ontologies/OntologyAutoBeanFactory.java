package org.iplantc.de.client.models.ontologies;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author aramsey
 */
public interface OntologyAutoBeanFactory extends AutoBeanFactory {

    AutoBean<Ontology> getOntology();

    AutoBean<OntologyClass> getOntologyClass();

    AutoBean<OntologyHierarchy> getHierarchy();

    AutoBean<OntologyHierarchyList> getHierarchyList();

    AutoBean<OntologyList> getOntologyList();

    AutoBean<OntologyVersionDetail> getVersionDetail();

    AutoBean<MetadataTermSearchResult> getMetadataTermSearchResult();

    AutoBean<OntologyLookupServiceResponse> getOntologyLookupServiceResponse();

    AutoBean<OntologyLookupServiceDoc> getOntologyLookupServiceDoc();

    AutoBean<AstroThesaurusDoc> getAstroThesaurusDoc();

    AutoBean<AstroThesaurusResponse> getAstroThesaurusResponse();

    AutoBean<OntologyLookupServiceQueryParams> getOntologyLookupServiceQueryParams();
}
