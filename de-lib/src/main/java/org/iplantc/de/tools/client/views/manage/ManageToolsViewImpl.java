package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.tools.client.events.BeforeToolSearchEvent;
import org.iplantc.de.tools.client.events.ToolSearchResultLoadEvent;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewImpl extends Composite implements ManageToolsView {


    private final ManageToolsViewAppearance appearance;

    private final ToolProperties properties;

    @UiTemplate("ManageToolsView.ui.xml")
    interface ManageToolsViewUiBinder extends UiBinder<Widget, ManageToolsViewImpl> {
    }

    @UiField
    ListStore<Tool> listStore;

    @UiField
    ColumnModel cm;

    @UiField
    Grid<Tool> grid;

    @UiField
    GridView<Tool> gridView;

    @UiField
    VerticalLayoutContainer container;

    @UiField(provided = true)
    ManageToolsToolbarView toolbar;

    private static final ManageToolsViewUiBinder uiBinder = GWT.create(ManageToolsViewUiBinder.class);

    @Inject
    public ManageToolsViewImpl(ManageToolsView.ManageToolsViewAppearance appearance,
                               ManageToolsToolbarView toolbar, ToolProperties properties) {
        this.appearance = appearance;
        this.toolbar = toolbar;
        this.properties = properties;
        uiBinder.createAndBindUi(this);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.MULTI);
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<Tool>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<Tool> event) {
                fireEvent(new ToolSelectionChangedEvent(event.getSelection()));
            }
        });
    }

    @Override
    public Widget asWidget() {
        return container;
    }


    @UiFactory
    ListStore<Tool> buildListStore() {
        return new ListStore<>(new ModelKeyProvider<Tool>() {
            @Override
            public String getKey(Tool item) {
                return item.getId();
            }
        });
    }

    @UiFactory
    ColumnModel<Tool> buildColumnModel() {
        ColumnConfig<Tool, String> nameCol =
                new ColumnConfig<Tool, String>(properties.name(), appearance.nameWidth(), appearance.name());

        ColumnConfig<Tool, String> imgName = new ColumnConfig<Tool, String>(new ValueProvider<Tool, String>() {
            @Override
            public String getValue(Tool object) {
                return object.getContainer()!=null ? object.getContainer().getImage().getName() : "";
            }

            @Override
            public void setValue(Tool object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        }, appearance.imgNameWidth(),appearance.imaName());

        ColumnConfig<Tool, String> tag = new ColumnConfig<Tool, String>(new ValueProvider<Tool, String>() {
            @Override
            public String getValue(Tool object) {
                return (object.getContainer()!=null)? object.getContainer().getImage().getTag() : "";
            }

            @Override
            public void setValue(Tool object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        },appearance.tagWidth(), appearance.tag());

        ColumnConfig<Tool, String> status = new ColumnConfig<Tool, String>(new ValueProvider<Tool, String>() {
            @Override
            public String getValue(Tool object) {
                return (object.isPublic())? "Public" : object.getPermission();
            }

            @Override
            public void setValue(Tool object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        },50, appearance.status());
        return new ColumnModel<>(Arrays.asList(nameCol, imgName, tag, status));
    }

    @Override
    public void updateTool(Tool result) {
        Tool tool = listStore.findModelWithKey(result.getId());
        listStore.remove(tool);
        listStore.add(result);
    }

    @Override
    public void loadTools(List<Tool> tools) {
        listStore.clear();
        listStore.addAll(tools);
    }

    @Override
    public void addTool(Tool tool) {
        listStore.add(tool);
    }

    @Override
    public void removeTool(Tool tool) {
        listStore.remove(tool);
    }

    @Override
    public ManageToolsToolbarView getToolbar() {
        return toolbar;
    }


    @Override
    public void mask(String loadingMask) {
        container.mask(loadingMask);
    }

    @Override
    public void unmask() {
        container.unmask();
    }

    @Override
    public void onBeforeToolSearch(BeforeToolSearchEvent event) {
        mask(appearance.mask());
    }

    @Override
    public void onToolSearchResultLoad(ToolSearchResultLoadEvent event) {
        toolbar.resetFilter();
        listStore.clear();
        listStore.addAll(event.getResults());
        unmask();
    }

    @Override
    public HandlerRegistration addToolSelectionChangedEventHandler(ToolSelectionChangedEvent.ToolSelectionChangedEventHandler handler) {
        return addHandler(handler, ToolSelectionChangedEvent.TYPE);
    }

}

