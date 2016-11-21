package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasStatus;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author aramsey
 */
public interface DataInfo extends HasStatus {

    @PropertyName("user_home_path")
    String getHomePath();

    @PropertyName("user_trash_path")
    String getTrashPath();

    @PropertyName("base_trash_path")
    String getBaseTrashPath();

    String getError();
}
