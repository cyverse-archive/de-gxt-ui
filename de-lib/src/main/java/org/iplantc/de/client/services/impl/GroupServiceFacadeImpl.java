package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.models.groups.UpdateMemberRequest;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdateMemberResultList;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.GroupCallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.client.services.converters.SubjectMemberListCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * @author aramsey
 */
public class GroupServiceFacadeImpl implements GroupServiceFacade {

    private final String LISTS = "org.iplantc.services.collaboratorLists";

    private GroupAutoBeanFactory factory;
    private CollaboratorAutoBeanFactory collabFactory;
    private DiscEnvApiService deService;

    @Inject
    public GroupServiceFacadeImpl(GroupAutoBeanFactory factory,
                                  CollaboratorAutoBeanFactory collabFactory,
                                  DiscEnvApiService deService) {

        this.factory = factory;
        this.collabFactory = collabFactory;
        this.deService = deService;
    }

    @Override
    public void getGroups(AsyncCallback<List<Group>> callback) {
        String address = LISTS;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<Group>>(callback) {
            @Override
            protected List<Group> convertFrom(String object) {
                AutoBean<GroupList> decode = AutoBeanCodex.decode(factory, GroupList.class, object);
                return decode.as().getGroups();
            }
        });
    }

    @Override
    public void addGroup(Group group, AsyncCallback<Group> callback) {
        String address = LISTS;

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void deleteGroup(Group group, AsyncCallback<Group> callback) {
        String address = LISTS + "/" + URL.encode(group.getName());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void getMembers(Group group, AsyncCallback<List<Subject>> callback) {
        String groupName = group.getName();
        String address = LISTS + "/" + URL.encode(groupName) + "/members";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SubjectMemberListCallbackConverter(callback, collabFactory));
    }

    @Override
    public void deleteMember(Group group, Subject member, AsyncCallback<Void> callback) {
        String groupName = group.getName();
        String subjectId = member.getId();

        String address = LISTS + "/" + URL.encode(groupName) + "/members/" + subjectId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void addMembers(Group group,
                           List<Subject> subjects,
                           AsyncCallback<List<UpdateMemberResult>> callback) {
        String groupName = group.getName();
        UpdateMemberRequest request = factory.getUpdateMemberRequest().as();
        List<String> ids = subjects.stream()
                                   .map(collaborator -> collaborator.getId())
                                   .collect(Collectors.toList());
        request.setMembers(ids);

        String address = LISTS + "/" + URL.encode(groupName) + "/members";

        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<UpdateMemberResult>>(callback) {
            @Override
            protected List<UpdateMemberResult> convertFrom(String object) {
                AutoBean<UpdateMemberResultList> listAutoBean = AutoBeanCodex.decode(factory, UpdateMemberResultList.class, object);
                return listAutoBean.as().getResults();
            }
        });
    }

    @Override
    public void updateGroup(String originalGroup, Group group, AsyncCallback<Group> callback) {
        String address = LISTS + "/" + URL.encode(originalGroup);

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

}
