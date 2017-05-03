package org.iplantc.de.client.services;

import org.iplantc.de.apps.client.presenter.tools.ManageToolsViewPresenter;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.sharing.ToolSharingRequestList;
import org.iplantc.de.client.models.tool.sharing.ToolUnSharingRequestList;
import org.iplantc.de.shared.AppsCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public interface ToolServices {

    void searchTools(FilterPagingLoadConfig loadConfig, AsyncCallback<List<Tool>> callback);

    void getTools(AsyncCallback<List<Tool>> callback);

    void addTool(Tool tool, AsyncCallback<Tool> callback);

    void deleteTool(Tool tool, AsyncCallback<String> callback);

    void getPermissions(List<Tool> currentSelection, AsyncCallback<String> callback);

    void shareTool(ToolSharingRequestList obj, AsyncCallback<String> appsCallback);

    void unShareTool(ToolUnSharingRequestList obj, AsyncCallback<String> appsCallback);
}
