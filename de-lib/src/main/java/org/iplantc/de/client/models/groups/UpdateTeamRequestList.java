package org.iplantc.de.client.models.groups;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of a list of type UpdateTeamRequest
 * @author aramsey
 */
public interface UpdateTeamRequestList {
    @PropertyName("updates")
    List<UpdateTeamRequest> getRequests();

    @PropertyName("updates")
    void setRequests(List<UpdateTeamRequest> requests);
}
