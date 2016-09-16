package org.iplantc.de.client.models.tags;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

/**
 * @author jstroot
 */
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
