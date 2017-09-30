package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;

/**
 * {@link FilterPagingLoadConfigBean} for ontology term search services.
 *
 * @author psarando
 */
public class MetadataTermLoadConfig extends FilterPagingLoadConfigBean {

    private String query;

    /**
     * @return The user's search term.
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query The user's search term.
     */
    public void setQuery(String query) {
        this.query = query;
    }
}
