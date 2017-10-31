package org.iplantc.de.diskResource.client.views.sharing;

import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.commons.client.views.sharing.SharingAppearance;
import org.iplantc.de.diskResource.client.model.DataSharingKeyProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A view that displays the sharing details to the user, either grouped by user or data
 */
public class ShareBreakDownView extends Composite {

    interface MyUiBinder extends UiBinder<Widget, ShareBreakDownView> {
    }
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField ToolBar toolbar;
    @UiField TextButton groupByUser;
    @UiField TextButton groupByData;
    @UiField VerticalLayoutContainer container;
    @UiField Grid<Sharing> grid;
    @UiField GroupingView<Sharing> groupingView;
    @UiField ListStore<Sharing> listStore;
    @UiField ColumnModel<Sharing> cm;
    @UiField(provided = true) SharingAppearance appearance;

    @Inject
    public ShareBreakDownView(SharingAppearance appearance) {
        this.appearance = appearance;

        initWidget(uiBinder.createAndBindUi(this));

        groupingView.groupBy(cm.getColumn(0));
        groupingView.setAutoExpandColumn(cm.getColumn(0));
        groupingView.setShowGroupedColumn(false);
    }

    @UiHandler("groupByUser")
    void onGroupByUserSelected(SelectEvent event) {
        groupingView.groupBy(grid.getColumnModel().getColumn(0));
    }

    @UiHandler("groupByData")
    void onGroupByDataSelected(SelectEvent event) {
        groupingView.groupBy(grid.getColumnModel().getColumn(1));
    }

    @UiFactory
    ListStore<Sharing> createListStore() {
        return new ListStore<>(new DataSharingKeyProvider());
    }

    @UiFactory
    ColumnModel<Sharing> createColumnModel() {
        List<ColumnConfig<Sharing, ?>> configs = new ArrayList<>();
        ColumnConfig<Sharing, String> name = new ColumnConfig<>(new ValueProvider<Sharing, String>() {

            @Override
            public String getValue(Sharing object) {
                return object.getSubjectName();
            }

            @Override
            public void setValue(Sharing object, String value) {
                // do nothing intentionally

            }

            @Override
            public String getPath() {
                return "";
            }
        });

        name.setHeader(appearance.nameColumnLabel());
        name.setWidth(appearance.shareBreakDownDlgNameColumnWidth());

        ColumnConfig<Sharing, String> resource = new ColumnConfig<>(new ValueProvider<Sharing, String>() {

            @Override
            public String getValue(Sharing object) {
                return object.getName();
            }

            @Override
            public void setValue(Sharing object, String value) {
                // do nothing intentionally

            }

            @Override
            public String getPath() {
                return "";
            }
        });

        resource.setHeader(appearance.nameColumnLabel());
        resource.setWidth(appearance.shareBreakDownDlgNameColumnWidth());
        ColumnConfig<Sharing, PermissionValue> permission = new ColumnConfig<>(new ValueProvider<Sharing, PermissionValue>() {

            @Override
            public PermissionValue getValue(Sharing object) {
                return object.getDisplayPermission();
            }

            @Override
            public void setValue(Sharing object, PermissionValue value) {
                object.setDisplayPermission(value);

            }

            @Override
            public String getPath() {
                return "displayPermission";
            }
        });

        permission.setHeader(appearance.permissionsColumnLabel());
        permission.setWidth(appearance.shareBreakDownDlgPermissionColumnWidth());
        configs.add(name);
        configs.add(resource);
        configs.add(permission);
        return new ColumnModel<>(configs);
    }

    public void loadGrid(List<Sharing> shares) {
        listStore.clear();
        listStore.addAll(shares);
    }
}
