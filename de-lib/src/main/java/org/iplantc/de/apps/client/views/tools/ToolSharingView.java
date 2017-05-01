/**
 * 
 * @author sriram
 */

package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public interface ToolSharingView extends IsWidget {

    void addShareWidget(Widget widget);

    void setSelectedTools(List<Tool> models);
}
