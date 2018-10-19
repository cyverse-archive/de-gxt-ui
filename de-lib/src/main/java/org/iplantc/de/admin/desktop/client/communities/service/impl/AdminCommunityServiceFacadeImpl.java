package org.iplantc.de.admin.desktop.client.communities.service.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;

import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.converters.GroupListCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author aramsey
 */
public class AdminCommunityServiceFacadeImpl implements AdminCommunityServiceFacade {

    private final String ADMIN_COMMUNITIES = "org.iplantc.services.admin.communities";
    @Inject GroupAutoBeanFactory factory;
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

}
