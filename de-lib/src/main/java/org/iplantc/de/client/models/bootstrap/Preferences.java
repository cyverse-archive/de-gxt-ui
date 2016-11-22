package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasStatus;
import org.iplantc.de.client.models.diskResources.Folder;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 *
 * @author aramsey
 */
public interface Preferences extends HasStatus {

    @PropertyName("system_default_output_dir")
    Folder getSystemDefaultOutputDir();

    @PropertyName("default_output_folder")
    Folder getDefaultOutputDir();

    Splittable getError();

}
