package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.user.client.ui.HasName;

/**
 * The autobean representation of a privilege a user can have on a Group
 * @author aramsey
 */
public interface Privilege extends HasName {
    String getType();
    void setType(String type);

    Boolean isAllowed();
    void setAllowed(Boolean allowed);

    Boolean isRevokable();
    void setRevokable(Boolean revokable);

    Subject getSubject();
    void setSubject(Subject subject);
}
