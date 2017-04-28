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
    public void searchCollaborators(String term, AsyncCallback<List<Collaborator>> callback) ;
      
    public void getCollaborators(AsyncCallback<List<Collaborator>> callback);

    public void addCollaborators(List<Collaborator> collaborators, AsyncCallback<Void> callback);

    public void removeCollaborators(List<Collaborator> users, AsyncCallback<Void> callback);

    public void getUserInfo(List<String> usernames, AsyncCallback<FastMap<Collaborator>> callback);

}
