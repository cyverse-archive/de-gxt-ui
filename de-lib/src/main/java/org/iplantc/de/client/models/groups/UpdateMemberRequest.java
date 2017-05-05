package org.iplantc.de.client.models.groups;

import java.util.List;

/**
 * The AutoBean representation of the request sent to update a list of members in a Group
 * @author aramsey
 */
public interface UpdateMemberRequest {
    List<String> getMembers();

    void setMembers(List<String> ids);
}
