package org.iplantc.de.client.services;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupServiceFacade {

    /**
     * Get the list of Collaborator Lists a user has matching the specified search term
     * @param searchTerm
     * @param callback
     */
    void getGroups(AsyncCallback<List<Group>> callback);

    /**
     * Create a Collaborator List
     * @param group
     * @param callback
     */
    void addGroup(Group group, AsyncCallback<Group> callback);

    /**
     * Delete a Collaborator List
     * @param name
     * @param callback
     */
    void deleteGroup(String name, AsyncCallback<Group> callback);

    /**
     * Get the list of members who belong to a Collaborator List
     * @param group
     * @param callback
     */
    void getMembers(Group group, AsyncCallback<List<Subject>> callback);

    /**
     * Add a single member to a Collaborator List
     * @param group
     * @param callback
     */
    void addMember(Group group, Subject member, AsyncCallback<Void> callback);

    /**
     * Delete a single member from a Collaborator List
     * @param group
     * @param member
     * @param callback
     */
    void deleteMember(Group group, Subject member, AsyncCallback<Void> callback);

    /**
     * Replaces all members in the Collaborator List with the specified list instead
     * @param group
     * @param subjects
     */
    void updateMembers(Group group, List<Subject> subjects, AsyncCallback<List<UpdateMemberResult>> callback);

    /**
     * Update the details of a Collaborator List
     * @param group
     * @param callback
     */
    void updateGroup(Group group, AsyncCallback<Group> callback);

}
