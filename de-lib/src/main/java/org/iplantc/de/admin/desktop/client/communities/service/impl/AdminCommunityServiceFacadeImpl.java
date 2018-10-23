package org.iplantc.de.admin.desktop.client.communities.service.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;

import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.models.apps.proxy.AppSearchAutoBeanFactory;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.services.converters.GroupListCallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 */
public class AdminCommunityServiceFacadeImpl implements AdminCommunityServiceFacade {

    private final String ADMIN_COMMUNITIES = "org.iplantc.services.admin.communities";
    private final String ADMIN_APPS_COMMUNITIES = "org.iplantc.services.admin.apps.communities";
    @Inject GroupAutoBeanFactory factory;
    @Inject AppSearchAutoBeanFactory appAutoBeanFactory;
    @Inject private DiscEnvApiService deService;

    @Inject
    AdminCommunityServiceFacadeImpl() {
    }

    @Override
    public void getCommunities(AsyncCallback<List<Group>> callback) {
        String address = ADMIN_COMMUNITIES;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new GroupListCallbackConverter(factory, callback));
    }

    @Override
    public void getCommunityApps(Group community, DECallback<List<App>> appListCallback) {
        String address = ADMIN_APPS_COMMUNITIES + "/" + URL.encode(community.getDisplayName()) + "/apps";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new DECallbackConverter<String, List<App>>(appListCallback) {
            @Override
            protected List<App> convertFrom(String object) {
                AppListLoadResult listResult = AutoBeanCodex.decode(appAutoBeanFactory, AppListLoadResult.class, object).as();
                return listResult.getData();
            }
        });
    }
}
