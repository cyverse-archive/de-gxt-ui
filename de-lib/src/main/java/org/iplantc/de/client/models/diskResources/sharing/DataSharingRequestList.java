package org.iplantc.de.client.models.diskResources.sharing;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of DataSharingRequest objects
 *
 * @author aramsey
 */
public interface DataSharingRequestList {
    @PropertyName("sharing")
    List<DataSharingRequest> getDataSharingRequestList();

    @PropertyName("sharing")
    void setDataSharingRequestList(List<DataSharingRequest> dataSharingRequests);
}
