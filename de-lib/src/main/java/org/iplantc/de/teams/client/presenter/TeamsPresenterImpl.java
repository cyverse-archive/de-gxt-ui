package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.CreateTeamSelected;
import org.iplantc.de.teams.client.events.EditTeamSelected;
import org.iplantc.de.teams.client.events.LeaveTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;
import org.iplantc.de.teams.client.views.dialogs.LeaveTeamDialog;
import org.iplantc.de.teams.client.views.dialogs.TeamDetailsDialog;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import java.util.List;

/**
 * The presenter to handle all the logic for the Teams view
 *
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter,
                                           TeamInfoButtonSelected.TeamInfoButtonSelectedHandler,
                                           TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler,
                                           CreateTeamSelected.CreateTeamSelectedHandler,
                                           EditTeamSelected.EditTeamSelectedHandler,
                                           TeamSearchResultLoad.TeamSearchResultLoadHandler,
                                           LeaveTeamSelected.LeaveTeamSelectedHandler {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;
    private TeamSearchRpcProxy searchProxy;

    @Inject AsyncProviderWrapper<TeamDetailsDialog> detailsDlgProvider;
    @Inject AsyncProviderWrapper<EditTeamDialog> editTeamDlgProvider;
    @Inject AsyncProviderWrapper<LeaveTeamDialog> leaveTeamDlgProvider;
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

        view.addTeamInfoButtonSelectedHandler(this);
        view.addTeamFilterSelectionChangedHandler(this);
        view.addCreateTeamSelectedHandler(this);
        view.addEditTeamSelectedHandler(this);
        view.addLeaveTeamSelectedHandler(this);
        searchProxy.addTeamSearchResultLoadHandler(this);
        searchProxy.addTeamSearchResultLoadHandler(view);
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
            }
        });
    }

    @Override
    public void onEditTeamSelected(EditTeamSelected event) {
        Group group = event.getGroup();
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
            }
        });
    }

    @Override
    public void onLeaveTeamSelected(LeaveTeamSelected event) {
        leaveTeamDlgProvider.get(new AsyncCallback<LeaveTeamDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(LeaveTeamDialog dialog) {
                Group group = event.getGroup();
                dialog.show(group);
                dialog.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        Dialog.PredefinedButton hideButton = event.getHideButton();
                        if (Dialog.PredefinedButton.YES.equals(hideButton)) {
                            leaveTeam(group);
                        } else {
                            dialog.hide();
                        }
                    }
                });
            }
        });
    }

    void leaveTeam(Group team) {
        serviceFacade.leaveTeam(team, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                if (result != null && !result.isEmpty()) {
                    UpdateMemberResult updateMemberResult = result.get(0);
                    if (updateMemberResult.isSuccess()) {
                        announcer.schedule(new IplantAnnouncementConfig(appearance.leaveTeamSuccess(team)));
                        view.removeTeam(team);
                    } else {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.leaveTeamFail()));
                    }
                }
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
