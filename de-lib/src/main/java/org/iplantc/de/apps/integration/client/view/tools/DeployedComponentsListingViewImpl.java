/**
 *
 */
package org.iplantc.de.apps.integration.client.view.tools;

import org.iplantc.de.apps.integration.client.events.ShowToolInfoEvent;
import org.iplantc.de.apps.integration.client.model.DeployedComponentProperties;
import org.iplantc.de.apps.integration.client.view.deployedComponents.cells.DCNameHyperlinkCell;
import org.iplantc.de.apps.integration.client.dialogs.ToolInfoDialog;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.widgets.SearchField;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.tools.requests.client.views.dialogs.NewToolRequestDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A grid that displays list of available deployed components (bin/tools) in Condor
 *
 * @author sriram, jstroot
 */
public class DeployedComponentsListingViewImpl extends Composite implements
                                                                 DeployedComponentsListingView,
                                                                 ShowToolInfoEvent.ShowToolInfoEventHandler {

    @UiTemplate("DeployedComponentsListingView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DeployedComponentsListingViewImpl> { }

    @UiField VerticalLayoutContainer container;
    @UiField Grid<Tool> grid;
    @UiField TextButton newToolBtn;
    @UiField SearchField<Tool> searchField;
    @UiField DeployedComponentsListingViewAppearance appearance;
    @UiField(provided = true) ListStore<Tool> store;
    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader;
    private DCNameHyperlinkCell nameCell;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @Inject AsyncProviderWrapper<NewToolRequestDialog> newToolRequestDialogProvider;
    @Inject AsyncProviderWrapper<ToolInfoDialog> toolInfoDialogProvider;

    @Inject
    DeployedComponentsListingViewImpl(@Assisted ListStore<Tool> listStore,
                                      @Assisted PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader,
                                      DCNameHyperlinkCell nameCell) {
        this.store = listStore;
        this.loader = loader;
        this.nameCell = nameCell;
        initWidget(uiBinder.createAndBindUi(this));
        grid.setLoader(loader);
    }

    @Override
    public Tool getSelectedDC() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public void onShowToolInfo(ShowToolInfoEvent event) {
        Tool dc = event.getTool();
        toolInfoDialogProvider.get(new AsyncCallback<ToolInfoDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(ToolInfoDialog result) {
                result.show(dc);
            }
        });
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        searchField.ensureDebugId(baseID + AppIntegrationModule.Ids.SEARCH);
        newToolBtn.ensureDebugId(baseID + AppIntegrationModule.Ids.NEW_TOOL_REQUEST);
    }

    @UiFactory
    SearchField<Tool> createAppSearchField() {
        return new SearchField<>(loader);
    }

    @UiFactory
    ColumnModel<Tool> createColumnModel() {
        DeployedComponentProperties properties = GWT.create(DeployedComponentProperties.class);
        IdentityValueProvider<Tool> provider = new IdentityValueProvider<>("name");
        List<ColumnConfig<Tool, ?>> configs = new LinkedList<>();

        ColumnConfig<Tool, Tool> name = new ColumnConfig<>(provider, appearance.nameColumnWidth());
        name.setComparator(new Comparator<Tool>() {

            @Override
            public int compare(Tool o1, Tool o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        name.setSortable(true);
        name.setHeader(appearance.nameColumnHeader());
        configs.add(name);
        name.setCell(nameCell);
        nameCell.addShowToolInfoEventHandlers(this);
        name.setMenuDisabled(true);

        ColumnConfig<Tool, String> version = new ColumnConfig<>(properties.version(), appearance.versionColumnWidth());
        version.setHeader(appearance.versionColumnHeader());
        configs.add(version);
        version.setMenuDisabled(true);

        ColumnConfig<Tool, String> path = new ColumnConfig<>(properties.location(), appearance.pathColumnWidth());
        path.setHeader(appearance.pathColumnHeader());
        configs.add(path);
        path.setMenuDisabled(true);
        return new ColumnModel<>(configs);
    }

    @UiHandler({"newToolBtn"})
    void onNewToolRequestBtnClick(@SuppressWarnings("unused") SelectEvent event) {
       newToolRequestDialogProvider.get(new AsyncCallback<NewToolRequestDialog>() {
           @Override
           public void onFailure(Throwable caught) {
               ErrorHandler.post(caught);
           }

           @Override
           public void onSuccess(NewToolRequestDialog result) {
                result.show();
           }
       });
    }

    @Override
    public HandlerRegistration addSelectionChangedHandler(SelectionChangedHandler<Tool> handler) {
        grid.getSelectionModel().addSelectionChangedHandler(handler);
        return addHandler(handler, SelectionChangedEvent.getType());
    }
}
