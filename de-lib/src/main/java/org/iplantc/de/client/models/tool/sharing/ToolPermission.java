package org.iplantc.de.client.models.tool.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Created by sriram.
 */
public interface ToolPermission {

    void setPermission(String permission);

    String getPermission();

    @AutoBean.PropertyName("tool_id")
    void setId(String id);

    @AutoBean.PropertyName("tool_id")
    String getId();
}
