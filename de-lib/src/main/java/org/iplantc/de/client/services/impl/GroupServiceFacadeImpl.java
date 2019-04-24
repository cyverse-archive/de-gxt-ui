package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasMessage;
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
import org.iplantc.de.client.services.converters.GroupListCallbackConverter;
import org.iplantc.de.client.services.converters.GroupListToSubjectListCallbackConverter;
import org.iplantc.de.client.services.converters.PrivilegeListCallbackConverter;
import org.iplantc.de.client.services.converters.SplittableCallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.client.services.converters.SubjectMemberListCallbackConverter;
import org.iplantc.de.client.services.converters.UpdateMemberResultsCallbackConverter;
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
    private final String COMMUNITIES = "org.iplantc.services.communities";

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
        deService.getServiceData(wrapper, new GroupListCallbackConverter(factory, callback));
    }

    @Override
    public void getMyTeams(AsyncCallback<List<Group>> callback) {
        String address = TEAMS + "?member=" + userInfo.getUsername();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new GroupListCallbackConverter(factory, callback));
    }

    @Override
    public void getCommunities(AsyncCallback<Splittable> communitiesCallback) {
        String address = COMMUNITIES;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SplittableCallbackConverter(communitiesCallback));
    }

    @Override
    public void getMyCommunities(AsyncCallback<Splittable> communitiesCallback) {
        String address = COMMUNITIES + "?member=" + userInfo.getUsername();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SplittableCallbackConverter(communitiesCallback));
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
        addGroup(group, publicPrivileges, address, callback);
    }

    @Override
    public void addCommunity(Group community,
                             AsyncCallback<Group> communityCallback) {
        String address = COMMUNITIES;
        addGroup(community, null, address, communityCallback);
    }

    void addGroup(Group group, List<PrivilegeType> publicPrivileges, String address, AsyncCallback<Group> callback) {
        CreateTeamRequest request = factory.getCreateTeamRequest().as();
        request.setName(group.getName());
        request.setDescription(group.getDescription());
        if (publicPrivileges != null) {
            request.setPublicPrivileges(publicPrivileges);
        }

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
        deleteGroup(TEAMS, group.getName(), callback);
    }

    @Override
    public void deleteCommunity(String communityName, AsyncCallback<Group> communityCallback) {
        deleteGroup(COMMUNITIES, communityName, communityCallback);
    }

    private void deleteGroup(String address, String groupName, AsyncCallback<Group> callback) {
        address += "/" + URL.encodePathSegment(groupName);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void getListMembers(Group group, AsyncCallback<List<Subject>> callback) {
        getMembers(group.getName(), LISTS, callback);
    }

    @Override
    public void getTeamMembers(Group group, AsyncCallback<List<Subject>> callback) {
        getMembers(group.getName(), TEAMS, callback);
    }

    @Override
    public void getCommunityMembers(String communityName, AsyncCallback<List<Subject>> subjectListCallback) {
        getMembers(communityName, COMMUNITIES, subjectListCallback);
    }

    void getMembers(String groupName, String address, AsyncCallback<List<Subject>> callback) {
        address += "/" + URL.encodePathSegment(groupName) + "/members";


        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SubjectMemberListCallbackConverter(callback, collabFactory));
    }

    @Override
    public void getCommunityAdmins(String communityName, AsyncCallback<Splittable> adminListCallback) {
        String address = COMMUNITIES + "/" + URL.encodePathSegment(communityName) + "/admins";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new SplittableCallbackConverter(adminListCallback));
    }

    @Override
    public void removeCommunityAdmins(String communityName,
                                      List<String> admins,
                                      AsyncCallback<List<UpdateMemberResult>> updateMemberCallback) {
        String address = COMMUNITIES + "/" + URL.encodePathSegment(communityName) + "/admins/deleter";

        UpdateMemberRequest request = getUpdateMemberRequest(admins);
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, updateMemberCallback));
    }

    @Override
    public void addCommunityAdmins(String communityName,
                                   List<String> adminIds,
                                   AsyncCallback<List<UpdateMemberResult>> updateMemberCallback) {
        String address = COMMUNITIES + "/" + URL.encodePathSegment(communityName) + "/admins";

        UpdateMemberRequest request = getUpdateMemberRequest(adminIds);
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, updateMemberCallback));
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
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, callback));
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
        deService.getServiceData(wrapper, new UpdateMemberResultsCallbackConverter(factory, callback));
    }

    @Override
    public void updateList(String originalGroup, Group group, AsyncCallback<Group> callback) {
        String address = LISTS + "/" + URL.encodePathSegment(originalGroup);
        updateGroup(address, group, callback);
    }

    @Override
    public void updateTeam(String originalGroup, Group group, AsyncCallback<Group> callback) {
        String address = TEAMS + "/" + URL.encodePathSegment(originalGroup);
        updateGroup(address, group, callback);
    }

    @Override
    public void updateCommunity(String originalCommunity,
                                Group updatedCommunity,
                                boolean retagApps,
                                AsyncCallback<Group> callback) {
        String address = COMMUNITIES + "/" + URL.encodePathSegment(originalCommunity) + "?retag-apps=" + retagApps;
        updateGroup(address, updatedCommunity, callback);
    }

    void updateGroup(String address, Group group, AsyncCallback<Group> callback) {
        Group updatedGroup = factory.getGroup().as();
        updatedGroup.setName(group.getName());
        updatedGroup.setDescription(group.getDescription());

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(updatedGroup));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, encode.getPayload());
        deService.getServiceData(wrapper, new GroupCallbackConverter(callback, factory));
    }

    @Override
    public void updateTeamPrivileges(Group group,
                                     UpdatePrivilegeRequestList request,
                                     AsyncCallback<List<Privilege>> callback) {
        updateGroupPrivileges(TEAMS, group, request, callback);
    }

    private void updateGroupPrivileges(String address,
                                       Group group,
                                       UpdatePrivilegeRequestList request,
                                       AsyncCallback<List<Privilege>> callback) {
        String groupName = group.getName();
        address +=  "/" + URL.encodePathSegment(groupName) + "/privileges";

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new PrivilegeListCallbackConverter(callback, factory));
    }

    @Override
    public void getTeamPrivileges(Group group, AsyncCallback<List<Privilege>> callback) {
        getGroupPrivileges(TEAMS, group, callback);
    }

    @Override
    public void getCommunityPrivileges(Group community, AsyncCallback<List<Privilege>> callback) {
        getGroupPrivileges(COMMUNITIES, community, callback);
    }

    private void getGroupPrivileges(String address, Group group, AsyncCallback<List<Privilege>> callback) {
        String groupName = group.getName();
        address += "/" + URL.encodePathSegment(groupName) + "/privileges";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new PrivilegeListCallbackConverter(callback, factory));
    }

    @Override
    public void joinTeam(Group team, AsyncCallback<List<UpdateMemberResult>> updatesCallback) {
        joinGroup(TEAMS, team.getName(), updatesCallback);
    }

    @Override
    public void joinCommunity(String communityName, AsyncCallback<List<UpdateMemberResult>> updatesCallback) {
        joinGroup(COMMUNITIES, communityName, updatesCallback);
    }

    private void joinGroup(String address, String groupName, AsyncCallback<List<UpdateMemberResult>> updatesCallback) {
        address += "/" + URL.encodePathSegment(groupName) + "/join";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<UpdateMemberResult>>(updatesCallback) {
            @Override
            protected List<UpdateMemberResult> convertFrom(String object) {
                AutoBean<UpdateMemberResultList> listAutoBean = AutoBeanCodex.decode(factory, UpdateMemberResultList.class, object);
                return listAutoBean.as().getResults();
            }
        });

    }

    @Override
    public void requestToJoinTeam(Group team, HasMessage requestMessage, AsyncCallback<Void> voidCallback) {
        String teamName = team.getName();
        String address = TEAMS + "/" + URL.encodePathSegment(teamName) + "/join-request";

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(requestMessage));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(voidCallback));
    }

    @Override
    public void denyRequestToJoinTeam(Group team,
                                      HasMessage denyMessage,
                                      String requesterId,
                                      AsyncCallback<Void> voidCallback) {
        String teamName = team.getName();
        String address = TEAMS + "/" + URL.encodePathSegment(teamName) + "/join-request/" + requesterId + "/deny";

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(denyMessage));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(voidCallback));
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
        leaveGroup(TEAMS, group.getName(), callback);
    }

    @Override
    public void leaveCommunity(String communityName, AsyncCallback<List<UpdateMemberResult>> updatesCallback) {
        leaveGroup(COMMUNITIES, communityName, updatesCallback);
    }

    private void leaveGroup(String address, String groupName, AsyncCallback<List<UpdateMemberResult>> callback) {
        address += "/" + URL.encodePathSegment(groupName) + "/leave";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<UpdateMemberResult>>(callback) {
            @Override
            protected List<UpdateMemberResult> convertFrom(String object) {
                AutoBean<UpdateMemberResultList> listAutoBean = AutoBeanCodex.decode(factory, UpdateMemberResultList.class, object);
                return listAutoBean.as().getResults();
            }
        });
    }

    UpdateMemberRequest getUpdateMemberRequest(List<String> subjectIds) {
        UpdateMemberRequest request = factory.getUpdateMemberRequest().as();
        if (subjectIds != null) {
            request.setMembers(subjectIds);
        }
        return request;
    }
}
