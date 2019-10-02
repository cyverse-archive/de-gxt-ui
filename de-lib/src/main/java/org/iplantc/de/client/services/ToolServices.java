package org.iplantc.de.client.services;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.models.tool.sharing.ToolSharingRequestList;
import org.iplantc.de.client.models.tool.sharing.ToolUnSharingRequestList;
import org.iplantc.de.shared.AppsCallback;

import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public interface ToolServices {

    void searchTools(Boolean isPublic, FilterPagingLoadConfig loadConfig, AppsCallback<List<Tool>> callback);

    void searchTools(Boolean isPublic, String searchTerm, String order, String orderBy, int limit, int offset, AppsCallback<Splittable> callback);

    void addTool(Tool tool, AppsCallback<Splittable> callback);

    void deleteTool(String toolId, AppsCallback<Void> callback);

    void getPermissions(List<Tool> currentSelection, AppsCallback<String> callback);

    void shareTool(ToolSharingRequestList obj, AppsCallback<String> callback);

    void unShareTool(ToolUnSharingRequestList obj, AppsCallback<String> callback);

    void updateTool(Tool tool, AppsCallback<Tool> appsCallback);

    void getAppsForTool(String toolId, AppsCallback<List<App>> appsCallback);

    void getToolInfo(String toolId, AppsCallback<Tool> appsCallback);

    void getToolTypes(AppsCallback<List<ToolType>> callback);
}
