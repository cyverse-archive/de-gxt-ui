package org.iplantc.de.client.models.groups;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of the request body to update privileges on a team
 * @author aramsey
 */
public interface UpdateTeamRequest {

    @PropertyName("subject_id")
    String getSubjectId();

    @PropertyName("subject_id")
    void setSubjectId(String id);

    List<String> getPrivileges();
    void setPrivileges(List<String> privileges);
}
