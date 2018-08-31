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
    public String addMemberFail(String requesterName, String teamName) {
        return displayStrings.addMemberFail(requesterName, teamName);
    }

    @Override
    public String joinTeamSuccess(String requesterName, String teamName) {
        return displayStrings.joinTeamSuccess(requesterName, teamName);
    }


    @Override
    public String denyRequestSuccess(String requesterName, String teamName) {
        return displayStrings.denyRequestSuccess(requesterName, teamName);
    }

}

