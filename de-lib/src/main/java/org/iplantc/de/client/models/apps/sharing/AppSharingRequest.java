package org.iplantc.de.client.models.apps.sharing;

import org.iplantc.de.client.models.sharing.SharingSubject;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppSharingRequest {

    SharingSubject getSubject();

    void setSubject(SharingSubject subject);

    @AutoBean.PropertyName("apps")
    List<AppPermission> getAppPermissions();

    @AutoBean.PropertyName("apps")
    void setAppPermissions(List<AppPermission> appPerms);
}
