package org.iplantc.de.client.models.tool.sharing;

import org.iplantc.de.client.models.HasId;

/**
 * Created by sriram.
 */
public interface ToolPermission extends HasId {

    void setPermission(String permission);

    String getPermission();

    void setId(String id);
}
