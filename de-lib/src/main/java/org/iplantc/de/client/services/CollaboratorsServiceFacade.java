/**
 * 
 */
package org.iplantc.de.client.services;

import org.iplantc.de.client.models.collaborators.Collaborator;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.core.shared.FastMap;

import java.util.List;
/**
 * @author sriram
 *
 */
public interface CollaboratorsServiceFacade {

    /**
     * Get the list of collaborators a user has that match the specified search term
     * @param term
     * @param callback
     */
    public void searchCollaborators(String term, AsyncCallback<List<Collaborator>> callback) ;

    /**
     * Get the list of all collaborators a user has
     * @param callback
     */
    public void getCollaborators(AsyncCallback<List<Collaborator>> callback);

    /**
     * Add a collaborator to a user's list of collaborators
     * @param users
     * @param callback
     */
    public void addCollaborators(List<Collaborator> collaborators, AsyncCallback<Void> callback);

    /**
     * Remove a collaborator from a user's list of all collaborators
     * @param users
     * @param callback
     */
    public void removeCollaborators(List<Collaborator> users, AsyncCallback<Void> callback);

    /**
     *
     * @param usernames
     * @param callback
     */
    public void getUserInfo(List<String> usernames, AsyncCallback<FastMap<Collaborator>> callback);
}
