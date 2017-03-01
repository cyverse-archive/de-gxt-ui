package org.iplantc.de.client.models.bootstrap;

import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Created by sriram on 2/27/17.
 */
public interface AppsInfo {

    SystemIds getSystemsIds();

    Workspace getWorkspace();

    Splittable getError();
}
