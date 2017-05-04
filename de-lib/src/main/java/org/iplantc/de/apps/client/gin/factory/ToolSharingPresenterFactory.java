package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.views.tools.ToolSharingPresenter;
import org.iplantc.de.client.models.tool.Tool;

import java.util.List;

/**
 * Created by sriram on 5/3/17.
 */
public interface ToolSharingPresenterFactory {
    ToolSharingPresenter create(List<Tool> apps);
}
