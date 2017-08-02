package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;

import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;
import javax.inject.Inject;

public class TeamSearchRpcProxy extends RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Group>> implements TeamSearchResultLoad.HasTeamSearchResultLoadHandlers {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade searchFacade;
    private IplantAnnouncer announcer;
    private HandlerManager handlerManager;

    @Inject
    TeamSearchRpcProxy(TeamsView.TeamsViewAppearance appearance,
                       GroupServiceFacade searchFacade,
                       IplantAnnouncer announcer) {
        this.appearance = appearance;
        this.searchFacade = searchFacade;
        this.announcer = announcer;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
                     AsyncCallback<PagingLoadResult<Group>> callback) {
        String lastQueryText = "";

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
            }

            @Override
            public void onSuccess(List<Group> result) {
                PagingLoadResultBean<Group> loadResult = new PagingLoadResultBean<>(result, result.size(), loadConfig.getOffset());
                ensureHandlers().fireEvent(new TeamSearchResultLoad(result));
                callback.onSuccess(loadResult);
            }
        });
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
