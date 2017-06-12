package org.iplantc.de.client.models.tool.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolUnSharingRequestList {

    @AutoBean.PropertyName("unsharing")
    List<ToolUnsharingRequest> getToolUnSharingRequestList();

    @AutoBean.PropertyName("unsharing")
    void setToolUnSharingRequestList(List<ToolUnsharingRequest> unsharinglist);

}
