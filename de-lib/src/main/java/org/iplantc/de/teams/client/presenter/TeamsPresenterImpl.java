package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.CreateTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamNameSelected;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import java.util.List;

/**
 * The presenter to handle all the logic for the Teams view
 *
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter, TeamNameSelected.TeamNameSelectedHandler,
                                           TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler,
                                           CreateTeamSelected.CreateTeamSelectedHandler,
                                           TeamSearchResultLoad.TeamSearchResultLoadHandler {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;
    private TeamSearchRpcProxy searchProxy;
    
    @Inject AsyncProviderWrapper<EditTeamDialog> editTeamDlgProvider;
    @Inject IplantAnnouncer announcer;
    TeamsFilter currentFilter;

    @Inject
    public TeamsPresenterImpl(TeamsView.TeamsViewAppearance appearance,
                              GroupServiceFacade serviceFacade,
                              TeamsViewFactory viewFactory,
                              TeamSearchRpcProxy searchProxy) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.searchProxy = searchProxy;
        this.view = viewFactory.create(getPagingLoader());

        view.addTeamNameSelectedHandler(this);
        view.addTeamFilterSelectionChangedHandler(this);
        view.addCreateTeamSelectedHandler(this);
        searchProxy.addTeamSearchResultLoadHandler(this);
        searchProxy.addTeamSearchResultLoadHandler(view);
    }

    @Override
    public void go() {
        currentFilter = view.getCurrentFilter();
        getSelectedTeams();
    }

    @Override
    public void onTeamNameSelected(TeamNameSelected event) {
        Group group = event.getTeam();
        editTeamDlgProvider.get(new AsyncCallback<EditTeamDialog>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(EditTeamDialog dialog) {
                dialog.show(group);
                dialog.addTeamSavedHandler(new TeamSaved.TeamSavedHandler() {
                    @Override
                    public void onTeamSaved(TeamSaved event) {
                        Group team = event.getGroup();
                        view.updateTeam(team);
                    }
                });
                dialog.addLeaveTeamCompletedHandler(event -> {
                    Group team = event.getTeam();
                    view.removeTeam(team);
                });
            }
        });
    }

    @Override
    public void onTeamFilterSelectionChanged(TeamFilterSelectionChanged event) {
        TeamsFilter filter = event.getFilter();

        if (filter == null || filter.equals(currentFilter)) {
            return;
        }

        currentFilter = filter;

        getSelectedTeams();
    }

    void getSelectedTeams() {
        if (TeamsFilter.MY_TEAMS.equals(currentFilter)) {
            getMyTeams();
        } else {
            getAllTeams();
        }
    }

    void getMyTeams() {
        view.mask(appearance.loadingMask());
        serviceFacade.getMyTeams(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Group> result) {
                view.clearTeams();
                view.addTeams(result);
                view.unmask();
            }
        });
    }

    void getAllTeams() {
        view.mask(appearance.loadingMask());
        serviceFacade.getTeams(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Group> result) {
                view.clearTeams();
                view.addTeams(result);
                view.unmask();
            }
        });
    }

    @Override
    public void onCreateTeamSelected(CreateTeamSelected event) {
        editTeamDlgProvider.get(new AsyncCallback<EditTeamDialog>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(EditTeamDialog dialog) {
                dialog.show(null);
                dialog.addTeamSavedHandler(new TeamSaved.TeamSavedHandler() {
                    @Override
                    public void onTeamSaved(TeamSaved event) {
                        Group team = event.getGroup();
                        view.addTeams(Lists.newArrayList(team));
                    }
                });
                dialog.addLeaveTeamCompletedHandler(event -> {
                    Group team = event.getTeam();
                    view.removeTeam(team);
                });
            }
        });
    }

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> getPagingLoader() {
        return new PagingLoader<>(searchProxy);
    }

    @Override
    public void onTeamSearchResultLoad(TeamSearchResultLoad event) {
        List<Group> teams = event.getSearchResults();
        if (teams != null && !teams.isEmpty()) {
            currentFilter = null;
            view.clearTeams();
            view.addTeams(teams);
        }
    }
}
