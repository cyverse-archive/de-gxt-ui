package org.iplantc.de.admin.desktop.client.communities;

import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityAdminSelected;
import org.iplantc.de.admin.desktop.client.communities.events.RemoveCommunityAdminSelected;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author aramsey
 *
 * An interface for DE and community admins to edit and manage communities
 */
public interface ManageCommunitiesView extends IsWidget,
                                               IsMaskable,
                                               AddCommunityAdminSelected.HasAddCommunityAdminSelectedHandlers,
                                               RemoveCommunityAdminSelected.HasRemoveCommunityAdminSelectedHandlers {

    enum MODE {
        CREATE,
        EDIT
    }

    interface Appearance {

        String communityNameLabel();

        String communityDescLabel();

        String adminsSectionHeader();

        String adminPrivilegesExplanation();

        ImageResource deleteIcon();

        String loadingMask();

        String failedToAddCommunityAdmin(Subject admin, Group community);

        String add();

        ImageResource addButton();

        String remove();

        String failedToRemoveCommunityAdmin(Subject admin, Group community);
    }

    interface Presenter extends AddCommunityAdminSelected.AddCommunityAdminSelectedHandler,
                                RemoveCommunityAdminSelected.RemoveCommunityAdminSelectedHandler {

        /**
         * Initializes the view with a mode and a container to hold the view
         * @param widget
         * @param mode
         */
        void go(HasOneWidget widget, MODE mode);

        /**
         * Initializes the {@link org.iplantc.de.teams.client.views.TeamDetailsView.EditorDriver}
         * for editing the provided community
         * @param community
         */
        void editCommunity(Group community);

        /**
         * @return true if the view's fields are all valid
         */
        boolean isViewValid();

        /**
         * @return the current community with any modifications from the user
         */
        Group getUpdatedCommunity();

        /**
         * @return the list of collaborators selected to be admins to this community
         */
        List<Subject> getCommunityAdmins();

        /**
         * Sets the view's debug ID
         * @param baseID
         */
        void setViewDebugId(String baseID);
    }

    /**
     * Initializes the {@link org.iplantc.de.teams.client.views.TeamDetailsView.EditorDriver}
     * for editing the provided community
     * @param community
     */
    void edit(Group community);

    /**
     * Add the provided collaborators to the view's list of admins
     * @param communityAdmins
     */
    void addAdmins(List<Subject> communityAdmins);

    /**
     * @return true if the view's fields are all valid
     */
    boolean isValid();

    /**
     * @return the current community with any modifications from the user
     */
    Group getUpdatedCommunity();

    /**
     * Remove the specified collaborator from the view's list of admins
     * @param admin
     */
    void removeAdmin(Subject admin);

    /**
     * @return the list of collaborators selected to be admins to this community
     */
    List<Subject> getAdmins();
}
