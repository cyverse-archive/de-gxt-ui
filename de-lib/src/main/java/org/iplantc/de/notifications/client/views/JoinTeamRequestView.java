package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

/**
 * A view interface for team admins to accept or reject join request from team non-members
 */
@JsType
public interface JoinTeamRequestView {

    /**
     * An appearance class for all string related items in the JoinTeamRequest view
     */
    interface JoinTeamRequestAppearance {
        String addMemberFail(String requesterName, String teamName);

        String joinTeamSuccess(String requesterName, String teamName);

        String denyRequestSuccess(String requesterName, String teamName);
    }

    /**
     * This presenter is responsible for managing all the events from the JoinTeamRequestView
     */
    @JsType
    interface Presenter {

        /**
         * Method for starting up the view with the specified payload
         * @param message
         * @param payloadTeam
         */
        void go(NotificationMessage message, PayloadTeam payloadTeam);

        /**
         * Approve and Add member with specified privilege
         *
         * @param privilegeType
         * @param callback
         * @param errorCallback
         */
        void addMemberWithPrivilege(String privilegeType,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback);

        /**
         * Deny member to join a team. Send Optional msg.
         *
         * @param denyMessage
         * @param callback
         * @param errorCallback
         */
        void denyRequest(String denyMessage,
                         ReactSuccessCallback callback,
                         ReactErrorCallback errorCallback);
    }

    /**
     * Allow the EditTeamView to fill in the form with the specified payloadTeam details
     * @param  presenter
     * @param payloadTeam
     */
    void edit(Presenter presenter, Splittable payloadTeam);
}
