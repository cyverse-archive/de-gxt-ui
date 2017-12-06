package org.iplantc.de.diskResource.client.presenters.grid.proxy;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;

/**
 * A load config specifically for doing searches with the search service
 */
public class QuerySearchLoadConfig extends FilterPagingLoadConfigBean {

    private QueryDSLTemplate template;
    
    public void setTemplate(QueryDSLTemplate template) {
        this.template = template;
    }
    
    public QueryDSLTemplate getTemplate() {
        return template;
    }

}
