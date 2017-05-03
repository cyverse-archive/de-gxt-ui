package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.apps.client.events.tools.AddNewToolSelected;
import org.iplantc.de.apps.client.events.tools.BeforeToolSearchEvent;

import org.iplantc.de.apps.client.events.tools.DeleteToolSelected;
import org.iplantc.de.apps.client.events.tools.RefreshToolsSelectedEvent;
import org.iplantc.de.apps.client.events.tools.ShareToolsSelected;
import org.iplantc.de.apps.client.events.tools.ToolSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.tools.ToolSelectionChangedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsView extends IsWidget,
                                         IsMaskable,
                                         HasHandlers,
                                         BeforeToolSearchEvent.BeforeToolSearchEventHandler,
                                         ToolSearchResultLoadEvent.ToolSearchResultLoadEventHandler,
                                         ToolSelectionChangedEvent.HasToolSelectionChangedEventHandlers {


    public interface ManageToolsViewAppearance {
        String name();

        String version();

        String imaName();

        String status();

        String mask();

        int nameWidth();

        int imgNameWidth();

        int tagWidth();

        String tag();
    }


    void loadTools(List<Tool> tools);

    void addTool(Tool tool);

    void removeTool(Tool tool);

    ManageToolsToolbarView getToolbar();

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter,
                                       RefreshToolsSelectedEvent.RefreshToolsSelectedEventHandler,
                                       AddNewToolSelected.NewToolSelectedHandler,
                                       DeleteToolSelected.DeleteToolsSelectedHandler,
                                       ShareToolsSelected.ShareToolsSelectedHandler,
                                       ToolSelectionChangedEvent.ToolSelectionChangedEventHandler {
        void loadTools();

        void addTool(Tool tool);
    }
}
