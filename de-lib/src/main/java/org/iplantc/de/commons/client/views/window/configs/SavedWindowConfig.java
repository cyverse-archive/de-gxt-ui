package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.client.models.WindowType;

import com.google.web.bindery.autobean.shared.Splittable;

/**
 * An autobean to serialize DE winow configurations.
 *
 * Created by sriram on 1/17/18.
 *
 */
public interface SavedWindowConfig {
    String getTag();

    WindowType getWindowType();

    void setWindowType(WindowType type);

    Splittable getWindowConfig();

    void setWindowConfig(Splittable config);

}

