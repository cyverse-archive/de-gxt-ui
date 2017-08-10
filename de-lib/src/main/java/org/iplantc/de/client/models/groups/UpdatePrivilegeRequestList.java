package org.iplantc.de.client.models.groups;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of type UpdatePrivilegeRequest
 * @author aramsey
 */
public interface UpdatePrivilegeRequestList {
    @PropertyName("updates")
    List<UpdatePrivilegeRequest> getRequests();

    @PropertyName("updates")
    void setRequests(List<UpdatePrivilegeRequest> requests);
}
