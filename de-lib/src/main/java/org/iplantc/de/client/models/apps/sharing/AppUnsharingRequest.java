package org.iplantc.de.client.models.apps.sharing;

import java.util.List;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppUnsharingRequest {

    void setUser(String user);

    void setApps(List<AppPermission> apps);

    String getUser();

    List<AppPermission> getApps();
}
