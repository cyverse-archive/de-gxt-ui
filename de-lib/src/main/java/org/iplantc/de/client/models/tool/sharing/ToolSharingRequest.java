package org.iplantc.de.client.models.tool.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolSharingRequest {

    SharingSubject getSubject();

    @AutoBean.PropertyName("tools")
    List<ToolPermission> getToolPermissions();

    void setSubject(SharingSubject subject);

    @AutoBean.PropertyName("tools")
    void setToolPermissions(List<ToolPermission> toolPermissions);
}
