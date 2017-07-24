package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * An interface for users to create and edit teams
 */
public interface EditTeamView extends IsWidget,
                                      IsMaskable,
                                      UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers {

    enum MODE {
        CREATE,
        EDIT
    }

    String ALL_PUBLIC_USERS_NAME = "All Public Users";
    String ALL_PUBLIC_USERS_ID = "GrouperAll";
    String SEARCH_MEMBERS_TAG = "members";
    String SEARCH_NON_MEMBERS_TAG = "nonMembers";

    interface Presenter {

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

        void saveTeamSelected(IsHideable hideable);
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
     *
     * @return
     */
    boolean isValid();

    Group getTeam();

    List<Privilege> getMemberPrivileges();

    List<Privilege> getNonMemberPrivileges();
}
