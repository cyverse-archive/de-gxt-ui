package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.apps.client.models.ToolFilter;
import org.iplantc.de.apps.integration.client.presenter.ToolSearchRPCProxy;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.sharing.PermissionValue;
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
import org.iplantc.de.tools.client.events.UseToolInNewAppEvent;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.collect.Lists;
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
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import java.util.Arrays;
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

    @UiField(provided = true)
    SimpleComboBox<ToolFilter> filterCombo;

    private static final ManageToolsViewToolbarUiBinder uiBinder =
            GWT.create(ManageToolsViewToolbarUiBinder.class);

    final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader;
    final ToolSearchRPCProxy toolSearchRPCProxy = new ToolSearchRPCProxy();

    protected List<Tool> currentSelection = Lists.newArrayList();

    @Inject
    EventBus eventBus;

    @Inject
    public ManageToolsViewToolbarImpl(final ManageToolsToolbarView.ManageToolsToolbarAppearance appearance) {
        loader = new PagingLoader<>(toolSearchRPCProxy);
        toolSearch = new ToolSearchField(loader);
        toolSearchRPCProxy.setHasHandlers(this);
        filterCombo = new SimpleComboBox<>(item -> item.getFilterString());
        filterCombo.add(Arrays.asList(ToolFilter.ALL, ToolFilter.MY_TOOLS, ToolFilter.PUBLIC));
        filterCombo.setEditable(false);
        filterCombo.setValue(ToolFilter.ALL);
        filterCombo.addSelectionHandler(selectionEvent -> {
            onFilterChange(selectionEvent.getSelectedItem());
            clearSearch();
        });

        filterCombo.addValueChangeHandler(valueChangeEvent -> valueChangeEvent.getValue());

        initWidget(uiBinder.createAndBindUi(this));
    }

    private void onFilterChange(ToolFilter af) {
        switch (af) {
            case ALL:
                applyFilter(ToolFilter.ALL);
                break;
            case PUBLIC:
                applyFilter(ToolFilter.PUBLIC);
                break;

            case MY_TOOLS:
                applyFilter(ToolFilter.MY_TOOLS);
                break;
        }
    }

    void applyFilter(ToolFilter filter) {
        fireEvent(new ToolFilterChanged(filter));
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolsMenuButton.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_TOOLS);
        addTool.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_ADD_TOOLS);
        edit.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_EDIT);
        requestTool.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_REQUEST_TOOL);
        delete.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_DELETE);
        useInApp.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_USE_IN_APPS);

        shareMenuButton.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_SHARE);
        shareCollab.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_SHARE_COLLABS);
        sharePublic.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_SHARE_PUBLIC);

        refreshButton.ensureDebugId(baseID + ToolsModule.ToolIds.MENU_ITEM_REFRESH);

        toolSearch.ensureDebugId(baseID + ToolsModule.ToolIds.TOOL_SEARCH);
    }

    @UiHandler("addTool")
    void onAddClicked(SelectionEvent<Item> event) {
        fireEvent(new AddNewToolSelected());
    }

    @UiHandler("requestTool")
    void onRequestToolClicked(SelectionEvent<Item> event) {
        fireEvent(new RequestToolSelected());
    }

    @UiHandler("edit")
    void onEditClicked(SelectionEvent<Item> event) {
        fireEvent(new EditToolSelected());
    }

    @UiHandler("delete")
    void onDeleteClicked(SelectionEvent<Item> event) {
        fireEvent(new DeleteToolSelected());
    }

    @UiHandler("useInApp")
    void onUseInAppClicked(SelectionEvent<Item> event) {
       eventBus.fireEvent(new UseToolInNewAppEvent(currentSelection.get(0)));
    }

    @UiHandler("shareCollab")
    void onShareCollabClicked(SelectionEvent<Item> event) {
        fireEvent(new ShareToolsSelected());
    }

    @UiHandler("sharePublic")
    void onSharePublicClicked(SelectionEvent<Item> event) {
        fireEvent(new RequestToMakeToolPublicSelected());
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
    public HandlerRegistration addNewToolSelectedHandler(AddNewToolSelected.NewToolSelectedHandler handler) {
        return addHandler(handler, AddNewToolSelected.TYPE);
    }

    @Override
    public HandlerRegistration addDeleteToolsSelectedHandler(DeleteToolSelected.DeleteToolsSelectedHandler handler) {
        return addHandler(handler, DeleteToolSelected.TYPE);
    }

    @Override
    public HandlerRegistration addShareToolselectedHandler(ShareToolsSelected.ShareToolsSelectedHandler handler) {
        return addHandler(handler, ShareToolsSelected.TYPE);
    }

    @Override
    public HandlerRegistration addToolFilterChangedHandler(ToolFilterChanged.ToolFilterChangedHandler handler) {
        return addHandler(handler, ToolFilterChanged.TYPE);
    }

    @Override
    public HandlerRegistration addRequestToolSelectedHandler(RequestToolSelected.RequestToolSelectedHandler handler) {
        return addHandler(handler, RequestToolSelected.TYPE);
    }

    @Override
    public HandlerRegistration addEditToolSelectedHandler(EditToolSelected.EditToolSelectedHandler handler) {
        return addHandler(handler, EditToolSelected.TYPE);
    }

    @Override
    public HandlerRegistration addRequestToMakeToolPublicSelectedHandler(RequestToMakeToolPublicSelected.RequestToMakeToolPublicSelectedHandler handler) {
        return addHandler(handler, RequestToMakeToolPublicSelected.TYPE);
    }


    @Override
    public void setSelection(List<Tool> currentSelection) {
        this.currentSelection = currentSelection;
        setButtonState(currentSelection);
    }

    @Override
    public void clearSearch() {
        toolSearch.clear();
    }

    @Override
    public void resetFilter() {
        filterCombo.reset();
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
                    delete.setEnabled(false);
                    useInApp.setEnabled(false);
                    shareMenuButton.setEnabled(true);
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

}
