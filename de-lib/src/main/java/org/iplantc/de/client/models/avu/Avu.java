package org.iplantc.de.client.models.avu;

import org.iplantc.de.client.models.HasId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author aramsey
 */
public interface Avu extends HasId {

    String AVU_BEAN_TAG_MODEL_KEY = "model-key";

    @PropertyName("modified_on")
    Integer getModifiedOn();

    @PropertyName("modified_by")
    String getModifiedBy();

    String getUnit();

    void setUnit(String unit);

    String getValue();

    void setValue(String value);

    @PropertyName("created_on")
    Integer getCreatedOn();

    @PropertyName("created_by")
    String getCreatedBy();

    @PropertyName("attr")
    void setAttribute(String attribute);

    @PropertyName("attr")
    String getAttribute();

    @PropertyName("avus")
    List<Avu> getAvus();

    @PropertyName("avus")
    void setAvus(List<Avu> avus);

}
