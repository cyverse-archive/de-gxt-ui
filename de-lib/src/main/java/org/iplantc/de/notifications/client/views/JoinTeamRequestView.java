package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.notifications.client.events.JoinTeamApproved;
import org.iplantc.de.notifications.client.events.JoinTeamDenied;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import jsinterop.annotations.JsType;

/**
 * A view interface for team admins to accept or reject join request from team non-members
 */
@JsType
public interface JoinTeamRequestView extends IsWidget,
                                             IsMaskable,
                                             JoinTeamApproved.HasJoinTeamApprovedHandlers,
                                             JoinTeamDenied.HasJoinTeamDeniedHandlers {

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

        String denyBtnText();

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

        String denyRequestHeader();

        String denyRequestLabel(String requesterName);

        String denyRequestMessage(String requesterName, String teamName);

        String denyRequestSuccess(String requesterName, String teamName);

        String denyDetailsHeader();

        String denyDetailsMessage(String teamName);

        String denyAdminLabel();
    }

    /**
     * This presenter is responsible for managing all the events from the JoinTeamRequestView
     */
    @JsType
    interface Presenter {

        /**
         * Method for starting up the view with the specified payload
         * @param container
         * @param requestDlg
         * @param message
         * @param payloadTeam
         */
        void go(HasOneWidget container, IsHideable requestDlg, NotificationMessage message, PayloadTeam payloadTeam);

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
