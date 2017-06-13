package org.iplantc.de.client.models.tool.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * Created by sriram.
 */
public interface ToolSharingAutoBeanFactory extends AutoBeanFactory {

    AutoBean<ToolUserPermissionsList> resourceUserPermissionsList();

    AutoBean<ToolUserPermissions> resourceUserPermissions();

    AutoBean<ToolPermissionsRequest> ToolPermissionsRequest();

    AutoBean<ToolPermission> ToolPermission();

    AutoBean<ToolSharingRequest> toolSharingRequest();

    AutoBean<ToolSharingRequestList> toolSharingRequestList();

    AutoBean<ToolUnsharingRequest> toolUnSharingRequest();
    
    AutoBean<ToolUnSharingRequestList> toolUnSharingRequestList();

    AutoBean<ToolPermission> toolPermission();

    AutoBean<SharingSubject> getSharingSubject();
}
