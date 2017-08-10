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
}
