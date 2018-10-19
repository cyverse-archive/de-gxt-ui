package org.iplantc.de.admin.desktop.client.communities.service;

import org.iplantc.de.client.models.groups.Group;

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
}
