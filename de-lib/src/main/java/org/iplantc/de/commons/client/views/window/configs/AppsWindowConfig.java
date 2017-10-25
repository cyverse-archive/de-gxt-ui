package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.client.models.HasQualifiedId;

public interface AppsWindowConfig extends WindowConfig {

    HasQualifiedId getSelectedAppCategory();

    HasQualifiedId getSelectedApp();

    void setSelectedAppCategory(HasQualifiedId appGroup);

    void setSelectedApp(HasQualifiedId app);

    void setView(String lastViewSelected);

    String getView();
}
