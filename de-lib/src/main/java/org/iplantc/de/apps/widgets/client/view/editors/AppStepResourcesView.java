package org.iplantc.de.apps.widgets.client.view.editors;

import org.iplantc.de.client.models.apps.integration.AppTemplateStepRequirements;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppStepResourcesView extends IsWidget {
    AppTemplateStepRequirements getRequirements();

    void setHeading(String text);
}
