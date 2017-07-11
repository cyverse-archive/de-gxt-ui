package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.views.TeamDetailsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.inject.Inject;

import java.util.List;

/**
 * A dialog for displaying the details of a Team
 * @author aramsey
 */
public class TeamDetailsDialog extends IPlantDialog {

    private TeamDetailsView view;
    private TeamsView.TeamsViewAppearance appearance;

    @Inject
    public TeamDetailsDialog(TeamDetailsView view,
                             TeamsView.TeamsViewAppearance appearance) {
        this.view = view;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.teamDetailsWidth(), appearance.teamDetailsHeight());
        setOnEsc(false);
        setHideOnButtonClick(true);

        add(view);
    }

    public void show(Group group, List<Subject> members) {
        view.addMembers(members);
        view.edit(group);
        setHeading(appearance.detailsHeading(group));
        super.show();

        ensureDebugId(Teams.Ids.TEAM_DETAILS_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        view.ensureDebugId(Teams.Ids.TEAM_DETAILS_DLG);
    }
}
