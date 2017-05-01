/**
 * 
 * @author sriram
 */
package org.iplantc.de.apps.client.views.tools;

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
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.ArrayList;
import java.util.List;

public class ToolSharingViewImpl implements ToolSharingView {

    private static ToolSharingViewUiBinder uiBinder = GWT.create(ToolSharingViewUiBinder.class);

    @UiTemplate("ToolSharingView.ui.xml")
    interface ToolSharingViewUiBinder extends UiBinder<Widget, ToolSharingViewImpl> {
    }

    @UiField ColumnModel<Tool> ToolColumnModel;
    @UiField(provided = true)
    final ListStore<Tool> ToolListStore;
    final Widget widget;
    @UiField
    VerticalLayoutContainer container;
    @UiField
    FramedPanel ToolListPnl;
    @UiField
    Grid<Tool> ToolGrid;

    @Inject
    public ToolSharingViewImpl() {
        this.ToolListStore = buildToolStore();
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
            ToolListStore.clear();
            ToolListStore.addAll(models);
        }

    }

    @UiFactory
    public ColumnModel<Tool> buildToolColumnModel() {
        List<ColumnConfig<Tool, ?>> list = new ArrayList<>();

        ColumnConfig<Tool, String> name = new ColumnConfig<>(new ValueProvider<Tool, String>() {

            @Override
            public String getValue(Tool object) {
                return object.getName();
            }

            @Override
            public void setValue(Tool object, String value) {
                // TODO Auto-generated method stub
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 180, "Name");
        list.add(name);
        return new ColumnModel<>(list);
    }

    private ListStore<Tool> buildToolStore() {
        ListStore<Tool> ToolStore = new ListStore<>(new ModelKeyProvider<Tool>() {

            @Override
            public String getKey(Tool item) {
                return item.getId();
            }
        });
        return ToolStore;
    }

}
