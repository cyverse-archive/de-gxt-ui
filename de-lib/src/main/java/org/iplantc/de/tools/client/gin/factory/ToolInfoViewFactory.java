package org.iplantc.de.tools.client.gin.factory;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.tools.client.views.manage.ToolInfoViewImpl;

/**
 * Created by sriram on 5/30/17.
 */
public interface ToolInfoViewFactory {
    ToolInfoViewImpl build(Tool tool);
}
