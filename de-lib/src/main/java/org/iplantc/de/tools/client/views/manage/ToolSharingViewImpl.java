/**
 * 
 * @author sriram
 */
package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;
import java.util.List;

public class ToolSharingViewImpl implements ToolSharingView {

    private static ToolSharingViewUiBinder uiBinder = GWT.create(ToolSharingViewUiBinder.class);

    private ManageToolsView.ManageToolsViewAppearance toolsViewAppearance;

    @UiTemplate("ToolSharingView.ui.xml")
    interface ToolSharingViewUiBinder extends UiBinder<Widget, ToolSharingViewImpl> {
    }

    @UiField ColumnModel<Tool> toolColumnModel;
    @UiField
     ListStore<Tool> toolListStore;
    final Widget widget;
    @UiField
    VerticalLayoutContainer container;
    @UiField
    FramedPanel ToolListPnl;
    @UiField
    Grid<Tool> ToolGrid;

    final private ToolProperties toolProperties;

    @Inject
    public ToolSharingViewImpl(ToolProperties toolProperties,
                               ManageToolsView.ManageToolsViewAppearance toolsViewAppearance) {
        this.toolsViewAppearance = toolsViewAppearance;
        this.toolProperties = toolProperties;
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public void addShareWidget(Widget widget) {
        container.add(widget);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setSelectedTools(List<Tool> models) {
        if (models != null && models.size() > 0) {
            toolListStore.clear();
            toolListStore.addAll(models);
        }

    }

    @UiFactory
    public ColumnModel<Tool> buildToolColumnModel() {
        List<ColumnConfig<Tool, ?>> list = new ArrayList<>();

        ColumnConfig<Tool, String> name =
                new ColumnConfig<>(toolProperties.name(), 180, toolsViewAppearance.name());
        list.add(name);
        return new ColumnModel<>(list);
    }

    @UiFactory
    public ListStore<Tool> buildToolStore() {
        ListStore<Tool> toolListStore = new ListStore<>(new ModelKeyProvider<Tool>() {

            @Override
            public String getKey(Tool item) {
                return item.getId();
            }
        });
        return toolListStore;
    }

}
