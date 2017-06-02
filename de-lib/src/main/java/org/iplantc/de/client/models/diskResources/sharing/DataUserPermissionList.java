package org.iplantc.de.client.models.diskResources.sharing;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of DataUserPermission objects
 *
 * @author aramsey
 */
public interface DataUserPermissionList {

    @PropertyName("paths")
    List<DataUserPermission> getDataUserPermissions();

    @PropertyName("paths")
    void setDataUserPermissions(List<DataUserPermission> dataUserPermissions);
}
