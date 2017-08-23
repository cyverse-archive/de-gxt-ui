package org.iplantc.de.theme.base.client.notifications;

import org.iplantc.de.notifications.client.views.JoinTeamRequestView;

import com.google.gwt.core.client.GWT;

public class JoinTeamRequestDefaultAppearance implements JoinTeamRequestView.JoinTeamRequestAppearance {

    private JoinTeamRequestDisplayStrings displayStrings;

    public JoinTeamRequestDefaultAppearance() {
        this(GWT.create(JoinTeamRequestDisplayStrings.class));
    }

    public JoinTeamRequestDefaultAppearance(JoinTeamRequestDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }

    @Override
    public String joinTeamRequestHeader() {
        return displayStrings.joinTeamRequestHeader();
    }

    @Override
    public int joinTeamRequestWidth() {
        return 400;
    }

    @Override
    public String requesterNameLabel() {
        return displayStrings.requesterNameLabel();
    }

    @Override
    public String requesterEmailLabel() {
        return displayStrings.requesterEmailLabel();
    }

    @Override
    public String requesterMessageLabel() {
        return displayStrings.requesterMessageLabel();
    }

    @Override
    public String approveBtnText() {
        return displayStrings.approveBtnText();
    }

    @Override
    public String rejectBtnText() {
        return displayStrings.rejectBtnText();
    }

    @Override
    public String teamLabel() {
        return displayStrings.teamLabel();
    }

    @Override
    public String joinRequestIntro() {
        return displayStrings.joinRequestIntro();
    }

    @Override
    public int joinTeamRequestHeight() {
        return 250;
    }

    @Override
    public String buttonBarWidth() {
        return joinTeamRequestWidth() + "px";
    }
}
