package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.sharing.OldUserPermission;

import java.util.List;

/**
 * The autobean representation of a list of permissions returned in an Elasticsearch query response
 */
public interface DocumentUserPermissionList {

    List<OldUserPermission> getUserPermissions();
    void setUserPermissions(List<OldUserPermission> userPermissions);
}
