package org.iplantc.de.client.models.tags;

import org.iplantc.de.client.models.HasSettableId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import jsinterop.annotations.JsType;

/**
 * @author jstroot
 */
@JsType
public interface Tag extends HasSettableId {

    @PropertyName("description")
    void setDescription(String desc);

    @PropertyName("description")
    String getDescription();

    @PropertyName("value")
    void setValue(String value);

    @PropertyName("value")
    String getValue();

}
