package org.iplantc.de.client.models.toolRequests;

import com.google.web.bindery.autobean.shared.AutoBean;

public interface ToolRequestAdminAutoBeanFactory extends ToolRequestAutoBeanFactory {

    AutoBean<ToolRequestList> toolRequestList();

}
