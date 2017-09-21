package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.collaborators.client.CollaborationView;

/**
 * Window configuration for the Collaboration window
 * @author aramsey
 */
public interface CollaborationWindowConfig extends WindowConfig {

    void setSelectedTab(CollaborationView.TAB tab);
    CollaborationView.TAB getSelectedTab();

}
