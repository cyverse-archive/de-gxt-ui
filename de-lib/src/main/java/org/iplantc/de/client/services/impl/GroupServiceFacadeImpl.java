package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.CreateTeamRequest;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberRequest;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdateMemberResultList;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.GroupCallbackConverter;
import org.iplantc.de.client.services.converters.GroupListToSubjectListCallbackConverter;
import org.iplantc.de.client.services.converters.PrivilegeListCallbackConverter;
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
    private final String TEAMS = "org.iplantc.services.teams";

    private GroupAutoBeanFactory factory;
    private CollaboratorAutoBeanFactory collabFactory;
    private DiscEnvApiService deService;
    @Inject UserInfo userInfo;

    @Inject
    public GroupServiceFacadeImpl(GroupAutoBeanFactory factory,
                                  CollaboratorAutoBeanFactory collabFactory,
                                  DiscEnvApiService deService) {

        this.factory = factory;
        this.collabFactory = collabFactory;
        this.deService = deService;
    }

    @Override
    public void getLists(AsyncCallback<List<Subject>> callback) {
        String address = LISTS;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new GroupListToSubjectListCallbackConverter(callback, factory));
    }

    @Override
    public void getTeams(AsyncCallback<List<Group>> callback) {
        String address = TEAMS;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<Group>>(callback) {
            @Override
            protected List<Group> convertFrom(String object) {
                GroupList groupList = AutoBeanCodex.decode(factory, GroupList.class, object).as();
                return groupList.getGroups();
            }
        });
    }

    @Override
    public void getMyTeams(AsyncCallback<List<Group>> callback) {
        String address = TEAMS + "?member=" + userInfo.getUsername();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<Group>>(callback) {
            @Override
            protected List<Group> convertFrom(String object) {
                GroupList groupList = AutoBeanCodex.decode(factory, GroupList.class, object).as();
                return groupList.getGroups();
            }
        });
    }

    @Override
    public void addList(Group group, AsyncCallback<Group> callback) {
        String address = LISTS;

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void addTeam(Group group, List<PrivilegeType> publicPrivileges, AsyncCallback<Group> callback) {
        String address = TEAMS;

        CreateTeamRequest request = factory.getCreateTeamRequest().as();
        request.setName(group.getName());
        request.setDescription(group.getDescription());
        request.setPublicPrivileges(publicPrivileges);

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void deleteList(Group group, boolean retainPermissions, AsyncCallback<Group> callback) {
        String address = LISTS + "/" + URL.encodePathSegment(group.getName()) + "?retain-permissions=" + retainPermissions;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void deleteTeam(Group group, AsyncCallback<Group> callback) {
        String address = TEAMS + "/" + URL.encodePathSegment(group.getName());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void getListMembers(Group group, AsyncCallback<List<Subject>> callback) {
        getMembers(group, LISTS, callback);
    }

    @Override
    public void getTeamMembers(Group group, AsyncCallback<List<Subject>> callback) {
        getMembers(group, TEAMS, callback);
    }

    void getMembers(Group group, String address, AsyncCallback<List<Subject>> callback) {
        String groupName = group.getName();
        address += "/" + URL.encodePathSegment(groupName) + "/members";


        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SubjectMemberListCallbackConverter(callback, collabFactory));
    }

    @Override
    public void deleteListMembers(Group group, List<Subject> subjects, boolean retainPermissions, AsyncCallback<List<UpdateMemberResult>> callback) {
        deleteMembers(LISTS, group, subjects, retainPermissions, callback);
    }

    @Override
    public void deleteTeamMembers(Group group,
                                  List<Subject> member,
                                  boolean retainPermissions,
                                  AsyncCallback<List<UpdateMemberResult>> callback) {
        deleteMembers(TEAMS, group, member, retainPermissions, callback);
    }

    void deleteMembers(String address, Group group, List<Subject> subjects, boolean retainPermissions, AsyncCallback<List<UpdateMemberResult>> callback) {
        String groupName = group.getName();
        UpdateMemberRequest request = factory.getUpdateMemberRequest().as();
        List<String> ids = subjects.stream()
                                   .map(collaborator -> collaborator.getId())
                                   .collect(Collectors.toList());
        request.setMembers(ids);

        address += "/" + URL.encodePathSegment(groupName) + "/members/deleter?retain-permissions=" + retainPermissions;

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
    public void addMembersToList(Group group,
                                 List<Subject> subjects,
                                 AsyncCallback<List<UpdateMemberResult>> callback) {
        addMembersToGroup(LISTS, group, subjects, callback);
    }

    @Override
    public void addMembersToTeam(Group group,
                                 List<Subject> subjects,
                                 AsyncCallback<List<UpdateMemberResult>> callback) {
        addMembersToGroup(TEAMS, group, subjects, callback);

    }

    void addMembersToGroup(String address,
                           Group group,
                           List<Subject> subjects,
                           AsyncCallback<List<UpdateMemberResult>> callback) {
        String groupName = group.getName();
        UpdateMemberRequest request = factory.getUpdateMemberRequest().as();
        List<String> ids = subjects.stream()
                                   .map(collaborator -> collaborator.getId())
                                   .collect(Collectors.toList());
        request.setMembers(ids);

        address += "/" + URL.encodePathSegment(groupName) + "/members";

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
    public void updateList(String originalGroup, Group group, AsyncCallback<Group> callback) {
        String address = LISTS;
        updateGroup(address, originalGroup, group, callback);
    }

    @Override
    public void updateTeam(String originalGroup, Group group, AsyncCallback<Group> callback) {
        String address = TEAMS;
        updateGroup(address, originalGroup, group, callback);
    }

    void updateGroup(String address, String originalGroup, Group group, AsyncCallback<Group> callback) {
        address += "/" + URL.encodePathSegment(originalGroup);

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void updateTeamPrivileges(Group group,
                                     UpdatePrivilegeRequestList request,
                                     AsyncCallback<List<Privilege>> callback) {
        String groupName = group.getName();
        String address = TEAMS + "/" + URL.encodePathSegment(groupName) + "/privileges";

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new PrivilegeListCallbackConverter(callback, factory));
    }

    @Override
    public void getTeamPrivileges(Group group, AsyncCallback<List<Privilege>> callback) {
        String groupName = group.getName();
        String address = TEAMS + "/" + URL.encodePathSegment(groupName) + "/privileges";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new PrivilegeListCallbackConverter(callback, factory));
    }

    @Override
    public void searchTeams(String searchTerm, AsyncCallback<List<Group>> callback) {
        String address = TEAMS + "?search=" + URL.encodeQueryString(searchTerm);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<Group>>(callback) {
            @Override
            protected List<Group> convertFrom(String object) {
                GroupList groupList = AutoBeanCodex.decode(factory, GroupList.class, object).as();
                return groupList.getGroups();
            }
        });
    }

    @Override
    public void leaveTeam(Group group, AsyncCallback<List<UpdateMemberResult>> callback) {
        String groupName = group.getName();

        String address = TEAMS + "/" + URL.encodePathSegment(groupName) + "/leave";
        
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<UpdateMemberResult>>(callback) {
            @Override
            protected List<UpdateMemberResult> convertFrom(String object) {
                AutoBean<UpdateMemberResultList> listAutoBean = AutoBeanCodex.decode(factory, UpdateMemberResultList.class, object);
                return listAutoBean.as().getResults();
            }
        });
    }

}
