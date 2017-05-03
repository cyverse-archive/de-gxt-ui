package org.iplantc.de.client.models.tool.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolSharingRequestList {

    @AutoBean.PropertyName("sharing")
    List<ToolSharingRequest> getToolSharingRequestList();

    @AutoBean.PropertyName("sharing")
    void setToolSharingRequestList(List<ToolSharingRequest> sharinglist);


}
