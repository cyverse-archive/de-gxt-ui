package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;


/**
 * AutoBean interface for the bootstrap endpoint response.
 * 
 * @author psarando
 * 
 */
public interface UserBootstrap {

    @PropertyName("user_info")
    UserProfile getUserProfile();

    Session getSession();

    Workspace getWorkspace();

    @PropertyName("data_info")
    DataInfo getDataInfo();

    Preferences getPreferences();
}
