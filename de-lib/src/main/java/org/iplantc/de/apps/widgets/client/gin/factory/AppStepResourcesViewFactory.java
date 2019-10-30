package org.iplantc.de.apps.widgets.client.gin.factory;

import org.iplantc.de.apps.widgets.client.view.editors.AppStepResourcesView;
import org.iplantc.de.client.models.apps.integration.AppTemplateStepLimits;

public interface AppStepResourcesViewFactory {
    AppStepResourcesView create(AppTemplateStepLimits limits);
}
