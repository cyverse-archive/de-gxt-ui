/**
 * 
 */
package org.iplantc.de.client.services;

import org.iplantc.de.client.models.collaborators.Subject;

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
    public void searchCollaborators(String term, AsyncCallback<List<Subject>> callback) ;

    /**
     *
     * @param usernames
     * @param callback
     */
    public void getUserInfo(List<String> usernames, AsyncCallback<FastMap<Subject>> callback);
}
