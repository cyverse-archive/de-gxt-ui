package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.DeleteTeamCompleted;
import org.iplantc.de.teams.client.events.JoinTeamCompleted;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.PrivilegeAndMembershipLoaded;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.shared.Teams;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;

/**
 * The main dialog that presents the form to users for creating/editing a team
 */
public class EditTeamDialog extends IPlantDialog implements TeamSaved.HasTeamSavedHandlers,
                                                            LeaveTeamCompleted.HasLeaveTeamCompletedHandlers,
                                                            PrivilegeAndMembershipLoaded.PrivilegeAndMembershipLoadedHandler,
                                                            DeleteTeamCompleted.HasDeleteTeamCompletedHandlers,
                                                            JoinTeamCompleted.HasJoinTeamCompletedHandlers {
    private EditTeamView.Presenter presenter;
    private TeamsView.TeamsViewAppearance appearance;
    private TextButton leaveBtn;
    private TextButton deleteBtn;
    private TextButton joinBtn;
    private TextButton requestToJoinBtn;

    @Inject
    public EditTeamDialog(EditTeamView.Presenter presenter,
                          TeamsView.TeamsViewAppearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.editTeamWidth(), appearance.editTeamHeight());
        setMinWidth(appearance.editTeamWidth());
        setOnEsc(false);
        setHideOnButtonClick(false);
        setButtons();
        setHandlers();
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

    void setButtons() {
        leaveBtn = new TextButton(appearance.leaveTeam());
        deleteBtn = new TextButton(appearance.deleteTeam());
        joinBtn = new TextButton(appearance.joinTeam());
        requestToJoinBtn = new TextButton(appearance.requestToJoinTeam());

        leaveBtn.setVisible(false);
        deleteBtn.setVisible(false);
        joinBtn.setVisible(false);
        requestToJoinBtn.setVisible(false);

        buttonBar.setPack(BoxLayoutContainer.BoxLayoutPack.START);
        addButton(leaveBtn);
        addButton(deleteBtn);
        addButton(joinBtn);
        addButton(requestToJoinBtn);
        addButton(new FillToolItem());
        addButton(getButton(PredefinedButton.OK));
        addButton(getButton(PredefinedButton.CANCEL));
        buttonBar.forceLayout();
    }

    void setHandlers() {
        presenter.addPrivilegeAndMembershipLoadedHandler(this);
        leaveBtn.addSelectHandler(event -> presenter.onLeaveButtonSelected(this));
        deleteBtn.addSelectHandler(event -> presenter.onDeleteButtonSelected(this));
        joinBtn.addSelectHandler(event -> presenter.onJoinButtonSelected(this));

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

    @Override
    public void onPrivilegeAndMembershipLoaded(PrivilegeAndMembershipLoaded event) {
        boolean isAdmin = event.isAdmin();
        boolean isMember = event.isMember();
        deleteBtn.setVisible(isAdmin);
        leaveBtn.setVisible(isMember);
        requestToJoinBtn.setVisible(!isAdmin && !isMember);
        setHeight(appearance.editTeamAdjustedHeight(isAdmin, isMember));

        buttonBar.forceLayout();
    }

    @Override
    public HandlerRegistration addTeamSavedHandler(TeamSaved.TeamSavedHandler handler) {
        return presenter.addTeamSavedHandler(handler);
    }

    @Override
    public HandlerRegistration addLeaveTeamCompletedHandler(LeaveTeamCompleted.LeaveTeamCompletedHandler handler) {
        return presenter.addLeaveTeamCompletedHandler(handler);
    }

    @Override
    public HandlerRegistration addDeleteTeamCompletedHandler(DeleteTeamCompleted.DeleteTeamCompletedHandler handler) {
        return presenter.addDeleteTeamCompletedHandler(handler);
    }

    @Override
    public HandlerRegistration addJoinTeamCompletedHandler(JoinTeamCompleted.JoinTeamCompletedHandler handler) {
        return presenter.addJoinTeamCompletedHandler(handler);
    }
}
