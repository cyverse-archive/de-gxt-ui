package org.iplantc.de.client.models.apps.sharing;

import org.iplantc.de.client.models.apps.QualifiedAppId;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppPermission extends QualifiedAppId {

    void setPermission(String permission);

    String getPermission();
}
