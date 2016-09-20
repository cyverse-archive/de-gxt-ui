package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean;
import org.iplantc.de.client.models.HasSystemId;

/**
 * @author dennis
 */
public interface QualifiedAppId extends HasSystemId {

    String APP_ID_KEY = "app_id";

    @AutoBean.PropertyName(APP_ID_KEY)
    String getAppId();

    @AutoBean.PropertyName(APP_ID_KEY)
    void setAppId(String appId);
}
