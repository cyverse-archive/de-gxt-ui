package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.apps.client.views.ManageToolsToolbarView;
import org.iplantc.de.apps.client.views.ManageToolsView;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewImpl implements ManageToolsView {


    private final ManageToolsViewAppearance appearance;

    @UiTemplate("ManageToolsView.ui.xml")
    interface ManageToolsViewUiBinder extends UiBinder<Widget, ManageToolsViewImpl> {
    }

    @UiField
    ListStore<Tool> listStore;

    @UiField
    ColumnModel cm;

    @UiField
    Grid<App> grid;

    @UiField
    GridView<Tool> gridView;

    @UiField
    VerticalLayoutContainer container;

    @UiField(provided = true)
    ManageToolsToolbarView toolbar;

    private static final ManageToolsViewUiBinder uiBinder = GWT.create(ManageToolsViewUiBinder.class);

    @Inject
    public ManageToolsViewImpl(ManageToolsView.ManageToolsViewAppearance appearance,
                               ManageToolsToolbarView toolbar) {
        this.appearance = appearance;
        this.toolbar = toolbar;
        uiBinder.createAndBindUi(this);
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
        ToolProperties tp = GWT.create(ToolProperties.class);
        ColumnConfig<Tool, String> nameCol =
                new ColumnConfig<Tool, String>(tp.name(), 150, appearance.name());

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
        }, 200,appearance.imaName());

        ColumnConfig<Tool, String> tag = new ColumnConfig<Tool, String>(new ValueProvider<Tool, String>() {
            @Override
            public String getValue(Tool object) {
                return object.getContainer()!=null ? object.getContainer().getImage().getTag() : "";
            }

            @Override
            public void setValue(Tool object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        },50, appearance.version());
        return new ColumnModel<>(Arrays.asList(nameCol, imgName, tag));
    }

    @Override
    public void loadTools(List<Tool> tools) {
        listStore.addAll(tools);
    }


    @Override
    public void mask(String loadingMask) {
      container.mask(loadingMask);
    }

    @Override
    public void unmask() {
      container.unmask();
    }

}

