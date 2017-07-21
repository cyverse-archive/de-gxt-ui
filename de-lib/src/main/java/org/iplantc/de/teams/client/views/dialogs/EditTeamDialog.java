package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class EditTeamDialog extends IPlantDialog {
    private EditTeamView.Presenter presenter;
    private TeamsView.TeamsViewAppearance appearance;

    @Inject
    public EditTeamDialog(EditTeamView.Presenter presenter,
                          TeamsView.TeamsViewAppearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.editTeamWidth(), appearance.editTeamHeight());
        setOnEsc(false);
        setHideOnButtonClick(false);
        addOkButtonSelectHandler(selectEvent -> {
            if (presenter.isViewValid()) {
                presenter.saveTeamSelected(this);
            } else {
                AlertMessageBox alertMsgBox =
                        new AlertMessageBox(appearance.completeRequiredFieldsHeading(), appearance.completeRequiredFieldsError());
                alertMsgBox.show();
            }
        });
        addCancelButtonSelectHandler(selectEvent -> hide());
    }

    public void show(Group group) {
        presenter.go(this, group);

        setHeading(appearance.editTeamHeading(group));
        super.show();

        ensureDebugId(Teams.Ids.EDIT_TEAM_DIALOG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(Teams.Ids.EDIT_TEAM_DIALOG);
    }
}
