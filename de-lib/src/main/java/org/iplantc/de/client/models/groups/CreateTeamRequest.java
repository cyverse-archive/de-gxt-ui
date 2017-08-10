package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Autobean representation of what the groups service expects for requests to create a team
 */
public interface CreateTeamRequest extends HasName, HasDescription {

    @PropertyName("public_privileges")
    List<PrivilegeType> getPublicPrivileges();

    @PropertyName("public_privileges")
    void setPublicPrivileges(List<PrivilegeType> privileges);

}
