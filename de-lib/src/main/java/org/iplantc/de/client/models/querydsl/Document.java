package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.sharing.PermissionValue;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * The autobean representation of a "hit" in an Elasticsearch query response.
 */
public interface Document {

    String FILE_TYPE = "file";
    String FOLDER_TYPE = "folder";

    @PropertyName("_score")
    Double getScore();
    @PropertyName("_score")
    void setScore(Double score);

    @PropertyName("_type")
    String getType();
    @PropertyName("_type")
    void setType(String type);

    @PropertyName("_id")
    String getId();
    @PropertyName("_id")
    void setId(String id);

    Splittable getHighlight();
    void setHighlight(Splittable highlight);

    @PropertyName("_source")
    Source getSource();
    @PropertyName("_source")
    void setSource(Source source);

    Fields getFields();

    /**
     * This can be converted into a model if needed - inner_hits can contain CyVerse metadata
     * @return
     */
    @PropertyName("inner_hits")
    Splittable getInnerHits();
}
