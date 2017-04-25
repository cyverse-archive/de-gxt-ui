package org.iplantc.de.apps.integration.client.presenter;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

public class ToolSearchRPCProxy extends
        RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Tool>> {

    ToolServices dcService = ServicesInjector.INSTANCE.getDeployedComponentServices();

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<Tool>> callback) {

        dcService.searchTools(loadConfig, new AsyncCallback<List<Tool>>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.dcLoadError(), caught);

            }

            @Override
            public void onSuccess(List<Tool> result) {
                callback.onSuccess(new PagingLoadResultBean<Tool>(result,
                        result.size(), 0));
            }
        });
    }
}
