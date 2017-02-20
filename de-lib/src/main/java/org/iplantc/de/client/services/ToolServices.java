package org.iplantc.de.client.services;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public interface ToolServices {

    void getDeployedComponents(FilterPagingLoadConfig loadConfig, AsyncCallback<List<Tool>> callback);
}
