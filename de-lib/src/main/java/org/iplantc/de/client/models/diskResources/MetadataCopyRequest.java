package org.iplantc.de.client.models.diskResources;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The AutoBean representation of the expected request body for copying metadata
 */
public interface MetadataCopyRequest {

    @PropertyName("destination_ids")
    void setDestinationIds(List<String> ids);

    @PropertyName("destination_ids")
    List<String> getDestinationIds();
}
