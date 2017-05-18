package org.iplantc.de.client.models.diskResources.sharing;

import static com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean representation of the JSON object expected when sharing
 * a file or folder
 *
 * @author aramsey
 */
public interface DataSharingRequest {

    String getUser();

    void setUser(String user);

    @PropertyName("paths")
    List<DataPermission> getDataPermissions();

    @PropertyName("paths")
    void setDataPermissions(List<DataPermission> dataPermissions);
}
