package org.iplantc.de.groups.client.views;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.groups.client.model.GroupProperties;
import org.iplantc.de.groups.shared.GroupsModule;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;
import javax.inject.Inject;

/**
 * @author aramsey
 */
public class GroupViewImpl extends Composite implements GroupView {

    interface GroupViewImplUiBinder extends UiBinder<Widget, GroupViewImpl> {}

    private static GroupViewImplUiBinder uiBinder = GWT.create(GroupViewImplUiBinder.class);

    @UiField ToolBar toolBar;
    @UiField TextButton addGroup;
    @UiField TextButton deleteGroup;
    @UiField Grid<Group> grid;
    @UiField(provided = true) ListStore<Group> listStore;
    @UiField ColumnModel<Group> cm;
    @UiField(provided = true) GroupViewAppearance appearance;

    private final GroupProperties props;

    @Inject
    public GroupViewImpl(GroupViewAppearance appearance,
                         GroupProperties props,
                         @Assisted ListStore<Group> listStore) {
        this.appearance = appearance;
        this.props = props;
        this.listStore = listStore;

        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
    }

    @UiFactory
    ColumnModel<Group> createColumnModel() {
        List<ColumnConfig<Group, ?>> columns = Lists.newArrayList();

        ColumnConfig<Group, Group> nameCol = new ColumnConfig<>(new IdentityValueProvider<Group>("name"),
                                                                appearance.nameColumnWidth(),
                                                                appearance.nameColumnLabel());
        ColumnConfig<Group, String> descriptionCol = new ColumnConfig<>(props.description(),
                                                                        appearance.descriptionColumnWidth(),
                                                                        appearance.descriptionColumnLabel());
        columns.add(nameCol);
        columns.add(descriptionCol);
        return new ColumnModel<>(columns);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        addGroup.ensureDebugId(baseID + GroupsModule.Ids.ADD_GROUP);
        deleteGroup.ensureDebugId(baseID + GroupsModule.Ids.DELETE_GROUP);
        grid.ensureDebugId(baseID + GroupsModule.Ids.GRID);
    }
}
