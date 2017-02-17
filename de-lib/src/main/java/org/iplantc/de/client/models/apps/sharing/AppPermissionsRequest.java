package org.iplantc.de.client.models.apps.sharing;

import java.util.List;

/**
 * @author psarando
 */
public interface AppPermissionsRequest {

    List<AppPermission> getApps();

    void setApps(List<AppPermission> appPerms);
}
