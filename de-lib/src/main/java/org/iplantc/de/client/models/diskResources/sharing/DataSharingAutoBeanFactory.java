package org.iplantc.de.client.models.diskResources.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * The autobean factory for all the sharing autobeans for disk resources
 * @author aramsey
 */
public interface DataSharingAutoBeanFactory extends AutoBeanFactory {

    AutoBean<DataPermission> getDataPermission();

    AutoBean<DataPermissionList> getDataPermissionList();

    AutoBean<DataSharingRequest> getDataSharingRequest();

    AutoBean<DataUserPermission> getDataUserPermission();

    AutoBean<DataUserPermissionList> getDataUserPermissionList();

    AutoBean<DataSharingRequestList> getDataSharingRequestList();

    AutoBean<DataUnsharingRequest> getDataUnsharingRequest();

    AutoBean<DataUnsharingRequestList> getDataUnsharingRequestList();

}
