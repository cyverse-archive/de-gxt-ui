package org.iplantc.de.client.models.toolRequests;

import org.iplantc.de.client.models.requestStatus.RequestHistory;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ToolRequestAutoBeanFactory extends AutoBeanFactory {
    
    AutoBean<ToolRequest> toolRequest();
    
    AutoBean<ToolRequestDetails> details();
    
    AutoBean<RequestHistory> history();
    
    AutoBean<ToolRequestUpdate> update();

    AutoBean<NewToolRequest> makeNewToolRequest();
}
