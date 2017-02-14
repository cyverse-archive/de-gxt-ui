package org.iplantc.de.client.models.apps.sharing;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppPermission {

    @PropertyName("app_id")
    String getId();

    @PropertyName("app_id")
    void setId(String id);

    @PropertyName("system_id")
    String getSystemId();

    @PropertyName("system_id")
    void setSystemId(String id);

    void setPermission(String permission);

    String getPermission();
}
