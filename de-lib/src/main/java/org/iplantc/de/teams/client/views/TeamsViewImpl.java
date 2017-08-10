package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.models.GroupProperties;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.cells.TeamInfoCell;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

/**
 * @author aramsey
 */
public class TeamsViewImpl extends Composite implements TeamsView {

    interface MyUiBinder extends UiBinder<Widget, TeamsViewImpl> {}
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true) TeamsViewAppearance appearance;
    @UiField ToolBar toolbar;
    @UiField TextButton teamsMenu;
    @UiField MenuItem newTeamMI;
    @UiField MenuItem manageTeamMI;
    @UiField MenuItem leaveTeamMI;
    @UiField SimpleComboBox<TeamsFilter> teamFilter;
    @UiField ColumnModel<Group> cm;
    @UiField Grid<Group> grid;
    @UiField GridView<Group> gridView;
    @UiField ListStore<Group> listStore;
    private GroupProperties properties;
    private TeamInfoCell infoCell;

    @Inject
    public TeamsViewImpl(TeamsViewAppearance appearance,
                         GroupProperties properties,
                         TeamInfoCell infoCell) {
        this.appearance = appearance;
        this.properties = properties;
        this.infoCell = infoCell;

        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.MULTI);
    }

    @UiFactory
    SimpleComboBox<TeamsFilter> createTeamComboBox() {
        SimpleComboBox<TeamsFilter> combo = new SimpleComboBox<>(new StringLabelProvider<TeamsFilter>());
        combo.add(Lists.newArrayList(TeamsFilter.MY_TEAMS, TeamsFilter.ALL));
        combo.setValue(TeamsFilter.MY_TEAMS);
        combo.addSelectionHandler(new SelectionHandler<TeamsFilter>() {
            @Override
            public void onSelection(SelectionEvent<TeamsFilter> event) {
                fireEvent(new TeamFilterSelectionChanged(combo.getCurrentValue()));
            }
        });

        return combo;
    }

    @UiFactory
    ColumnModel<Group> createColumnModel() {
        List<ColumnConfig<Group, ?>> list = Lists.newArrayList();
        ColumnConfig<Group, Group> infoCol = new ColumnConfig<>(new IdentityValueProvider<Group>(""),
                                                                appearance.infoColWidth());

        ColumnConfig<Group, String> nameCol = new ColumnConfig<>(properties.extension(),
                                                                 appearance.nameColumnWidth(),
                                                                 appearance.nameColumnLabel());
        ColumnConfig<Group, String> descCol = new ColumnConfig<>(properties.description(),
                                                                 appearance.descColumnWidth(),
                                                                 appearance.descColumnLabel());
        infoCol.setCell(infoCell);
        list.add(infoCol);
        list.add(nameCol);
        list.add(descCol);
        return new ColumnModel<>(list);
    }

    @UiFactory
    ListStore<Group> createListStore() {
        return new ListStore<>(properties.id());
    }

    @UiHandler("newTeamMI")
    void onNewTeamSelected(SelectionEvent<Item> event) {

    }

    @UiHandler("manageTeamMI")
    void onManageTeamSelected(SelectionEvent<Item> event) {

    }

    @UiHandler("leaveTeamMI")
    void onLeaveTeamSelected(SelectionEvent<Item> event) {

    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        toolbar.ensureDebugId(baseID + Teams.Ids.TEAMS_TOOLBAR);
        String teamsMenuId = toolbar.getId() + Teams.Ids.TEAMS_MENU;
        teamsMenu.ensureDebugId(teamsMenuId);
        newTeamMI.ensureDebugId(teamsMenuId + Teams.Ids.CREATE_TEAM);
        manageTeamMI.ensureDebugId(teamsMenuId + Teams.Ids.MANAGE_TEAM);
        leaveTeamMI.ensureDebugId(teamsMenuId + Teams.Ids.LEAVE_TEAM);
        teamFilter.asWidget().ensureDebugId(teamsMenuId + Teams.Ids.FILTER_TEAMS);
        grid.ensureDebugId(baseID + Teams.Ids.GRID);

        for (ColumnConfig<Group, ?> cc : cm.getColumns()) {
            if (cc.getCell() instanceof TeamInfoCell) {
                ((TeamInfoCell)cc.getCell()).setBaseDebugId(baseID);
            }
        }
    }

    @Override
    public HandlerRegistration addTeamInfoButtonSelectedHandler(TeamInfoButtonSelected.TeamInfoButtonSelectedHandler handler) {
        return infoCell.addTeamInfoButtonSelectedHandler(handler);
    }

    @Override
    public void addTeams(List<Group> result) {
        if (result != null && !result.isEmpty()) {
            listStore.addAll(result);
        }
    }

    @Override
    public void clearTeams() {
        listStore.clear();
    }

    @Override
    public TeamsFilter getCurrentFilter() {
        return teamFilter.getCurrentValue();
    }

    @Override
    public HandlerRegistration addTeamFilterSelectionChangedHandler(TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler handler) {
        return addHandler(handler, TeamFilterSelectionChanged.TYPE);
    }
}
