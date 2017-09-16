package org.iplantc.de.client.models.ontologies;

import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * An AutoBean model of {@link MetadataTemplateAttribute} settings for the {@link OntologyLookupServiceLoadConfig}.
 *
 * @author psarando
 */
public interface OntologyLookupServiceQueryParams {
    String FILTER_FIELD_ONTOLOGY = "ontology";
    String FILTER_FIELD_ENTITY_TYPE = "type";
    String FILTER_FIELD_CHILDREN = "childrenOf";
    String FILTER_FIELD_ALL_CHILDREN = "allChildrenOf";

    /**
     * OLS searches may be restricted to one of these entity types.
     */
    enum EntityTypeFilterValue {
        CLASS,
        PROPERTY,
        INDIVIDUAL,
        ONTOLOGY;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * OLS searches may be restricted to a set of ontologies.
     *
     * @return A list of OLS ontology IDs.
     */
    @PropertyName(FILTER_FIELD_ONTOLOGY)
    List<String> getOntologies();

    /**
     * Restricts searches to a set of ontologies.
     *
     * @param ontologies A list of OLS ontology IDs.
     */
    @PropertyName(FILTER_FIELD_ONTOLOGY)
    void setOntologies(List<String> ontologies);

    /**
     * OLS searches may be restricted to one entity type.
     */
    @PropertyName(FILTER_FIELD_ENTITY_TYPE)
    EntityTypeFilterValue getEntityType();

    /**
     * Restricts searches to an entity type.
     *
     * @param entityType
     */
    @PropertyName(FILTER_FIELD_ENTITY_TYPE)
    void setEntityType(EntityTypeFilterValue entityType);

    /**
     * OLS searches may be restricted to all children of a given term (subclassOf/is-a relation only).
     *
     * @return A list of IRI for the terms to search under.
     */
    @PropertyName(FILTER_FIELD_CHILDREN)
    List<String> getChildren();

    /**
     * Restricts searches to all children of a given term (subclassOf/is-a relation only).
     *
     * @param children A list of IRI for the terms to search under.
     */
    @PropertyName(FILTER_FIELD_CHILDREN)
    void setChildren(List<String> children);

    /**
     * OLS searches may be restricted to all children of a given term
     * (subclassOf/is-a plus any hierarchical/transitive properties like 'part of' or 'develops from').
     *
     * @return A list of IRI for the terms to search under.
     */
    @PropertyName(FILTER_FIELD_ALL_CHILDREN)
    List<String> getAllChildren();

    /**
     * Restrict searches to all children of a given term
     * (subclassOf/is-a plus any hierarchical/transitive properties like 'part of' or 'develops from').
     *
     * @param allChildren A list of IRI for the terms to search under.
     */
    @PropertyName(FILTER_FIELD_ALL_CHILDREN)
    void setAllChildren(List<String> allChildren);
}
