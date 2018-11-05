package org.iplantc.de.apps.client;

import org.iplantc.de.admin.desktop.client.communities.events.RemoveCommunityAdminSelected;
import org.iplantc.de.apps.client.events.AddCommunityAdminSelected;
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

        void go(HasOneWidget widget, MODE mode);

        void editCommunity(Group community);

        boolean isViewValid();

        Group getUpdatedCommunity();

        List<Subject> getCommunityAdmins();
    }

    void edit(Group community);

    void addAdmins(List<Subject> communityAdmins);

    boolean isValid();

    Group getUpdatedCommunity();

    void removeAdmin(Subject admin);

    List<Subject> getAdmins();
}
