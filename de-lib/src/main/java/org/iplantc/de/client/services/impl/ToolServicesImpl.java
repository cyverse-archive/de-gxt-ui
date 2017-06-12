package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.sharing.ToolPermissionsRequest;
import org.iplantc.de.client.models.tool.sharing.ToolSharingAutoBeanFactory;
import org.iplantc.de.client.models.tool.sharing.ToolSharingRequestList;
import org.iplantc.de.client.models.tool.sharing.ToolUnSharingRequestList;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.client.services.converters.ToolCallbackConverter;
import org.iplantc.de.client.services.converters.ToolsCallbackConverter;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.services.BaseServiceCallWrapper;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

import java.util.ArrayList;
import java.util.List;

public class ToolServicesImpl implements ToolServices {

    private final String TOOLS = "org.iplantc.services.tools";
    private final ToolAutoBeanFactory factory;
    private final DiscEnvApiService deServiceFacade;

    @Inject
    ToolSharingAutoBeanFactory sharingFactory;

    @Inject
    public ToolServicesImpl(final DiscEnvApiService deServiceFacade, final ToolAutoBeanFactory factory) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
    }

    @Override
    public void searchTools(Boolean isPublic, FilterPagingLoadConfig loadConfig, AppsCallback<List<Tool>> callback) {
        String address = TOOLS + "?";
        // Get the proxy's search params.
        String searchTerm = null;
        if(loadConfig != null) {
            List<FilterConfig> filterConfigs = loadConfig.getFilters();
            if (filterConfigs != null && !filterConfigs.isEmpty()) {
                searchTerm = filterConfigs.get(0).getValue();
            }
            if (Strings.isNullOrEmpty(searchTerm)) {
                searchTerm = "*";
            }

            SortInfo sortInfo =
                    Iterables.getFirst(loadConfig.getSortInfo(), new SortInfoBean("NAME", SortDir.ASC));

            address += "search=" + URL.encodeQueryString(searchTerm) + "&sort-field=" + sortInfo.getSortField().toLowerCase()
                       + "&sort-dir=" + sortInfo.getSortDir().toString();
        }

        if(isPublic != null) {
            address += "&public=" + isPublic;
        }


        ToolsCallbackConverter callbackCnvt = new ToolsCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

    @Override
    public void addTool(Tool tool, AppsCallback<Tool> callback) {
        String address = TOOLS;
        String newTool = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool)).getPayload();
        ToolCallbackConverter callbackCnvt = new ToolCallbackConverter(callback, factory);
        ServiceCallWrapper wrapper =
                new ServiceCallWrapper(BaseServiceCallWrapper.Type.POST, address, newTool);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

    @Override
    public void deleteTool(Tool tool, AppsCallback<String> callback) {
        String address = TOOLS + "/" + tool.getId();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(BaseServiceCallWrapper.Type.DELETE, address);

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getPermissions(List<Tool> currentSelection, AppsCallback<String> callback) {
        String address = TOOLS + "/" + "permission-lister";
        List<String> toolPermissionList = new ArrayList<>();

        for (Tool t : currentSelection) {
            toolPermissionList.add(t.getId());
        }

        final AutoBean<ToolPermissionsRequest> requestAutoBean = sharingFactory.ToolPermissionsRequest();
        requestAutoBean.as().setTools(toolPermissionList);
        final Splittable requestJson = AutoBeanCodex.encode(requestAutoBean);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(BaseServiceCallWrapper.Type.POST,
                                                            address,
                                                            requestJson.getPayload());
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void shareTool(ToolSharingRequestList obj, AppsCallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(obj)).getPayload();
        GWT.log("tool sharing request:" + payload);
        String address = TOOLS + "/" + "sharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void unShareTool(ToolUnSharingRequestList obj, AppsCallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(obj)).getPayload();
        GWT.log("tool un-sharing request:" + payload);
        String address = TOOLS + "/" + "unsharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void updateTool(Tool tool, AppsCallback<Tool> appsCallback) {
        String address = TOOLS + "/" + tool.getId();
        String newTool = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool)).getPayload();
        ToolCallbackConverter callbackCnvt = new ToolCallbackConverter(appsCallback, factory);
        ServiceCallWrapper wrapper =
                new ServiceCallWrapper(BaseServiceCallWrapper.Type.PATCH, address, newTool);

        deServiceFacade.getServiceData(wrapper, callbackCnvt);
    }

}
