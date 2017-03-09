package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasSplittableError;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Created by sriram on 2/27/17.
 */
public interface AppsInfo extends HasSplittableError {

    @AutoBean.PropertyName("system_ids")
    SystemIds getSystemsIds();

    Workspace getWorkspace();
}
