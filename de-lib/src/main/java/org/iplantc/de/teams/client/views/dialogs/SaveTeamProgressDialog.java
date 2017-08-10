package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;

/**
 * A dialog that indicates to the user which step of the team creation/update process is
 * in progress
 */
public class SaveTeamProgressDialog extends ProgressMessageBox {

    private TeamsView.TeamsViewAppearance appearance;
    private double totalSteps;
    private int stepNumber;

    @Inject
    public SaveTeamProgressDialog(TeamsView.TeamsViewAppearance appearance) {
        super(appearance.saveTeamHeader());
        this.appearance = appearance;

        ensureDebugId(Teams.Ids.SAVE_PROGRESS_DLG);
    }

    public void startProgress(int totalSteps) {
        this.totalSteps = totalSteps;
        show();
        stepNumber = 0;
        updateProgress();
    }

    public void updateProgress() {
        String progressText;
        switch (stepNumber) {
            case 0:
                progressText = appearance.updatingTeamMsg();
                break;
            case 1:
                progressText = appearance.updatingPrivilegesMsg();
                break;
            case 2:
                progressText = appearance.updatingMembershipMsg();
                break;
            default:
                if (stepNumber == totalSteps) {
                    progressText = appearance.teamSuccessfullySaved();
                } else {
                    progressText = appearance.miscTeamUpdates();
                }
                break;
        }
        updateProgress( (stepNumber / totalSteps), progressText);
        stepNumber++;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + Teams.Ids.OK_BTN);
    }
}
