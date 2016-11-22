package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author aramsey
 */
public interface UserProfile {

    String getUsername();

    @PropertyName("full_username")
    String getFullUsername();

    String getEmail();

    @PropertyName("first_name")
    String getFirstName();

    @PropertyName("last_name")
    String getLastName();

}
