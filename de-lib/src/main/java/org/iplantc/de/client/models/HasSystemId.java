package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Interface definition for POJOs which have a system ID property.
 *
 * This class is often used with autobeans.
 *
 * @author dennis
 */
public interface HasSystemId {

    String SYSTEM_ID_KEY = "system_id";

    @AutoBean.PropertyName(SYSTEM_ID_KEY)
    String getSystemId();

    @AutoBean.PropertyName(SYSTEM_ID_KEY)
    void setSystemId(String systemId);
}
