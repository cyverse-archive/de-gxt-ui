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

    @AutoBean.PropertyName("system_id")
    String getSystemId();
}
