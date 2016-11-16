package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author aramsey
 */
public interface WorkspaceError {

    @PropertyName("error_code")
    String getErrorCode();

    //TODO more fields here?
}
