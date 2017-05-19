package org.iplantc.de.client.models.apps.sharing;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppUnSharingRequestList {

    @AutoBean.PropertyName("unsharing")
    List<AppSharingRequest> getAppUnSharingRequestList();

    @AutoBean.PropertyName("unsharing")
    void setAppUnSharingRequestList(List<AppSharingRequest> unsharinglist);

}
