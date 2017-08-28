package org.iplantc.de.notifications.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * A dialog that shows a user the details to the denial of their request to join a team
 */
public class DenyJoinRequestDetailsDialog extends IPlantDialog {

    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;

    @Inject
    public DenyJoinRequestDetailsDialog(JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        this.appearance = appearance;

        setWidth(appearance.privilegeDlgWidth());
        setPredefinedButtons(PredefinedButton.OK);
    }

    public void show(String teamName, String adminMessage) {
        setHeading(appearance.denyDetailsHeader());

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        HTML denyMessage = new HTML(appearance.denyDetailsMessage(teamName));
        Label denyFieldLabel = new Label(appearance.denyAdminLabel());
        Label adminMessageText = new Label(adminMessage);

        container.add(denyMessage);
        container.add(denyFieldLabel);
        container.add(adminMessageText);

        add(container);

        super.show();
        ensureDebugId(Notifications.JoinRequestIds.DENY_DETAILS_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + Notifications.JoinRequestIds.OK_BTN);
    }
}
