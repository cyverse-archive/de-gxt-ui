package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;

/**
 * {@link MetadataTermLoadConfig} for the Unified Astronomy Thesaurus `concept` endpoint:
 * https://documentation.ands.org.au/display/DOC/Linked+Data+API
 *
 * @author psarando
 */
public class AstroThesaurusLoadConfig extends MetadataTermLoadConfig {
    private static final String FILTER_FIELD_QUERY = "labelcontains";
    private static final String FILTER_FIELD_SORT = "_sort";

    private FastMap<FilterConfig> filterConfigMap = new FastMap<>();

    public AstroThesaurusLoadConfig() {
        filterConfigMap.put(FILTER_FIELD_QUERY, new FilterConfigBean());
        filterConfigMap.put(FILTER_FIELD_SORT, new FilterConfigBean());

        filterConfigMap.forEach((key, filterConfig) -> filterConfig.setField(key));

        getFilters().addAll(filterConfigMap.values());
    }

    /**
     * @return The user's search term.
     */
    public String getQuery() {
        return filterConfigMap.get(FILTER_FIELD_QUERY).getValue();
    }

    /**
     * @param query The user's search term.
     */
    public void setQuery(String query) {
        filterConfigMap.get(FILTER_FIELD_QUERY).setValue(query);
    }

    /**
     * @return A comma-separated list of property paths to values that should be sorted on.
     *         A `-` prefix on a property path indicates a descending search.
     */
    public String getSort() {
        return filterConfigMap.get(FILTER_FIELD_QUERY).getValue();
    }

    /**
     * @param sort A comma-separated list of property paths to values that should be sorted on.
     *             A `-` prefix on a property path indicates a descending search.
     */
    public void setSort(String sort) {
        filterConfigMap.get(FILTER_FIELD_QUERY).setValue(sort);
    }
}
