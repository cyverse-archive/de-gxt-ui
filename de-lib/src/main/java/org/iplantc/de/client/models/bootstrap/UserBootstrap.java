package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;


/**
 * AutoBean interface for the bootstrap endpoint response.
 * 
 * @author psarando
 * 
 */
public interface UserBootstrap {

    String USER_INFO_KEY = "user_info";
    String SESSION_KEY = "session";
    String WORKSPACE_KEY = "workspace";
    String DATA_INFO_KEY = "data_info";
    String PREFERENCES_KEY = "preferences";
    String APPS_INFO_KEY = "apps_info";

    @PropertyName(USER_INFO_KEY)
    UserProfile getUserProfile();

    @PropertyName(SESSION_KEY)
    Session getSession();

    @PropertyName(WORKSPACE_KEY)
    Workspace getWorkspace();

    @PropertyName(DATA_INFO_KEY)
    DataInfo getDataInfo();

    @PropertyName(PREFERENCES_KEY)
    Preferences getPreferences();

    @PropertyName(APPS_INFO_KEY)
    AppsInfo getAppsInfo();

}
