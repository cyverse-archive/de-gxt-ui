package org.iplantc.de.client.models.tool.sharing;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolPermissionsRequest {

    List<String> getTools();

    void setTools(List<String> toolPerms);
}
