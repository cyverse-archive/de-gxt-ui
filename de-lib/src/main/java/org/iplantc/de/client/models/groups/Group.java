package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.collaborators.Subject;

/**
 * Autobean currently used to represent a Group as defined in the iplant-groups POST /groups endpoint
 *
 * @author aramsey
 */
public interface Group extends Subject, HasDescription {

    String DEFAULT_GROUP = "default";
    
    String getType();
    void setType(String type);

    String getExtension();

    /**
     * This is an artificial key in that it will never be populated unless specifically done
     * in the UI code, i.e. the service will never return this key or a value for the key
     * @return
     */
    String getCreator();
    void setCreator(String creator);
}
