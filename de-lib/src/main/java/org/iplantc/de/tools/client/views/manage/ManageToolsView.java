
package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.tools.client.events.ShowToolInfoEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.tools.client.events.AddNewToolSelected;
import org.iplantc.de.tools.client.events.BeforeToolSearchEvent;
import org.iplantc.de.tools.client.events.DeleteToolSelected;
import org.iplantc.de.tools.client.events.EditToolSelected;
import org.iplantc.de.tools.client.events.RefreshToolsSelectedEvent;
import org.iplantc.de.tools.client.events.RequestToMakeToolPublicSelected;
import org.iplantc.de.tools.client.events.RequestToolSelected;
import org.iplantc.de.tools.client.events.ShareToolsSelected;
import org.iplantc.de.tools.client.events.ToolFilterChanged;
import org.iplantc.de.tools.client.events.ToolSearchResultLoadEvent;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

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

        String shareTools();

        String deleteTool();

        String confirmDelete();

        String appsLoadError();
    }


    void loadTools(List<Tool> tools);

    void addTool(Tool tool);

    void removeTool(Tool tool);

    ManageToolsToolbarView getToolbar();

    void updateTool(Tool result);

    HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<Tool> handler);

    HandlerRegistration addShowToolInfoEventHandlers(ShowToolInfoEvent.ShowToolInfoEventHandler handler);

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter,
                                       RefreshToolsSelectedEvent.RefreshToolsSelectedEventHandler,
                                       AddNewToolSelected.NewToolSelectedHandler,
                                       DeleteToolSelected.DeleteToolsSelectedHandler,
                                       ShareToolsSelected.ShareToolsSelectedHandler,
                                       ToolSelectionChangedEvent.ToolSelectionChangedEventHandler,
                                       ToolFilterChanged.ToolFilterChangedHandler,
                                       RequestToolSelected.RequestToolSelectedHandler, EditToolSelected.EditToolSelectedHandler,
                                       RequestToMakeToolPublicSelected.RequestToMakeToolPublicSelectedHandler,
                                       ShowToolInfoEvent.ShowToolInfoEventHandler{
        void setViewDebugId(String baseId);

        void loadTools(Boolean isPublic);

        void addTool(Tool tool, Command dialogCallbackCommand);

        void updateTool(Tool tool, Command dialogCallbackCommand);

        Tool getSelectedTool();

        HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<Tool> handler);
    }
}
