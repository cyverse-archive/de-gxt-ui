package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.teams.client.events.AddPublicUserSelected;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.PrivilegeAndMembershipLoaded;
import org.iplantc.de.teams.client.events.RemoveMemberPrivilegeSelected;
import org.iplantc.de.teams.client.events.RemoveNonMemberPrivilegeSelected;
import org.iplantc.de.teams.client.events.TeamSaved;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * An interface for users to create and edit teams
 */
public interface EditTeamView extends IsWidget,
                                      IsMaskable,
                                      UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers,
                                      RemoveMemberPrivilegeSelected.HasRemoveMemberPrivilegeSelectedHandlers,
                                      RemoveNonMemberPrivilegeSelected.HasRemoveNonMemberPrivilegeSelectedHandlers,
                                      AddPublicUserSelected.HasAddPublicUserSelectedHandlers {

    enum MODE {
        CREATE,
        EDIT,
        VIEW
    }

    String SEARCH_MEMBERS_TAG = "members";
    String SEARCH_NON_MEMBERS_TAG = "nonMembers";

    interface Presenter extends TeamSaved.HasTeamSavedHandlers,
                                LeaveTeamCompleted.HasLeaveTeamCompletedHandlers,
                                PrivilegeAndMembershipLoaded.HasPrivilegeAndMembershipLoadedHandlers {

        /**
         * Initialize the presenter which creates the view
         * @param widget
         * @param group
         */
        void go(HasOneWidget widget, Group group);

        /**
         * Set the static ID for the view
         * @param debugId
         */
        void setViewDebugId(String debugId);

        /**
         * Returns true if the view has no validation errors
         * @return
         */
        boolean isViewValid();

        /**
         * This method is called when the user hits the OK button in the EditTeamDialog,
         * which indicates they are ready to save the team
         * @param hideable the EditTeamDialog
         */
        void saveTeamSelected(IsHideable hideable);

        /**
         * This method is called when the "Leave Team" button is selected
         * @param hideable
         */
        void onLeaveButtonSelected(IsHideable hideable);

        /**
         * This method is called when the "Delete Team" button is selected
         * @param hideable
         */
        void onDeleteButtonSelected(IsHideable hideable);
    }

    /**
     * Load up the details of the Team (group) into the view
     * @param group
     */
    void edit(Group group);

    /**
     * Add subjects and privileges to the non-members section
     * @param privilegeList
     */
    void addNonMembers(List<Privilege> privilegeList);

    /**
     * Add subjects and privileges to the members section
     * @param privilegeList
     */
    void addMembers(List<Privilege> privilegeList);

    /**
     * Returns true if the view has no validation errors (e.g. missing required information)
     * @return
     */
    boolean isValid();

    /**
     * Get the team name and description in Group form from the view
     * @return
     */
    Group getTeam();

    /**
     * Get the list of privileges being assigned to members
     * @return
     */
    List<Privilege> getMemberPrivileges();

    /**
     * Get the list of privileges being assigned to non-members
     * @return
     */
    List<Privilege> getNonMemberPrivileges();

    /**
     * Remove the specified privilege from the Members section
     * @param privilege
     */
    void removeMemberPrivilege(Privilege privilege);

    /**
     * Remove the specified privilege from the Non-Members section
     * @param privilege
     */
    void removeNonMemberPrivilege(Privilege privilege);

    /**
     * Show the "Add All Public Users" button in the UI if the public user is not currently present
     * @param isVisible
     */
    void setPublicUserButtonVisibility(boolean isVisible);

    /**
     * Enables aspects of the Edit Team view that are only applicable to those with admin privileges
     * @param adminMode
     */
    void showAdminMode(boolean adminMode);

}
