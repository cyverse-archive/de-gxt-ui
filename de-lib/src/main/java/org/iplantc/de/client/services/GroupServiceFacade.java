package org.iplantc.de.client.services;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupServiceFacade {

    void getGroups(String searchTerm, AsyncCallback<List<Group>> callback);

    void addGroup(Group group, AsyncCallback<Group> callback);

    void deleteGroup(String name, AsyncCallback<Group> callback);

}
