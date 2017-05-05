package org.iplantc.de.client.models.groups;

import java.util.List;

/**
 * @author aramsey
 */
public interface UpdateMemberRequest {
    List<String> getMembers();

    void setMembers(List<String> ids);
}
