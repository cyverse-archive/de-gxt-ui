package org.iplantc.de.admin.apps.client;

import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.apps.client.models.AppModelKeyProvider;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.util.StaticIdHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppStatsGridViewImpl extends Composite implements AdminAppStatsGridView{

    interface AdminAppStatsGridViewUiBinder
            extends UiBinder<Widget, AdminAppStatsGridViewImpl> {
    }

    private static AdminAppStatsGridViewUiBinder ourUiBinder =
            GWT.create(AdminAppStatsGridViewUiBinder.class);

    @UiField
    Grid<App> grid;
    @UiField
    ColumnModel<App> cm;
    @UiField
    ListStore<App> store;
    @UiField
    Appearance appearance;
    @UiField
    GridView<App> view;

    @UiField
    VerticalLayoutContainer container;

    @Inject
    public AdminAppStatsGridViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        view.setAutoExpandColumn(cm.getColumn(0));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
    }


    @UiFactory
    ColumnModel<App> createColumnModel() {
        ColumnConfig<App, String> appName = new ColumnConfig<>(new ValueProvider<App, String>() {
            @Override
            public String getValue(App object) {
                return object.getName();
            }

            @Override
            public void setValue(App object, String value) {

            }

            @Override
            public String getPath() {
                return "name";

            }
        }, 200, appearance.name());

        ColumnConfig<App, Double> rating = new ColumnConfig<App, Double>(new ValueProvider<App, Double>() {
            @Override
            public Double getValue(App object) {
                return object.getRating().getAverage();
            }

            @Override
            public void setValue(App object, Double value) {

            }

            @Override
            public String getPath() {
                return  "rating.averageRating";
            }
        }, 100, appearance.rating());

        ColumnConfig<App, Integer> total = new ColumnConfig<>(new ValueProvider<App, Integer>() {
            @Override
            public Integer getValue(App object) {
                return object.getAppStats().getTotal();
            }

            @Override
            public void setValue(App object, Integer value) {

            }

            @Override
            public String getPath() {
                return "appStats.total";
            }
        },100, appearance.total());

        ColumnConfig<App,Integer> totalCompleted = new ColumnConfig<>(new ValueProvider<App, Integer>() {
            @Override
            public Integer getValue(App object) {
                return object.getAppStats().getTotalCompleted();
            }

            @Override
            public void setValue(App object, Integer value) {

            }

            @Override
            public String getPath() {
                return "appStats.totalCompleted";
            }
        },100, appearance.completed());

        ColumnConfig<App, Integer> totalFailed = new ColumnConfig<>(new ValueProvider<App, Integer>() {
            @Override
            public Integer getValue(App object) {
                return object.getAppStats().getTotalFailed();
            }

            @Override
            public void setValue(App object, Integer value) {

            }

            @Override
            public String getPath() {
                return "appStats.totalFailed";

            }
        }, 100, appearance.failed());

        ColumnConfig<App, Date> lastCompleted = new ColumnConfig<>(new ValueProvider<App, Date>() {
            @Override
            public Date getValue(App object) {
                return object.getAppStats().getLastCompletedDate();
            }

            @Override
            public void setValue(App object, Date value) {

            }

            @Override
            public String getPath() {
                return "appStats.lastCompletedDate";
            }
        }, 200, appearance.lastCompleted());

        ColumnConfig<App, Date> lastUsed = new ColumnConfig<>(new ValueProvider<App, Date>() {
            @Override
            public Date getValue(App object) {
                return object.getAppStats().getLastUsedDate();
            }

            @Override
            public void setValue(App object, Date value) {

            }

            @Override
            public String getPath() {
                return "appStats.lastUsedDate";
            }
        },200, appearance.lastUsed());


        return new ColumnModel<>(Arrays.<ColumnConfig<App, ?>>asList(appName, rating, total, totalCompleted, totalFailed, lastCompleted, lastUsed));
    }

    @UiFactory
    ListStore<App> createListStore() {
        return new ListStore<>(new AppModelKeyProvider());
    }


    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public void addAll(List<App> apps) {
        store.addAll(apps);
    }

    @Override
    protected void onEnsureDebugId(final String baseID) {
        super.onEnsureDebugId(baseID);

        container.ensureDebugId(baseID);
        grid.asWidget().ensureDebugId(baseID + Belphegor.AppStatIds.GRID);
        grid.addViewReadyHandler(new ViewReadyEvent.ViewReadyHandler() {
            @Override
            public void onViewReady(ViewReadyEvent event) {
                StaticIdHelper.getInstance()
                              .gridColumnHeaders(baseID + Belphegor.AppStatIds.GRID
                                                 + Belphegor.AppStatIds.COL_HEADER, grid);
            }
        });
    }
}
