package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.apps.client.events.tools.BeforeToolSearchEvent;
import org.iplantc.de.apps.client.events.tools.RefreshToolsSelectedEvent;
import org.iplantc.de.apps.client.events.tools.ToolSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.tools.ToolSelectionChangedEvent;
import org.iplantc.de.apps.integration.client.presenter.ToolSearchRPCProxy;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import java.util.List;

/**
 * Created by sriram on 4/21/17.
 */

public class ManageToolsViewToolbarImpl extends Composite implements ManageToolsToolbarView {

    @UiTemplate("ManageToolsToolbar.ui.xml")
    interface ManageToolsViewToolbarUiBinder extends UiBinder<Widget, ManageToolsViewToolbarImpl> {

    }

    @UiField
    TextButton toolsMenuButton;

    @UiField
    TextButton shareMenuButton;

    @UiField
    MenuItem shareCollab;

    @UiField
    MenuItem sharePublic;

    @UiField
    TextButton refreshButton;

    @UiField(provided = true)
    ToolSearchField toolSearch;

    @UiField
    MenuItem addTool;

    @UiField
    MenuItem requestTool;

    @UiField
    MenuItem edit;

    @UiField
    MenuItem delete;

    @UiField
    MenuItem useInApp;

    private static final ManageToolsViewToolbarUiBinder uiBinder =
            GWT.create(ManageToolsViewToolbarUiBinder.class);

    final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader;
    final ToolSearchRPCProxy toolSearchRPCProxy = new ToolSearchRPCProxy();

    @Inject
    public ManageToolsViewToolbarImpl(final ManageToolsToolbarView.ManageToolsToolbarAppearance apperance) {
        loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>>(toolSearchRPCProxy);
        toolSearch = new ToolSearchField(loader);
        initWidget(uiBinder.createAndBindUi(this));
        toolSearchRPCProxy.setHasHandlers(this);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolsMenuButton.ensureDebugId(baseID + AppsModule.ToolIds.MENU_TOOLS);
        addTool.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_ADD_TOOLS);
        edit.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_EDIT);
        requestTool.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_REQUEST_TOOL);
        delete.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_DELETE);
        useInApp.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_USE_IN_APPS);

        shareMenuButton.ensureDebugId(baseID + AppsModule.ToolIds.MENU_SHARE);
        shareCollab.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_SHARE_COLLABS);
        sharePublic.ensureDebugId(baseID + AppsModule.ToolIds.MENU_ITEM_SHARE_PUBLIC);
    }

    @UiHandler("addTool")
    void onAddClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("requestTool")
    void onRequestToolClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("edit")
    void onEditClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("delete")
    void onDeleteClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("useInApp")
    void onUseInAppClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("shareCollab")
    void onShareCollabClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("sharePublic")
    void onSharePublicClicked(SelectionEvent<Item> event) {

    }

    @UiHandler("refreshButton")
    void onRefresh(SelectEvent event) {
        fireEvent(new RefreshToolsSelectedEvent());
    }

    @Override
    public HandlerRegistration addBeforeToolSearchEventHandler(BeforeToolSearchEvent.BeforeToolSearchEventHandler handler) {
        return addHandler(handler, BeforeToolSearchEvent.TYPE);
    }

    @Override
    public HandlerRegistration addToolSearchResultLoadEventHandler(ToolSearchResultLoadEvent.ToolSearchResultLoadEventHandler handler) {
        return addHandler(handler, ToolSearchResultLoadEvent.TYPE);
    }


    @Override
    public HandlerRegistration addRefreshToolsSelectedEventHandler(RefreshToolsSelectedEvent.RefreshToolsSelectedEventHandler handler) {
        return addHandler(handler, RefreshToolsSelectedEvent.TYPE);
    }

    @Override
    public void onToolSelectionChanged(ToolSelectionChangedEvent event) {
        setButtonState(event.getToolSelection());
    }

    private void setButtonState(List<Tool> tools) {
        //view support single selection only
        if (tools != null) {
            switch (tools.size()) {
                case 0:
                      addTool.setEnabled(true);
                      requestTool.setEnabled(true);
                      edit.setEnabled(false);
                      delete.setEnabled(false);
                      useInApp.setEnabled(false);
                      shareMenuButton.setEnabled(false);
                    break;
                case 1:
                    Tool selection = tools.get(0);
                    addTool.setEnabled(true);
                    requestTool.setEnabled(true);
                    edit.setEnabled(isEditable(selection));
                    delete.setEnabled(isOwner(selection));
                    useInApp.setEnabled(true);
                    shareMenuButton.setEnabled(isOwner(selection));
                    shareCollab.setEnabled(isOwner(selection));
                    sharePublic.setEnabled(isOwner(selection));
                    break;
                default:
                    addTool.setEnabled(true);
                    requestTool.setEnabled(true);
                    edit.setEnabled(false);
                    delete.setEnabled(true);
                    useInApp.setEnabled(false);
                    shareMenuButton.setEnabled(false);
                    shareCollab.setEnabled(true);
                    sharePublic.setEnabled(false);
            }
        }

    }

    private boolean isEditable(Tool tool) {
        return !tool.isPublic() && (isOwner(tool) || hasWritePermission(tool));
    }

    private boolean isOwner(Tool tool) {
        return tool.getPermission().equalsIgnoreCase(PermissionValue.own.toString());
    }

    private boolean hasWritePermission(Tool tool) {
        return tool.getPermission().equalsIgnoreCase(PermissionValue.write.toString());
    }

    private boolean hasReadPermission(Tool tool) {
        return tool.getPermission().equalsIgnoreCase(PermissionValue.read.toString());
    }


}
