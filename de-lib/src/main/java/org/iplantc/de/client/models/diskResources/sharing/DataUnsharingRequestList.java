package org.iplantc.de.client.models.diskResources.sharing;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of DataUnsharingRequest objects
 * @author aramsey
 */
public interface DataUnsharingRequestList {

    @PropertyName("unshare")
    List<DataUnsharingRequest> getDataUnsharingRequests();

    @PropertyName("unshare")
    void setDataUnsharingRequests(List<DataUnsharingRequest> dataUnsharingRequests);
}
