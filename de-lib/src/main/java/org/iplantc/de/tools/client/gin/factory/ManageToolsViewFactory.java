package org.iplantc.de.tools.client.gin.factory;

import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;

/**
 * @author aramsey
 */
public interface ManageToolsViewFactory {
    ManageToolsView create(ReactToolViews.ManageToolsProps baseProps);
}
