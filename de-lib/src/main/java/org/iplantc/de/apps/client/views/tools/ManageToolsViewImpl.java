package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.apps.client.views.ManageToolsView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

import java.util.Arrays;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewImpl implements IsWidget, ManageToolsView {


    @UiTemplate("ManageToolsView.ui.xml")
    interface  ManageToolsViewUiBinder extends UiBinder<Widget, ManageToolsViewImpl> {
    }

    @UiField(provided = true)
    final ListStore<Tool> listStore;

    @UiField
    ColumnModel cm;

    @UiField
    Grid<App> grid;

    @UiField
    GridView<Tool> gridView;

    @UiField
    VerticalLayoutContainer container;

    private static final ManageToolsViewUiBinder uiBinder = GWT.create(ManageToolsViewUiBinder.class);

    public ManageToolsViewImpl() {
         listStore = new ListStore<>(new ModelKeyProvider<Tool>() {
             @Override
             public String getKey(Tool item) {
                 return item.getId();
             }
         });
        uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    ColumnModel<Tool> buildColumnModel() {
        ToolProperties tp = GWT.create(ToolProperties.class);
        ColumnConfig<Tool, String> nameCol = new ColumnConfig<Tool, String>(tp.name(),150, "Name");
        ColumnConfig<Tool, String> version = new ColumnConfig<Tool, String>(tp.version(), 50, "Version");
        return new ColumnModel<>(Arrays.asList(nameCol, version));
    }
}
