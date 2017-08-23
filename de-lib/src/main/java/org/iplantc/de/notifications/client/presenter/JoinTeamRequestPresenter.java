package org.iplantc.de.notifications.client.presenter;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.shared.Notifications;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class JoinTeamRequestPresenter implements JoinTeamRequestView.Presenter {

    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;
    private JoinTeamRequestView view;
    private IsHideable requestDlg;
    private PayloadTeam payloadTeam;

    @Inject
    public JoinTeamRequestPresenter(JoinTeamRequestView view,
                                    JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        this.view = view;
        this.appearance = appearance;
    }

    @Override
    public void go(HasOneWidget container, IsHideable requestDlg, PayloadTeam payloadTeam) {
        this.requestDlg = requestDlg;
        this.payloadTeam = payloadTeam;

        container.setWidget(view);
        view.edit(payloadTeam);
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.asWidget().ensureDebugId(baseID + Notifications.JoinRequestIds.JOIN_REQUEST_VIEW);
    }
}
