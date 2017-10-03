package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams.EntityTypeFilterValue;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MetadataTermLoadConfig} for the Ontology Lookup Service `select` endpoint:
 * http://www.ebi.ac.uk/ols/docs/api#_select_terms
 *
 * @author psarando
 */
public class OntologyLookupServiceLoadConfig extends MetadataTermLoadConfig {
    private static final String FILTER_FIELD_QUERY = "q";
    private static final String FILTER_FIELD_RESPONSE_FIELDS = "fieldList";

    private FastMap<FilterConfig> filterConfigMap = new FastMap<>();

    public OntologyLookupServiceLoadConfig() {
        this(null);
    }

    public OntologyLookupServiceLoadConfig(OntologyLookupServiceQueryParams loaderSettings) {
        filterConfigMap.put(FILTER_FIELD_QUERY,           new FilterConfigBean());
        filterConfigMap.put(FILTER_FIELD_RESPONSE_FIELDS, new FilterConfigBean());
        filterConfigMap.put(OntologyLookupServiceQueryParams.FILTER_FIELD_ONTOLOGY,     new FilterConfigBean());
        filterConfigMap.put(OntologyLookupServiceQueryParams.FILTER_FIELD_ENTITY_TYPE,  new FilterConfigBean());
        filterConfigMap.put(OntologyLookupServiceQueryParams.FILTER_FIELD_CHILDREN,     new FilterConfigBean());
        filterConfigMap.put(OntologyLookupServiceQueryParams.FILTER_FIELD_ALL_CHILDREN, new FilterConfigBean());

        filterConfigMap.forEach((key, filterConfig) -> filterConfig.setField(key));

        initFilterConfigValues(loaderSettings);

        getFilters().addAll(filterConfigMap.values());
    }

    private void initFilterConfigValues(OntologyLookupServiceQueryParams loaderSettings) {
        filterConfigMap.get(FILTER_FIELD_RESPONSE_FIELDS).setValue("id,iri,label,ontology_prefix");

        if (loaderSettings != null) {
            setEntityTypeFilter(loaderSettings.getEntityType());

            setOntologyListFilter(list2ConfigFilterString(loaderSettings.getOntologies()));
            setChildrenFilter(list2ConfigFilterString(loaderSettings.getChildren()));
            setAllChildrenFilter(list2ConfigFilterString(loaderSettings.getAllChildren()));
        }
    }

    private String list2ConfigFilterString(List<String> list) {
        if (list != null && !list.isEmpty()) {
            List<String> filteredList = list.stream()
                                            .filter(s -> !Strings.isNullOrEmpty(s))
                                            .collect(Collectors.toList());

            if (!filteredList.isEmpty()) {
                return Joiner.on(',').join(filteredList);
            }
        }

        return null;
    }

    /**
     * @return The user's search term.
     */
    @Override
    public String getQuery() {
        return filterConfigMap.get(FILTER_FIELD_QUERY).getValue();
    }

    /**
     * @param query The user's search term.
     */
    @Override
    public void setQuery(String query) {
        filterConfigMap.get(FILTER_FIELD_QUERY).setValue(query);
    }

    /**
     *
     * @return A comma-separated list of ontologies e.g. "edam" or "uberon,ma"
     */
    public String getOntologyListFilter() {
        return filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ONTOLOGY).getValue();
    }

    /**
     * Restrict a search to a set of ontologies.
     * http://www.ebi.ac.uk/ols/ontologies
     *
     * @param ontologies A comma-separated list of ontologies (e.g. "edam" or "uberon,ma") or null to clear the filter.
     */
    public void setOntologyListFilter(String ontologies) {
        filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ONTOLOGY).setValue(ontologies);
    }

    /**
     * @return The `type` filter value, or null for no filter.
     */
    public EntityTypeFilterValue getEntityTypeFilter() {
        String value = filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ENTITY_TYPE).getValue();

        return (value == null ? null : EntityTypeFilterValue.valueOf(value.toUpperCase()));
    }

    /**
     * Restrict a search to an entity type.
     *
     * @param entityTypeFilter null to clear the filter.
     */
    public void setEntityTypeFilter(EntityTypeFilterValue entityTypeFilter) {
        String value = (entityTypeFilter == null ? null : entityTypeFilter.toString());

        filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ENTITY_TYPE).setValue(value);
    }

    /**
     * @return The `childrenOf` filter value, or null for no filter.
     */
    public String getChildrenFilter() {
        return filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_CHILDREN).getValue();
    }

    /**
     * You can restrict a search to all children of a given term.
     * Supply a list of IRI for the terms that you want to search under
     * (subclassOf/is-a relation only).
     *
     * @param iriList A comma-separated list of IRIs, or null to clear the filter.
     */
    public void setChildrenFilter(String iriList) {
        filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_CHILDREN).setValue(iriList);
    }

    /**
     * @return The `allChildrenOf` filter value, or null for no filter.
     */
    public String getAllChildrenFilter() {
        return filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ALL_CHILDREN).getValue();
    }

    /**
     * You can restrict a search to all children of a given term.
     * Supply a list of IRI for the terms that you want to search under
     * (subclassOf/is-a plus any hierarchical/transitive properties like 'part of' or 'develops from').
     *
     * @param iriList A comma-separated list of IRIs, or null to clear the filter.
     */
    public void setAllChildrenFilter(String iriList) {
        filterConfigMap.get(OntologyLookupServiceQueryParams.FILTER_FIELD_ALL_CHILDREN).setValue(iriList);
    }
}
