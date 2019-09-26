package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.shared.FastMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The presenter to handle all the logic for the Teams view
 *
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;
    private CollaboratorsServiceFacade collaboratorsFacade;
    private GroupAutoBeanFactory factory;
    private CollaboratorsUtil collaboratorsUtil;
    private List<Group> selectedTeams;

    @Inject AsyncProviderWrapper<EditTeamDialog> editTeamDlgProvider;
    @Inject IplantAnnouncer announcer;
    TeamsFilter currentFilter;

    @Inject
    public TeamsPresenterImpl(TeamsView.TeamsViewAppearance appearance,
                              GroupServiceFacade serviceFacade,
                              CollaboratorsServiceFacade collaboratorsFacade,
                              TeamsViewFactory viewFactory,
                              GroupAutoBeanFactory factory,
                              CollaboratorsUtil collaboratorsUtil) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.collaboratorsFacade = collaboratorsFacade;
        this.factory = factory;
        this.collaboratorsUtil = collaboratorsUtil;
        this.view = viewFactory.create(getBaseTeamProps());
    }

    @Override
    public TeamsView getView() {
        return view;
    }

    @Override
    public void showCheckBoxes() {
        view.showCheckBoxes();
    }

    @Override
    public List<Group> getSelectedTeams() {
        return selectedTeams;
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.setBaseId(baseID + Teams.Ids.VIEW);
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onTeamNameSelected(Splittable teamSpl) {
        Group group = convertSplittableToGroup(teamSpl);
        editTeamDlgProvider.get(new AsyncCallback<EditTeamDialog>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(EditTeamDialog dialog) {
                dialog.show(group);
                dialog.addTeamSavedHandler(event1 -> {
                    refreshTeamListing();
                });
                dialog.addLeaveTeamCompletedHandler(event -> {
                    refreshTeamListing();
                });
                dialog.addDeleteTeamCompletedHandler(event -> {
                    refreshTeamListing();
                });
                dialog.addJoinTeamCompletedHandler(event -> {
                    refreshTeamListing();
                });
            }
        });
    }

    void refreshTeamListing() {
        if (TeamsFilter.MY_TEAMS.equals(currentFilter)) {
            getMyTeams();
        } else {
            getAllTeams();
        }
    }

    @Override
    public void getMyTeams() {
        currentFilter = TeamsFilter.MY_TEAMS;
        view.mask();

        serviceFacade.getMyTeams(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable result) {
                view.setTeamList(result);
//                getTeamCreatorNames(result);
            }
        });
    }

    @Override
    public void getAllTeams() {
        currentFilter = TeamsFilter.ALL;
        view.mask();

        serviceFacade.getTeams(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable result) {
                view.setTeamList(result);
//                getTeamCreatorNames(result);
            }
        });
    }

    @Override
    public void searchTeams(String searchTerm) {
        view.mask();
        serviceFacade.searchTeams(searchTerm, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                announcer.schedule(new ErrorAnnouncementConfig(appearance.teamSearchFailed()));
            }

            @Override
            public void onSuccess(Splittable result) {
                view.setTeamList(result);
//                getTeamCreatorNames(result);
            }
        });
    }

    @Override
    public void getTeamCreatorNames(String[] subjectIds, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        if (subjectIds != null) {
            collaboratorsFacade.getUserInfo(subjectIds, new AsyncCallback<Splittable>() {
                @Override
                public void onFailure(Throwable throwable) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.getCreatorNamesFailed()));
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
                }

                @Override
                public void onSuccess(Splittable creators) {
                    callback.onSuccess(creators);
                }
            });
        } else {
            callback.onSuccess(null);
        }
    }

    void addTeamsToView(List<Group> teams) {
        Splittable teamListSpl = convertGroupListToSplittable(teams);
        view.setTeamList(teamListSpl);
        view.unmask();
    }

    @Override
    public void onCreateTeamSelected() {
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
                        refreshTeamListing();
                    }
                });
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onTeamSelectionChanged(Splittable teamList) {
        view.setSelectedTeams(teamList);
        this.selectedTeams = convertSplittableToGroupList(teamList);
    }

    void addCreatorToTeams(List<Group> teams, FastMap<Subject> creatorFastMap) {
        teams.forEach(group -> {
            String creatorId = getCreatorId(group);
            if (!Strings.isNullOrEmpty(creatorId)) {
                group.setCreator(creatorFastMap.get(creatorId).getSubjectDisplayName());
            }
        });
    }

    List<String> getCreatorIds(List<Group> teams) {
        return teams.stream().map(this::getCreatorId).distinct().collect(Collectors.toList());
    }

    /**
     * A team name is always in the format of {creator_id}:{team_name}
     *
     * @param team
     * @return
     */
    String getCreatorId(Group team) {
        String teamName = team.getName();

        int lastIndex = teamName.lastIndexOf(Subject.GROUP_NAME_DELIMITER);
        if (lastIndex > 0) {
            return teamName.substring(0, lastIndex);
        }
        return null;
    }

    ReactTeamViews.TeamProps getBaseTeamProps(){
        ReactTeamViews.TeamProps props = new ReactTeamViews.TeamProps();
        props.presenter = this;
        props.loading = true;
        props.teamListing = StringQuoter.createIndexed();
        props.collaboratorsUtil = collaboratorsUtil;
        props.isSelectable = false;
        props.selectedTeams = StringQuoter.createIndexed();

        return props;
    }

    Group convertSplittableToGroup(Splittable teamSpl) {
        return teamSpl != null ?
               AutoBeanCodex.decode(factory, Group.class, teamSpl.getPayload()).as() :
               null;
    }

    List<Group> convertSplittableToGroupList(Splittable groupSpl) {
        return groupSpl != null ?
               AutoBeanCodex.decode(factory, GroupList.class, groupSpl.getPayload()).as().getGroups() :
               Lists.newArrayList();
    }

    Splittable convertGroupToSplittable(Group group) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));
    }

    Splittable convertGroupListToSplittable(List<Group> teams) {
        Splittable list = StringQuoter.createIndexed();
        if (teams != null && teams.size() > 0) {
            teams.forEach(team -> {
                Splittable teamSpl = convertGroupToSplittable(team);
                teamSpl.assign(list, list.size());
            });
        }
        return list;
    }
}
