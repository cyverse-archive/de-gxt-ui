package org.iplantc.de.tools.client.gin.factory;

import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.views.manage.EditToolView;

/**
 * @author aramsey
 */
public interface EditToolViewFactory {
    EditToolView create(ReactToolViews.BaseEditToolProps baseProps);
}
