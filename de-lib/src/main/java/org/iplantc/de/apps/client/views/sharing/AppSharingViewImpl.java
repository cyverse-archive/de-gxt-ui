/**
 * 
 * @author sriram
 */
package org.iplantc.de.apps.client.views.sharing;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.sharing.SharingPresenter;

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

public class AppSharingViewImpl implements AppSharingView {

    private static AppSharingViewUiBinder uiBinder = GWT.create(AppSharingViewUiBinder.class);

    @UiTemplate("AppSharingView.ui.xml")
    interface AppSharingViewUiBinder extends UiBinder<Widget, AppSharingViewImpl> {
    }

    @UiField ColumnModel<App> appColumnModel;
    @UiField(provided = true)
    final ListStore<App> appListStore;
    final Widget widget;
    @UiField
    VerticalLayoutContainer container;
    @UiField
    FramedPanel appListPnl;
    @UiField
    Grid<App> appGrid;

    @Inject
    public AppSharingViewImpl() {
        this.appListStore = buildAppStore();
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
    public void setSelectedApps(List<App> models) {
        if (models != null && models.size() > 0) {
            appListStore.clear();
            appListStore.addAll(models);
        }

    }

    @UiFactory
    public ColumnModel<App> buildAppColumnModel() {
        List<ColumnConfig<App, ?>> list = new ArrayList<>();

        ColumnConfig<App, String> name = new ColumnConfig<>(new ValueProvider<App, String>() {

            @Override
            public String getValue(App object) {
                return object.getName();
            }

            @Override
            public void setValue(App object, String value) {
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

    private ListStore<App> buildAppStore() {
        ListStore<App> appStore = new ListStore<>(new ModelKeyProvider<App>() {

            @Override
            public String getKey(App item) {
                return item.getId();
            }
        });
        return appStore;
    }

}
