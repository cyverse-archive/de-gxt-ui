package org.iplantc.de.client.models.diskResources.sharing;

import java.util.List;

/**
 * The autobean representation of the request to unshare a file or folder
 * @author aramsey
 */
public interface DataUnsharingRequest {

    String getUser();
    void setUser(String user);

    List<String> getPaths();
    void setPaths(List<String> paths);
}
