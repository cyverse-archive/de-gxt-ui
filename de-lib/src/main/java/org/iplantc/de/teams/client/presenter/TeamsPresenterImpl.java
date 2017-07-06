package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;

import com.google.inject.Inject;

/**
 * The presenter to handle all the logic for the Teams view
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter,
                                           TeamInfoButtonSelected.TeamInfoButtonSelectedHandler {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;
    private TeamsView view;

    @Inject
    public TeamsPresenterImpl(TeamsView.TeamsViewAppearance appearance,
                              GroupServiceFacade serviceFacade,
                              TeamsView view) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.view = view;
        
        view.addTeamInfoButtonSelectedHandler(this);
    }

    @Override
    public void onTeamInfoButtonSelected(TeamInfoButtonSelected event) {

    }
}
