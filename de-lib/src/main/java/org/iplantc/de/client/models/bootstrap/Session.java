package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.Map;

/**
 * @author aramsey
 */
public interface Session {

    @PropertyName("login_time")
    String getLoginTime();

    @PropertyName("auth-redirect")
    Map<String, String> getAuthRedirects();

    String getError();
}
