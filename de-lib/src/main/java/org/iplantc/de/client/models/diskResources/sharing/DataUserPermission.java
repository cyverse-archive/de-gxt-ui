package org.iplantc.de.client.models.diskResources.sharing;

import static com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.sharing.OldUserPermission;

import java.util.List;

/**
 * The autobean representation of a disk resource path and the associated
 * permissions on that path
 *
 * @author aramsey
 */
public interface DataUserPermission extends HasPath {

    @PropertyName("user-permissions")
    List<OldUserPermission> getUserPermissions();

    @PropertyName("user-permissions")
    void setUserPermissions(List<OldUserPermission> userPermissions);

}
