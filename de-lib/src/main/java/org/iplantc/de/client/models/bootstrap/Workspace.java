package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasStatus;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author aramsey
 */
public interface Workspace extends HasStatus {

    @PropertyName("new_workspace")
    Boolean isNewUser();

    @PropertyName("workspace_id")
    String getWorkspaceId();

    Splittable getError();
}
