package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.notifications.client.events.JoinTeamApproved;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * A view interface for team admins to accept or reject join request from team non-members
 */
public interface JoinTeamRequestView extends IsWidget,
                                             IsMaskable,
                                             JoinTeamApproved.HasJoinTeamApprovedHandlers {

    /**
     * An appearance class for all string related items in the JoinTeamRequest view
     */
    interface JoinTeamRequestAppearance {

        String joinTeamRequestHeader();

        int joinTeamRequestWidth();

        String requesterNameLabel();

        String requesterEmailLabel();

        String requesterMessageLabel();

        String approveBtnText();

        String rejectBtnText();

        String teamLabel();

        String joinRequestIntro();

        int joinTeamRequestHeight();

        String buttonBarWidth();

        String setPrivilegesHeading();

        String setPrivilegesText(String requesterName, String teamName);

        int privilegeComboWidth();

        String addMemberFail(String requesterName, String teamName);

        String joinTeamSuccess(String requesterName, String teamName);

        int privilegeDlgWidth();
    }

    /**
     * This presenter is responsible for managing all the events from the JoinTeamRequestView
     */
    interface Presenter {

        /**
         * Method for starting up the view with the specified payload
         * @param container
         * @param requestDlg
         * @param payloadTeam
         */
        void go(HasOneWidget container, IsHideable requestDlg, PayloadTeam payloadTeam);

        /**
         * Sets the JoinTeamRequestView debug ID
         * @param baseID
         */
        void setViewDebugId(String baseID);
    }

    /**
     * Allow the EditTeamView to fill in the form with the specified payloadTeam details
     * @param payloadTeam
     */
    void edit(PayloadTeam payloadTeam);
}
