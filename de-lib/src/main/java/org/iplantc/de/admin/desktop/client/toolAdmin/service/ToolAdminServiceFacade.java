package org.iplantc.de.admin.desktop.client.toolAdmin.service;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author aramsey
 */
public interface ToolAdminServiceFacade {

    void getTools(String searchTerm, AsyncCallback<List<Tool>> callback);

    void getToolDetails(String toolId, AsyncCallback<Tool> callback);

    void addTool(ToolList toolList, AsyncCallback<Void> callback);

    void updateTool(Tool tool, boolean overwrite, AsyncCallback<Void> callback);

    void deleteTool(String toolId, AsyncCallback<Void> callback);

    void publishTool(Tool tool, AsyncCallback<Void> callback);

}
