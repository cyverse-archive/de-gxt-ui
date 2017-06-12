package org.iplantc.de.client.models.tool.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolSharingRequest {

    String getUser();

    @AutoBean.PropertyName("tools")
    List<ToolPermission> getToolPermissions();

    void setUser(String user);

    @AutoBean.PropertyName("tools")
    void setToolPermissions(List<ToolPermission> toolPermissions);
}
