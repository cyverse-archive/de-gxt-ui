package org.iplantc.de.client.models.tool.sharing;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolUnsharingRequest {

    void setUser(String user);

    void setTools(List<String> tools);

    String getUser();

    List<String> getTools();
}
