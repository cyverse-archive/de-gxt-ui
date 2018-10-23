package org.iplantc.de.admin.desktop.client.communities.service;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;
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
     */
    void updateCommunity(String originalCommunity, Group updatedCommunity, AsyncCallback<Group> communityCallback);
}
