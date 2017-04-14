package org.iplantc.de.client.models.notifications.payload;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * An AutoBean interface containing a tool's information provided within a notification
 *
 * @author aramsey
 */
public interface PayloadTool {

    @AutoBean.PropertyName("tool_id")
    String getToolId();

    @AutoBean.PropertyName("tool_name")
    String getToolName();
}
