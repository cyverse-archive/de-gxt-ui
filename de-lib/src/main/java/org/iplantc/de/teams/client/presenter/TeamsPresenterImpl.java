package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.teams.client.TeamsView;

import com.google.inject.Inject;

/**
 * The presenter to handle all the logic for the Teams view
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter {

    private TeamsView.TeamsViewAppearance appearance;
    private GroupServiceFacade serviceFacade;

    @Inject
    public TeamsPresenterImpl(TeamsView.TeamsViewAppearance appearance,
                              GroupServiceFacade serviceFacade) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
    }
}
