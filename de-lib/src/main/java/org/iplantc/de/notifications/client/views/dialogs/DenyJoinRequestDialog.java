package org.iplantc.de.notifications.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * A dialog that allows the user reviewing a join team request to deny the request
 */
public class DenyJoinRequestDialog extends IPlantDialog {

    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;
    private TextArea denyMessage;

    @Inject
    public DenyJoinRequestDialog(JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        this.appearance = appearance;

        setHeading(appearance.denyRequestHeader());
        setWidth(appearance.privilegeDlgWidth());
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
    }

    public void show(String requesterName, String teamName) {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        FieldLabel denyFieldLabel = new FieldLabel();
        Label denyText = new Label(appearance.denyRequestMessage(requesterName, teamName));
        denyMessage = new TextArea();

        denyFieldLabel.setText(appearance.denyRequestLabel(requesterName));
        denyFieldLabel.setWidget(denyMessage);
        container.add(denyText);
        container.add(denyFieldLabel);

        add(container);

        super.show();
        ensureDebugId(Notifications.JoinRequestIds.DENY_REQUEST_DLG);
    }

    public String getDenyMessage() {
        return denyMessage.getCurrentValue();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + Notifications.JoinRequestIds.OK_BTN);
        getButton(PredefinedButton.CANCEL).ensureDebugId(baseID + Notifications.JoinRequestIds.CANCEL_BTN);
    }
}
