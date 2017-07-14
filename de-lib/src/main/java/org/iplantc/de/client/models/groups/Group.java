package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.collaborators.Subject;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Autobean currently used to represent a Group as defined in the iplant-groups POST /groups endpoint
 *
 * @author aramsey
 */
public interface Group extends Subject, HasDescription {

    String DEFAULT_GROUP = "default";
    
    String getType();
    void setType(String type);

    @PropertyName("display_name")
    String getDisplayName();

    String getExtension();

    default String getSubjectDisplayName() {
        String groupName = getName();
        if (!hasCollaboratorListLongName(groupName)) {
            return groupName;
        } else {
            return getCollaboratorListDisplayName(groupName);
        }
    }

}
