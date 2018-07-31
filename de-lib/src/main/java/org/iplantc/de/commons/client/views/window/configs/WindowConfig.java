package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.client.models.WindowType;

public interface WindowConfig {
    
    String getTag();
    
    WindowType getWindowType();

    String getWindowTitle();

    void setWindowTitle(String title);

    boolean isMinimized();

    void setMinimized(boolean minimized);

}
