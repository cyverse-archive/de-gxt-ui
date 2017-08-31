package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

/**
 * A dialog that warns users trying to delete a team of the consequences of the operation
 * before executing the operation
 */
public class DeleteTeamDialog extends ConfirmMessageBox {

    private TeamsView.TeamsViewAppearance appearance;

    @Inject
    public DeleteTeamDialog(TeamsView.TeamsViewAppearance appearance) {
        super(SafeHtmlUtils.EMPTY_SAFE_HTML, appearance.deleteTeamWarning());
        this.appearance = appearance;

        setWidth(appearance.deleteTeamWidth());
        setPredefinedButtons(PredefinedButton.YES, PredefinedButton.CANCEL);
        getButton(PredefinedButton.YES).setText(appearance.deleteTeam());

        ensureDebugId(Teams.Ids.DELETE_TEAM_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.YES).asWidget().ensureDebugId(baseID + Teams.Ids.DELETE_TEAM_BTN);
        getButton(PredefinedButton.CANCEL).asWidget().ensureDebugId(baseID + Teams.Ids.CANCEL_BTN);
    }

    public void show(Group team) {
        setHeading(appearance.deleteTeamHeader(team));

        super.show();
    }
}
