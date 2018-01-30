package org.iplantc.de.client.models;

import org.iplantc.de.commons.client.views.window.configs.SavedWindowConfig;

import java.util.List;

public interface UserSession {
    List<SavedWindowConfig>  getWindowConfigs();

    void setWindowConfigs(List<SavedWindowConfig> configs);

}
