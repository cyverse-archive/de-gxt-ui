package org.iplantc.de.client.services;


import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * 
 * Marker interface
 * 
 * @author sriram
 * 
 */
public interface AppMetadataServiceFacade extends MetadataServiceFacade {
    void updateAppCommunityTags(Group community, App app, AsyncCallback<Splittable> callback);

    void deleteAppCommunityTags(Group community, App app, AsyncCallback<Splittable> callback);
}
