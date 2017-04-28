package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.client.services.converters.ToolCallbackConverter;
import org.iplantc.de.client.services.converters.ToolsCallbackConverter;
import org.iplantc.de.shared.services.BaseServiceCallWrapper;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public class ToolServicesImpl implements ToolServices {

    private final String TOOLS = "org.iplantc.services.tools";
    private final ToolAutoBeanFactory factory;
    private final DiscEnvApiService deServiceFacade;

    @Inject
    public ToolServicesImpl(final DiscEnvApiService deServiceFacade, final ToolAutoBeanFactory factory) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
    }

    @Override
    public void searchTools(FilterPagingLoadConfig loadConfig, AsyncCallback<List<Tool>> callback) {
        String address = TOOLS + "?";
        // Get the proxy's search params.
        String searchTerm = null;
        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            searchTerm = filterConfigs.get(0).getValue();
        }
        if (Strings.isNullOrEmpty(searchTerm)) {
            searchTerm = "*";
        }

        SortInfo sortInfo =
                Iterables.getFirst(loadConfig.getSortInfo(), new SortInfoBean("NAME", SortDir.ASC));

        address +=
                "search=" + URL.encodeQueryString(searchTerm) + "&sort-field=" + sortInfo.getSortField()
                                                                                         .toLowerCase()
                + "&sort-dir=" + sortInfo.getSortDir().toString();

        ToolsCallbackConverter callbackCnvt = new ToolsCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

    @Override
    public void getTools(AsyncCallback<List<Tool>> callback) {
        String address = TOOLS;
        ToolsCallbackConverter callbackCnvt = new ToolsCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

    @Override
    public void addTool(Tool tool, AsyncCallback<Tool> callback) {
        String address = TOOLS;
        String newTool = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool)).getPayload();
        ToolCallbackConverter callbackCnvt = new ToolCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper =
                new ServiceCallWrapper(BaseServiceCallWrapper.Type.POST, address, newTool);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

    @Override
    public void deleteTool(Tool tool, AsyncCallback<String> callback) {
        String address = TOOLS + "/" + tool.getId();
        ServiceCallWrapper wrapper =
                new ServiceCallWrapper(BaseServiceCallWrapper.Type.DELETE, address);

        deServiceFacade.getServiceData(wrapper, callback);
    }

}
