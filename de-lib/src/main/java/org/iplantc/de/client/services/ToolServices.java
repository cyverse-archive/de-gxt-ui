package org.iplantc.de.client.services;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public interface ToolServices {

    void searchTools(FilterPagingLoadConfig loadConfig, AsyncCallback<List<Tool>> callback);

    void getTools(AsyncCallback<List<Tool>> callback);

    void addTool(Tool tool, AsyncCallback<Tool> callback);

    void deleteTool(Tool tool, AsyncCallback<String> callback);
}
