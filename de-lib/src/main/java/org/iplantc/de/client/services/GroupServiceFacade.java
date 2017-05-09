package org.iplantc.de.client.services;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;

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
    void getGroups(String searchTerm, AsyncCallback<List<Group>> callback);

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
    void getMembers(Group group, AsyncCallback<List<Collaborator>> callback);

}
