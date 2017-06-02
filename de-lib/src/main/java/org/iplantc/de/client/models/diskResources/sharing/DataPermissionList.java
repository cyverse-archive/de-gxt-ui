package org.iplantc.de.client.models.diskResources.sharing;

import static com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of DataPermission objects
 *
 * @author aramsey
 */
public interface DataPermissionList {
    @PropertyName("paths")
    List<DataPermission> getDataPermissions();

    @PropertyName("paths")
    void setDataPermissions(List<DataPermission> dataPermissions);
}
