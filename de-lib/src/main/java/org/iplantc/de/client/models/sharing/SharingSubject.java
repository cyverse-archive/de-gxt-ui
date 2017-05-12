package org.iplantc.de.client.models.sharing;

import org.iplantc.de.client.models.HasSettableId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * The autobean representation of the subject object in a sharing request
 * @author aramsey
 */
public interface SharingSubject extends HasSettableId {

    @PropertyName("source_id")
    String getSourceId();

    @PropertyName("source_id")
    void setSourceId(String sourceId);
}
