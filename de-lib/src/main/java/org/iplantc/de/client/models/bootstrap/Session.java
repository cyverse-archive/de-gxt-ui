package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasSplittableError;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.Map;

/**
 * @author aramsey
 */
public interface Session extends HasSplittableError {

    @PropertyName("login_time")
    Long getLoginTime();

    @PropertyName("auth_redirect")
    Map<String, String> getAuthRedirects();

    @PropertyName("auth_redirect")
    void setAuthRedirects(Map<String, String> redirects);

    void setError(Splittable error);
}
