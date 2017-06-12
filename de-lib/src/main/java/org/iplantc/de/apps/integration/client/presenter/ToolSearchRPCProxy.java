package org.iplantc.de.apps.integration.client.presenter;

import org.iplantc.de.apps.client.events.tools.BeforeToolSearchEvent;
import org.iplantc.de.apps.client.events.tools.ToolSearchResultLoadEvent;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.common.base.Strings;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

public class ToolSearchRPCProxy extends
        RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Tool>> {

    ToolServices dcService = ServicesInjector.INSTANCE.getDeployedComponentServices();
    private HasHandlers hasHandlers;

    public void setHasHandlers(HasHandlers hasHandlers){
        this.hasHandlers = hasHandlers;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<Tool>> callback) {
        // Cache the query text.
        String lastQueryText = "";

        // Get the proxy's search params.
        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            lastQueryText = filterConfigs.get(0).getValue();
        }

        if (Strings.isNullOrEmpty(lastQueryText)) {
            // nothing to search
            return;
        }

        // Cache the search text for this callback; used to sort the results.
        final String searchText = lastQueryText;
        hasHandlers.fireEvent(new BeforeToolSearchEvent());
        dcService.searchTools(loadConfig, new AsyncCallback<List<Tool>>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.dcLoadError(), caught);

            }

            @Override
            public void onSuccess(List<Tool> result) {
                callback.onSuccess(new PagingLoadResultBean<Tool>(result,
                        result.size(), 0));
                // The search service accepts * and ? wildcards, so convert them for the pattern group.
                String pattern = "(" + searchText.replace("*", ".*").replace('?', '.') + ")";
                hasHandlers.fireEvent(new ToolSearchResultLoadEvent(searchText, pattern, result));
            }
        });
    }
}
