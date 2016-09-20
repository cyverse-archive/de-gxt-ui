package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * @author dennis
 */
public interface AppDeletionRequest {

    String APP_IDS_KEY = "app_ids";

    @AutoBean.PropertyName(APP_IDS_KEY)
    List<QualifiedAppId> getAppIds();

    @AutoBean.PropertyName(APP_IDS_KEY)
    void setAppIds(List<QualifiedAppId> appIds);
}
