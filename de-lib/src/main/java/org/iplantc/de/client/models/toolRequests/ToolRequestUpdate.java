package org.iplantc.de.client.models.toolRequests;

/**
 * @author jstroot
 */
public interface ToolRequestUpdate {

    String getStatus();

    void setStatus(String status);

    String getComments();

    void setComments(String comments);

}
