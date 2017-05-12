package org.iplantc.de.client.models.apps.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import java.util.List;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppUnsharingRequest {

    SharingSubject getSubject();

    void setSubject(SharingSubject subject);

    void setApps(List<AppPermission> apps);

    List<AppPermission> getApps();
}
