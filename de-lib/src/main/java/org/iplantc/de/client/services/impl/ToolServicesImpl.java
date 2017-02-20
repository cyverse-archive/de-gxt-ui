package org.iplantc.de.client.services.impl;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.client.services.converters.GetDeployedComponentsCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.collect.Iterables;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.List;

public class ToolServicesImpl implements ToolServices {

//    private final String COMPONENTS = "org.iplantc.services.apps.elements.tools";
    private final String TOOLS = "org.iplantc.services.tools";
    private final ToolAutoBeanFactory factory;
    private final DiscEnvApiService deServiceFacade;

    @Inject
    public ToolServicesImpl(final DiscEnvApiService deServiceFacade,
                                         final ToolAutoBeanFactory factory) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
    }

    @Override
    public void getDeployedComponents(FilterPagingLoadConfig loadConfig,
                                      AsyncCallback<List<Tool>> callback) {
        String address = TOOLS + "?";
        // Get the proxy's search params.
        String searchTerm;
        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            searchTerm = filterConfigs.get(0).getValue();
        } else {
            searchTerm = "*";
        }

        SortInfo sortInfo =
                Iterables.getFirst(loadConfig.getSortInfo(), new SortInfoBean("NAME", SortDir.ASC));

        address += "search=" + URL.encodeQueryString(searchTerm)
                   + "&sort-field=" + sortInfo.getSortField().toLowerCase()
                   + "&sort-dir=" + sortInfo.getSortDir().toString();

        GetDeployedComponentsCallbackConverter callbackCnvt = new GetDeployedComponentsCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

}
