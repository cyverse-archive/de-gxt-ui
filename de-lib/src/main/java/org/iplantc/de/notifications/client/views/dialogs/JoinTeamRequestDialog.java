package org.iplantc.de.notifications.client.views.dialogs;

import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.inject.Inject;

/**
 * A dialog that allows the user to approve or deny a request to join a team
 */
public class JoinTeamRequestDialog extends IPlantDialog {

    private JoinTeamRequestView.Presenter presenter;
    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;

    @Inject
    public JoinTeamRequestDialog(JoinTeamRequestView.Presenter presenter,
                                 JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setHeading(appearance.joinTeamRequestHeader());
        setWidth(appearance.joinTeamRequestWidth());
        setHeight(appearance.joinTeamRequestHeight());
        setPredefinedButtons(PredefinedButton.CANCEL);

        ensureDebugId(Notifications.JoinRequestIds.JOIN_REQUEST_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.CANCEL).asWidget().ensureDebugId(baseID + Notifications.JoinRequestIds.CANCEL_BTN);
        presenter.setViewDebugId(baseID);
    }

    public void show(NotificationMessage message, PayloadTeam payloadTeam) {
        presenter.go(this, this, message, payloadTeam);
        super.show();
    }
}
