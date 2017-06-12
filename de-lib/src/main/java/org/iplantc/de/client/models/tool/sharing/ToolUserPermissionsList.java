package org.iplantc.de.client.models.tool.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolUserPermissionsList {

    @AutoBean.PropertyName("tools")
    List<ToolUserPermissions> getResourceUserPermissionsList();
}
