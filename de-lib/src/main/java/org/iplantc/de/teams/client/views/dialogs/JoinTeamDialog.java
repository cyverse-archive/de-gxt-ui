package org.iplantc.de.teams.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * A dialog that allows user to compose a short message to send to a team admin
 * to review their request to join their team
 */
public class JoinTeamDialog extends IPlantDialog {

    private TeamsView.TeamsViewAppearance appearance;
    private TextArea requestMessage;
    private FieldLabel requestMessageLabel;
    private HTML joinTeamText;

    @Inject
    public JoinTeamDialog(TeamsView.TeamsViewAppearance appearance) {
        this.appearance = appearance;
        joinTeamText = new HTML();
        requestMessageLabel = new FieldLabel();
        requestMessage = new TextArea();
        requestMessageLabel.setWidget(requestMessage);

        setWidth(appearance.joinTeamWidth());
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        getButton(PredefinedButton.OK).setText(appearance.sendRequestButton());

        VerticalLayoutContainer container = new VerticalLayoutContainer();

        container.add(joinTeamText);
        container.add(requestMessageLabel);

        add(container);

        ensureDebugId(Teams.Ids.JOIN_TEAM_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + Teams.Ids.SEND_REQUEST_BTN);
        getButton(PredefinedButton.CANCEL).asWidget().ensureDebugId(baseID + Teams.Ids.CANCEL_BTN);
        requestMessage.ensureDebugId(baseID + Teams.Ids.JOIN_REQUEST_MESSAGE);
    }

    public void show(Group team) {
        setHeading(appearance.joinTeamHeader(team));
        requestMessageLabel.setText(appearance.requestMessageLabel());
        joinTeamText.setHTML(appearance.joinTeamText(team));
        super.show();
    }

    public String getRequestMessage() {
        return requestMessage.getText();
    }
}
