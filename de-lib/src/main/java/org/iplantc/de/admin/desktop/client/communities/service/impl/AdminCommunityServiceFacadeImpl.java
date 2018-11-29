package org.iplantc.de.admin.desktop.client.communities.service.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.models.apps.proxy.AppSearchAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberRequest;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.services.converters.GroupCallbackConverter;
import org.iplantc.de.client.services.converters.GroupListCallbackConverter;
import org.iplantc.de.client.services.converters.SubjectMemberListCallbackConverter;
import org.iplantc.de.client.services.converters.UpdateMemberResultsCallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class AdminCommunityServiceFacadeImpl implements AdminCommunityServiceFacade {

    private final String ADMIN_COMMUNITIES = "org.iplantc.services.admin.communities";
    private final String ADMIN_APPS_COMMUNITIES = "org.iplantc.services.admin.apps.communities";
    @Inject GroupAutoBeanFactory factory;
    @Inject AppSearchAutoBeanFactory appAutoBeanFactory;
    @Inject CollaboratorAutoBeanFactory collaboratorAutoBeanFactory;
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
        String address = ADMIN_APPS_COMMUNITIES + "/" + URL.encodePathSegment(community.getDisplayName()) + "/apps";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new DECallbackConverter<String, List<App>>(appListCallback) {
            @Override
            protected List<App> convertFrom(String object) {
                AppListLoadResult listResult = AutoBeanCodex.decode(appAutoBeanFactory, AppListLoadResult.class, object).as();
                return listResult.getData();
            }
        });
    }

    @Override
    public void updateCommunity(String originalCommunity,
                                Group updatedCommunity,
                                AsyncCallback<Group> communityCallback) {
        String address = ADMIN_COMMUNITIES + "/" + URL.encodePathSegment(originalCommunity);

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(updatedCommunity));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(communityCallback, factory));
    }

    @Override
    public void deleteCommunity(Group community, AsyncCallback<Group> communityCallback) {
        String address = ADMIN_COMMUNITIES + "/" + URL.encodePathSegment(community.getName());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(communityCallback, factory));
    }

    @Override
    public void getCommunityAdmins(Group community, AsyncCallback<List<Subject>> adminListCallback) {
        String address = ADMIN_COMMUNITIES + "/" + URL.encodePathSegment(community.getName()) + "/admins";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SubjectMemberListCallbackConverter(adminListCallback, collaboratorAutoBeanFactory));
    }

    @Override
    public void addCommunityAdmins(Group community,
                                   List<Subject> admins,
                                   AsyncCallback<List<UpdateMemberResult>> adminListCallback) {
        String address = ADMIN_COMMUNITIES + "/" + URL.encodePathSegment(community.getName()) + "/admins";

        UpdateMemberRequest request = getUpdateMemberRequest(admins);
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, adminListCallback));
    }

    @Override
    public void deleteCommunityAdmins(Group community,
                                      List<Subject> admins,
                                      AsyncCallback<List<UpdateMemberResult>> adminListCallback) {
        String address = ADMIN_COMMUNITIES + "/" + URL.encodePathSegment(community.getName()) + "/admins/deleter";

        UpdateMemberRequest request = getUpdateMemberRequest(admins);
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, adminListCallback));

    }

    UpdateMemberRequest getUpdateMemberRequest(List<Subject> subjects) {
        UpdateMemberRequest request = factory.getUpdateMemberRequest().as();
        if (subjects != null) {
            List<String> ids = subjects.stream().map(HasId::getId).collect(Collectors.toList());
            request.setMembers(ids);
        }
        return request;
    }
}
