package org.iplantc.de.client.models.tool.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolUnsharingRequest {

    SharingSubject getSubject();

    void setTools(List<String> tools);

    void setSubject(SharingSubject subject);

    List<String> getTools();
}
