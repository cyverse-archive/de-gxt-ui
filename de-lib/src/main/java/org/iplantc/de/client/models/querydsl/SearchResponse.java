package org.iplantc.de.client.models.querydsl;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Autobean representation of the response received back from calling the query dsl search service
 */
public interface SearchResponse {

    int getTotal();

    @PropertyName("max_score")
    Double getMaxScore();

    @PropertyName("hits")
    List<Document> getHits();
}
