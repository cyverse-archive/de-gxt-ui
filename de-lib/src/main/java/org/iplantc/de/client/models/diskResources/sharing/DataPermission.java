package org.iplantc.de.client.models.diskResources.sharing;

import org.iplantc.de.client.models.HasPath;

/**
 * The autobean representation of a disk resource and permission being applied
 * to that resource
 * @author aramsey
 */
public interface DataPermission extends HasPath {

    void setPath(String path);

    String getPermission();
    void setPermission(String permission);
}
