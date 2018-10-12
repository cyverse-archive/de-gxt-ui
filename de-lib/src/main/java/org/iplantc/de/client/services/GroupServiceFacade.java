package org.iplantc.de.client.services;

import org.iplantc.de.client.models.HasMessage;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupServiceFacade {

    /**
     * Get the list of Collaborator Lists a user has matching the specified search term
     * @param subjectListCallback
     */
    void getLists(AsyncCallback<List<Subject>> subjectListCallback);

    /**
     * Get the list of all Teams for which the user has View permissions
     * @param teamListCallback
     */
    void getTeams(AsyncCallback<List<Group>> teamListCallback);

    /**
     * Get the the list of all Teams that a user belongs to
     * @param teamListCallback
     */
    void getMyTeams(AsyncCallback<List<Group>> teamListCallback);

    /**
     * Get the list of all Communities for which the user has View permissions
     * @param communitiesCallback
     */
    void getCommunities(AsyncCallback<List<Group>> communitiesCallback);

    /**
     * Create a Collaborator List
     * @param collabList
     * @param collabListCallback
     */
    void addList(Group collabList, AsyncCallback<Group> collabListCallback);

    /**
     * Create a Team
     * @param team
     * @param publicPrivileges
     * @param teamCallback
     */
    void addTeam(Group team, List<PrivilegeType> publicPrivileges, AsyncCallback<Group> teamCallback);

    /**
     * Delete a collaborator list
     * @param collabList
     * @param retainPermissions
     * @param collabCallback
     */
    void deleteList(Group collabList, boolean retainPermissions, AsyncCallback<Group> collabCallback);

    /**
     * Delete a Team
     * @param team
     * @param teamCallback
     */
    void deleteTeam(Group team, AsyncCallback<Group> teamCallback);

    /**
     * Get the list of members who belong to a Collaborator List
     * @param team
     * @param subjectListCallback
     */
    void getListMembers(Group team, AsyncCallback<List<Subject>> subjectListCallback);

    /**
     * Get the list of members who belong to a Team
     * @param team
     * @param subjectListCallback
     */
    void getTeamMembers(Group team, AsyncCallback<List<Subject>> subjectListCallback);

    /**
     * Delete members from a Collaborator List
     * @param team
     * @param members
     * @param retainPermissions
     * @param updatesCallback
     */
    void deleteListMembers(Group team, List<Subject> members, boolean retainPermissions, AsyncCallback<List<UpdateMemberResult>> updatesCallback);

    /**
     * Delete members from a Team
     * @param team
     * @param member
     * @param retainPermissions
     * @param updatesCallback
     */
    void deleteTeamMembers(Group team, List<Subject> member, boolean retainPermissions, AsyncCallback<List<UpdateMemberResult>> updatesCallback);

    /**
     * Adds  members to the Collaborator List
     * @param collabList
     * @param subjects
     */
    void addMembersToList(Group collabList, List<Subject> subjects, AsyncCallback<List<UpdateMemberResult>> updatesCallback);

    /**
     * Adds  members to the Team
     * @param team
     * @param subjects
     */
    void addMembersToTeam(Group team, List<Subject> subjects, AsyncCallback<List<UpdateMemberResult>> updatesCallback);

    /**
     * Update the details of a Collaborator List
     * @param originalList
     * @param updatedList
     * @param collabListCallback
     */
    void updateList(String originalList, Group updatedList, AsyncCallback<Group> collabListCallback);

    /**
     * Update the details of a Team
     * @param originalTeam
     * @param updatedTeam
     * @param teamCallback
     */
    void updateTeam(String originalTeam, Group updatedTeam, AsyncCallback<Group> teamCallback);

    /**
     * Update the privileges on a Team
     * @param team
     * @param updatePrivilegeRequests
     * @param privilegeListCallback
     */
    void updateTeamPrivileges(Group team, UpdatePrivilegeRequestList updatePrivilegeRequests, AsyncCallback<List<Privilege>> privilegeListCallback);

    /**
     * Get the list of privileges on a Team
     * @param team
     * @param privilegeListCallback
     */
    void getTeamPrivileges(Group team, AsyncCallback<List<Privilege>> privilegeListCallback);

    /**
     * With optin privileges, join the team
     * @param team
     * @param updatesCallback
     */
    void joinTeam(Group team, AsyncCallback<List<UpdateMemberResult>> updatesCallback);

    /**
     * Without optin privileges, request to join the team
     * @param team
     * @param requestMessage
     * @param voidCallback
     */
    void requestToJoinTeam(Group team, HasMessage requestMessage, AsyncCallback<Void> voidCallback);

    /**
     * Deny a user's request to join a team
     * @param team
     * @param denyMessage
     * @param requesterId
     * @param voidCallback
     */
    void denyRequestToJoinTeam(Group team, HasMessage denyMessage, String requesterId, AsyncCallback<Void> voidCallback);
    /**
     * Get the list of Teams that match the provided search term
     * @param searchTerm
     * @param teamListCallback
     */
    void searchTeams(String searchTerm, AsyncCallback<List<Group>> teamListCallback);

    /**
     * Leave the selected Team
     * @param team
     * @param updatesCallback
     */
    void leaveTeam(Group team, AsyncCallback<List<UpdateMemberResult>> updatesCallback);
}
