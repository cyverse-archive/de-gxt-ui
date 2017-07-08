package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.models.TeamsFilter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.TabItemConfig;

import java.util.List;

/**
 * The presenter to handle all the logic for the Teams view
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter,
                                           TeamInfoButtonSelected.TeamInfoButtonSelectedHandler,
                                           TeamFilterSelectionChanged.TeamFilterSelectionChangedHandler {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;
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
    }

    @Override
    public void go() {
        currentFilter = view.getCurrentFilter();
        getMyTeams();
    }

    @Override
    public void onTeamInfoButtonSelected(TeamInfoButtonSelected event) {

    }

    @Override
    public void onTeamFilterSelectionChanged(TeamFilterSelectionChanged event) {
        TeamsFilter filter = event.getFilter();

        if (currentFilter.equals(filter)) {
            return;
        }

        currentFilter = filter;

        if (TeamsFilter.MY_TEAMS.equals(filter)) {
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
}
