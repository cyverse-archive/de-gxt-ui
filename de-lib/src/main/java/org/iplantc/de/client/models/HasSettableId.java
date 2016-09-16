package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * @author dennis
 */
public interface HasSettableId extends HasId {

    @AutoBean.PropertyName(ID_KEY)
    void setId(String id);
}
