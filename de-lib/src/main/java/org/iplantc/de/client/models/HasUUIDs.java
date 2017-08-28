package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a JSON object with a "uuid" key and list of string values
 */
public interface HasUUIDs {

    @PropertyName("uuids")
    List<String> getUUIDs();

    @PropertyName("uuids")
    void setUUIDs(List<String> uuids);
}
