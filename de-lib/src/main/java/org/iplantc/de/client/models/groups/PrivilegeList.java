package org.iplantc.de.client.models.groups;

import java.util.List;

/**
 * The autobean representation of a list of type Privilege
 * @author aramsey
 */
public interface PrivilegeList {
    List<Privilege> getPrivileges();
    void setPrivileges(List<Privilege> privileges);
}
