package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.CreateTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;
import org.iplantc.de.teams.client.views.dialogs.TeamDetailsDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.List;

/**
 * The presenter to handle all the logic for the Teams view
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter,
                                           TeamInfoButtonSelected.TeamInfoButtonSelectedHandler,
                                           TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler,
                                           CreateTeamSelected.CreateTeamSelectedHandler {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;

    @Inject AsyncProviderWrapper<TeamDetailsDialog> detailsDlgProvider;
    @Inject AsyncProviderWrapper<EditTeamDialog> editTeamDlgProvider;
    TeamsFilter currentFilter;

    @Inject
    public TeamsPresenterImpl(TeamsView.TeamsViewAppearance appearance,
                              GroupServiceFacade serviceFacade,
                              TeamsView view) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.view = view;
        
        view.addTeamInfoButtonSelectedHandler(this);
        view.addTeamFilterSelectionChangedHandler(this);
        view.addCreateTeamSelectedHandler(this);
    }

    @Override
    public void go() {
        currentFilter = view.getCurrentFilter();
        getSelectedTeams();
    }

    @Override
    public void onTeamInfoButtonSelected(TeamInfoButtonSelected event) {
        Group group = event.getGroup();
        serviceFacade.getTeamMembers(group, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Subject> result) {
                detailsDlgProvider.get(new AsyncCallback<TeamDetailsDialog>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }

                    @Override
                    public void onSuccess(TeamDetailsDialog dialog) {
                        dialog.show(group, result);
                    }
                });
            }
        });
    }

    @Override
    public void onTeamFilterSelectionChanged(TeamFilterSelectionChanged event) {
        TeamsFilter filter = event.getFilter();

        if (currentFilter.equals(filter)) {
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
            }
        });
    }
}
