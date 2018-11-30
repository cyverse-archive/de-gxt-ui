package org.iplantc.de.admin.desktop.client.communities.service;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author aramsey
 */
public interface AdminCommunityServiceFacade {

    /**
     * Fetch the list of communities
     * @param communityCallback
     */
    void getCommunities(AsyncCallback<List<Group>> communityCallback);

    /**
     * Fetch the list of apps that belong to a community
     * @param community
     * @param appListCallback
     */
    void getCommunityApps(Group community, DECallback<List<App>> appListCallback);

    /**
     * Update the details of a Community
     * @param originalCommunity
     * @param updatedCommunity
     * @param communityCallback
     * @param retagApps
     */
    void updateCommunity(String originalCommunity, Group updatedCommunity, boolean retagApps, AsyncCallback<Group> communityCallback);

    /**
     * Delete a community
     * @param community
     * @param communityCallback
     */
    void deleteCommunity(Group community, AsyncCallback<Group> communityCallback);

    /**
     * Fetch the list of community admins for a given community
     * @param community
     * @param adminListCallback
     */
    void getCommunityAdmins(Group community, AsyncCallback<List<Subject>> adminListCallback);

    /**
     * Add a list of collaborators as admins to a community
     * @param community
     * @param admins
     * @param adminListCallback
     */
    void addCommunityAdmins(Group community, List<Subject> admins, AsyncCallback<List<UpdateMemberResult>> adminListCallback);

    /**
     * Remove a list of collaborators as admins to a community
     * @param community
     * @param admins
     * @param adminListCallback
     */
    void deleteCommunityAdmins(Group community, List<Subject> admins, AsyncCallback<List<UpdateMemberResult>> adminListCallback);
}
