package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.teams.client.events.DeleteTeamCompleted;
import org.iplantc.de.teams.client.events.JoinTeamCompleted;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.TeamSaved;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * A dialog interface for users to create and edit teams
 */
@JsType
public interface EditTeamView extends IsWidget {

    @JsType
    interface Presenter extends TeamSaved.HasTeamSavedHandlers,
                                LeaveTeamCompleted.HasLeaveTeamCompletedHandlers,
                                DeleteTeamCompleted.HasDeleteTeamCompletedHandlers,
                                JoinTeamCompleted.HasJoinTeamCompletedHandlers {

        /**
         * Initialize the presenter which creates the view
         * @param team - parameter that should be included if a team is being edited,
         *             null if a team is being created
         */
        @JsIgnore
        void go(Group team);

        /**
         * Close the Edit Team Dialog in React
         */
        void closeEditTeamDlg();

        /**
         * Update an already existing team's name or description
         * @param originalName - the team's name in the format creatorName:teamName
         * @param name
         * @param description
         */
        void updateTeam(String originalName, String name, String description);

        /**
         * Search for collaborators/subjects based on a search term
         * @param searchTerm
         * @param callback
         * @param errorCallback
         */
        void searchCollaborators(String searchTerm, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        /**
         * Save a new team
         * @param name - the team's name in the format creatorName:teamName
         * @param createTeamRequest - same format as {@link org.iplantc.de.client.models.groups.CreateTeamRequest}
         * @param updatePrivilegeReq - same format as {@link org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList}
         * @param memberIds - String array containing the user IDs of the subjects to be added as team members
         */
        @SuppressWarnings("unusable-by-js")
        void saveTeamSelected(String name,
                              Splittable createTeamRequest,
                              Splittable updatePrivilegeReq,
                              String[] memberIds);

        /**
         * Update privileges to a team.  Optionally, if memberIds is provided, this will also add
         * those memberIDs as team members.
         * @param teamName - the team's name in the format creatorName:teamName
         * @param updatePrivilegeReq - same format as {@link org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList}
         * @param memberIds - String array containing the user IDs of the subjects to be added as team members
         * @param callback - Optional callback to EditTeamDialog
         */
        @SuppressWarnings("unusable-by-js")
        void updatePrivilegesToTeam(String teamName,
                                    Splittable updatePrivilegeReq,
                                    String[] memberIds,
                                    ReactSuccessCallback callback);

        /**
         * Delete a member of a team and also remove their privileges on that team
         * @param originalName - the team's name in the format creatorName:teamName
         * @param subjectId - the user ID of the subject being removed from the team
         * @param updatePrivilegeReq - same format as {@link org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList}
         * @param callback
         */
        @SuppressWarnings("unusable-by-js")
        void removeMemberAndPrivilege(String originalName,
                                      String subjectId,
                                      Splittable updatePrivilegeReq,
                                      ReactSuccessCallback callback);

        /**
         * Remove the current user's membership from a team
         * @param teamName - the team's name in the format creatorName:teamName
         * @param callback
         */
        void leaveTeamSelected(String teamName, ReactSuccessCallback callback);

        /**
         * Delete the team
         * @param teamName - the team's name in the format creatorName:teamName
         * @param callback
         */
        void deleteTeamSelected(String teamName, ReactSuccessCallback callback);

        /**
         * Attempt to have the current user join a team.
         * This should be successful if the team admin granted OPTIN privileges to either the public
         * user or the current user.
         * This will fail otherwise and prompt the user with a dialog to send a request to join
         * the team to one of the team's admins
         * @param teamName - the team's name in the format creatorName:teamName
         * @param errorCallback
         */
        void joinTeamSelected(String teamName, ReactErrorCallback errorCallback);

        /**
         * Send a request to the team's admin to allow the current user to join the team
         * @param teamName - the team's name in the format creatorName:teamName
         * @param hasMessage - same format as {@link org.iplantc.de.client.models.HasMessage}, contains a
         *                   message that will be sent to the team's admin detailing why this user wants
         *                   to join this team
         * @param callback
         * @param errorCallback
         */
        @SuppressWarnings("unusable-by-js")
        void sendRequestToJoin(String teamName,
                               Splittable hasMessage,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback);

        /**
         * Set the static ID for the view
         * @param debugId
         */
        void setViewDebugId(String debugId);
    }

    /**
     * Load up the details of the Team (group) into the view
     */
    @JsIgnore
    void edit(Splittable team, Splittable privileges, Splittable members);

    /**
     * Update the `team` prop for the view
     * @param team
     */
    @JsIgnore
    void updateTeam(Splittable team);

    /**
     * Apply a loading mask to the view
     */
    void mask();

    /**
     * Remove a loading mask from the view
     */
    void unmask();

    /**
     * Close the Edit Team Dialog
     */
    void close();
}
