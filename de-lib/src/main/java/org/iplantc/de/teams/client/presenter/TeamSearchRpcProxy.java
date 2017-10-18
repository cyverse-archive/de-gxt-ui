package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;

import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class TeamSearchRpcProxy extends RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Group>> implements TeamSearchResultLoad.HasTeamSearchResultLoadHandlers {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade searchFacade;
    private CollaboratorsServiceFacade collaboratorsFacade;
    private IplantAnnouncer announcer;
    HandlerManager handlerManager;
    String lastQueryText;

    @Inject
    TeamSearchRpcProxy(TeamsView.TeamsViewAppearance appearance,
                       GroupServiceFacade searchFacade,
                       CollaboratorsServiceFacade collaboratorsFacade,
                       IplantAnnouncer announcer) {
        this.appearance = appearance;
        this.searchFacade = searchFacade;
        this.collaboratorsFacade = collaboratorsFacade;
        this.announcer = announcer;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
                     AsyncCallback<PagingLoadResult<Group>> callback) {
        lastQueryText = "";

        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            lastQueryText = filterConfigs.get(0).getValue();
        }

        if (Strings.isNullOrEmpty(lastQueryText)) {
            PagingLoadResultBean<Group> noResults = new PagingLoadResultBean<>();
            callback.onSuccess(noResults);
            return;
        }

        searchFacade.searchTeams(lastQueryText, new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                announcer.schedule(new ErrorAnnouncementConfig(appearance.teamSearchFailed()));
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(List<Group> result) {
                getTeamCreatorNames(result, loadConfig, callback);
            }
        });
    }

    void getTeamCreatorNames(List<Group> teams, FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Group>> callback) {
        if (teams != null && !teams.isEmpty()) {

            List<String> subjectIds = getCreatorIds(teams);
            collaboratorsFacade.getUserInfo(subjectIds, new AsyncCallback<FastMap<Subject>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.getCreatorNamesFailed()));
                    sendResults(teams, loadConfig, callback);
                }

                @Override
                public void onSuccess(FastMap<Subject> creatorFastMap) {
                    addCreatorToTeams(teams, creatorFastMap);
                    sendResults(teams, loadConfig, callback);
                }
            });
        } else {
            sendResults(teams, loadConfig, callback);
        }
    }

    void sendResults(List<Group> teams, FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Group>> callback) {
        PagingLoadResultBean<Group> loadResult = getLoadResult(teams, loadConfig);
        ensureHandlers().fireEvent(new TeamSearchResultLoad(teams));
        callback.onSuccess(loadResult);
    }

    PagingLoadResultBean<Group> getLoadResult(List<Group> result, FilterPagingLoadConfig loadConfig) {
        return new PagingLoadResultBean<>(result, result.size(), loadConfig.getOffset());
    }

    List<String> getCreatorIds(List<Group> teams) {
        return teams.stream().map(this::getCreatorId).distinct().collect(Collectors.toList());
    }

    void addCreatorToTeams(List<Group> teams, FastMap<Subject> creatorFastMap) {
        teams.forEach(group -> group.setCreator(creatorFastMap.get(getCreatorId(group)).getSubjectDisplayName()));
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

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }

    @Override
    public HandlerRegistration addTeamSearchResultLoadHandler(TeamSearchResultLoad.TeamSearchResultLoadHandler handler) {
        return ensureHandlers().addHandler(TeamSearchResultLoad.TYPE, handler);
    }
}
