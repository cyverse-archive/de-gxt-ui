package org.iplantc.de.client.models.diskResources.sharing;

import static com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.sharing.UserPermission;

import java.util.List;

/**
 * The autobean representation of a disk resource path and the associated
 * permissions on that path
 *
 * @author aramsey
 */
public interface DataUserPermission extends HasPath {

    @PropertyName("user-permissions")
    List<UserPermission> getUserPermissions();

    @PropertyName("user-permissions")
    void setUserPermissions(List<UserPermission> userPermissions);

}
