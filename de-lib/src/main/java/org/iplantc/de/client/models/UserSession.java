package org.iplantc.de.client.models;

import org.iplantc.de.commons.client.views.window.configs.WindowConfig;

import java.util.List;

public interface UserSession {
    List<WindowConfig>  getWindowConfigs();

    void setWindowConfigs(List<WindowConfig> configs);

}
