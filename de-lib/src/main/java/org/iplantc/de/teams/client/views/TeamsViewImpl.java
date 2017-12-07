package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.CreateTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamNameSelected;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.models.GroupProperties;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.cells.TeamNameCell;
import org.iplantc.de.teams.client.views.widgets.TeamSearchField;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.Comparator;
import java.util.List;

/**
 * @author aramsey
 */
public class TeamsViewImpl extends Composite implements TeamsView {

    class TeamNameComparator implements Comparator<Group> {

        @Override
        public int compare(Group o1, Group o2) {
            return o1.getGroupShortName().compareToIgnoreCase(o2.getGroupShortName());
        }
    }

    interface MyUiBinder extends UiBinder<Widget, TeamsViewImpl> {}
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true) TeamsViewAppearance appearance;
    @UiField ToolBar toolbar;
    @UiField TextButton createTeam;
    @UiField SimpleComboBox<TeamsFilter> teamFilter;
    @UiField TeamSearchField searchField;
    @UiField ColumnModel<Group> cm;
    @UiField Grid<Group> grid;
    @UiField GridView<Group> gridView;
    @UiField ListStore<Group> listStore;
    private GroupProperties properties;
    private CheckBoxSelectionModel<Group> checkBoxSelectionModel;
    private ColumnConfig<Group, Group> checkBoxCol;
    private TeamNameCell nameCell;
    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loader;

    @Inject
    public TeamsViewImpl(TeamsViewAppearance appearance,
                         GroupProperties properties,
                         TeamNameCell nameCell,
                         @Assisted PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loader) {
        this.appearance = appearance;
        this.properties = properties;
        this.nameCell = nameCell;
        this.loader = loader;
        this.checkBoxSelectionModel = getCheckBoxSelectionModel();

        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.MULTI);
        grid.setLoader(loader);
    }

    @UiFactory
    SimpleComboBox<TeamsFilter> createTeamComboBox() {
        SimpleComboBox<TeamsFilter> combo = new SimpleComboBox<>(new StringLabelProvider<TeamsFilter>());
        combo.add(Lists.newArrayList(TeamsFilter.MY_TEAMS, TeamsFilter.ALL));
        combo.setValue(TeamsFilter.MY_TEAMS);
        combo.addSelectionHandler(event -> {
            searchField.clear();
            applyFilter(combo.getCurrentValue());
        });

        return combo;
    }

    @UiFactory
    ColumnModel<Group> createColumnModel() {
        List<ColumnConfig<Group, ?>> list = Lists.newArrayList();
        checkBoxCol = checkBoxSelectionModel.getColumn();
        ColumnConfig<Group, Group> nameCol = new ColumnConfig<>(new IdentityValueProvider<>("extension"),
                                                                 appearance.nameColumnWidth(),
                                                                 appearance.nameColumnLabel());
        ColumnConfig<Group, String> creatorCol = new ColumnConfig<>(properties.creator(),
                                                                    appearance.creatorColumnWidth(),
                                                                    appearance.creatorColumnLabel());
        ColumnConfig<Group, String> descCol = new ColumnConfig<>(properties.description(),
                                                                 appearance.descColumnWidth(),
                                                                 appearance.descColumnLabel());
        nameCol.setCell(nameCell);
        nameCol.setComparator(new TeamNameComparator());
        list.add(checkBoxCol);
        list.add(nameCol);
        list.add(creatorCol);
        list.add(descCol);

        checkBoxCol.setHidden(true);
        return new ColumnModel<>(list);
    }

    @UiFactory
    ListStore<Group> createListStore() {
        return new ListStore<>(properties.id());
    }

    @UiFactory
    TeamSearchField createSearchField() {
        return new TeamSearchField(loader);
    }

    @UiHandler("createTeam")
    void onNewTeamSelected(SelectEvent event) {
        fireEvent(new CreateTeamSelected());
    }

    @UiHandler("searchField")
    void searchFieldKeyUp(KeyUpEvent event) {
        if (Strings.isNullOrEmpty(searchField.getCurrentValue())) {
            applyFilter(TeamsFilter.ALL);
        } else {
            applyFilter(null);
        }
    }

    void applyFilter(TeamsFilter filter) {
        teamFilter.setValue(filter);
        fireEvent(new TeamFilterSelectionChanged(filter));
    }

    @Override
    public void onTeamSearchResultLoad(TeamSearchResultLoad event) {
        teamFilter.setText(null);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        String toolbarId = baseID + Teams.Ids.TEAMS_TOOLBAR;
        toolbar.ensureDebugId(toolbarId);
        createTeam.ensureDebugId(toolbarId + Teams.Ids.CREATE_TEAM);
        teamFilter.asWidget().ensureDebugId(toolbarId + Teams.Ids.FILTER_TEAMS);
        searchField.asWidget().ensureDebugId(toolbarId + Teams.Ids.SEARCH_FIELD);
        grid.ensureDebugId(baseID + Teams.Ids.GRID);

        for (ColumnConfig<Group, ?> cc : cm.getColumns()) {
            if (cc.getCell() instanceof TeamNameCell) {
                ((TeamNameCell)cc.getCell()).setBaseDebugId(baseID);
            }
        }
    }

    @Override
    public HandlerRegistration addTeamNameSelectedHandler(TeamNameSelected.TeamNameSelectedHandler handler) {
        return nameCell.addTeamNameSelectedHandler(handler);
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
    public void updateTeam(Group team) {
        listStore.update(team);
    }

    @Override
    public void removeTeam(Group team) {
        Group found = listStore.findModel(team);
        if (found != null) {
            listStore.remove(found);
        }
    }

    @Override
    public void showCheckBoxes() {
        grid.setSelectionModel(checkBoxSelectionModel);
        checkBoxCol.setHidden(false);
        grid.getView().refresh(true);
    }

    @Override
    public List<Group> getSelectedTeams() {
        return grid.getSelectionModel().getSelectedItems();
    }

    CheckBoxSelectionModel<Group> getCheckBoxSelectionModel() {
        return new CheckBoxSelectionModel<>(new IdentityValueProvider<Group>());
    }

    @Override
    public HandlerRegistration addTeamFilterSelectionChangedHandler(TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler handler) {
        return addHandler(handler, TeamFilterSelectionChanged.TYPE);
    }

    @Override
    public HandlerRegistration addCreateTeamSelectedHandler(CreateTeamSelected.CreateTeamSelectedHandler handler) {
        return addHandler(handler, CreateTeamSelected.TYPE);
    }
}
