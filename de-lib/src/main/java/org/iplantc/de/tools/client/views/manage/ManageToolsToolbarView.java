package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.tools.client.events.AddNewToolSelected;
import org.iplantc.de.tools.client.events.BeforeToolSearchEvent;
import org.iplantc.de.tools.client.events.DeleteToolSelected;
import org.iplantc.de.tools.client.events.EditToolSelected;
import org.iplantc.de.tools.client.events.RefreshToolsSelectedEvent;
import org.iplantc.de.tools.client.events.RequestToolSelected;
import org.iplantc.de.tools.client.events.ShareToolsSelected;
import org.iplantc.de.tools.client.events.ToolFilterChanged;
import org.iplantc.de.tools.client.events.ToolSearchResultLoadEvent;

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
                                                ShareToolsSelected.HasShareToolselectedHandlers,
                                                ToolFilterChanged.HasToolFilterChangedHandlers,
                                                RequestToolSelected.HasRequestToolSelectedHandlers,
                                                EditToolSelected.HasEditToolSelectedHandlers {

    void setSelection(List<Tool> currentSelection);

    void clearSearch();

    void resetFilter();

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
