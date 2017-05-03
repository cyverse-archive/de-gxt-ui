package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.apps.client.events.tools.AddNewToolSelected;
import org.iplantc.de.apps.client.events.tools.BeforeToolSearchEvent;
import org.iplantc.de.apps.client.events.tools.DeleteToolSelected;
import org.iplantc.de.apps.client.events.tools.RefreshToolsSelectedEvent;
import org.iplantc.de.apps.client.events.tools.ShareToolsSelected;
import org.iplantc.de.apps.client.events.tools.ToolSearchResultLoadEvent;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsToolbarView extends IsWidget,
                                                HasHandlers,
                                                BeforeToolSearchEvent.HasBeforeToolSearchEventHandlers,
                                                ToolSearchResultLoadEvent.HasToolSearchResultLoadEventHandlers,
                                                RefreshToolsSelectedEvent.HasRefreshToolsSelectedEventHandlers,
                                                AddNewToolSelected.HasNewToolSelectedHandlers,
                                                DeleteToolSelected.HasDeleteToolsSelectedHandlers,
                                                ShareToolsSelected.HasShareToolselectedHandlers{

    void setSelection(List<Tool> currentSelection);

    interface ManageToolsToolbarAppearance {

        String tools();

        String requestTool();

        String edit();

        String delete();

        String useInApp();

        String share();

        String shareCollab();

        String sharePublic();

        String submitForPublicUse();

        String refresh();

        ImageResource refreshIcon();

        ImageResource shareToolIcon();

        ImageResource submitForPublicIcon();

        String searchTools();

        String addTool();

        ImageResource addIcon();

        ImageResource requestToolIcon();

        ImageResource editIcon();

        ImageResource deleteIcon();

        ImageResource useInAppIcon();
    }

}
