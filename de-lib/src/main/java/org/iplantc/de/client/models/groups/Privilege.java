package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.collaborators.Subject;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * The autobean representation of a privilege a user can have on a Group
 * @author aramsey
 */
public interface Privilege {

    @PropertyName("name")
    PrivilegeType getPrivilegeType();

    @PropertyName("name")
    void setPrivilegeType(PrivilegeType type);

    String getType();
    void setType(String type);

    Boolean isAllowed();
    void setAllowed(Boolean allowed);

    Boolean isRevokable();
    void setRevokable(Boolean revokable);

    Subject getSubject();
    void setSubject(Subject subject);
}
