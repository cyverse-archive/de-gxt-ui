package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.DeleteMembersSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * The GroupDetailsView is used within the GroupDetailsDialog.  It allows users to
 * edit/create Collaborator Lists and add members to those lists.
 * @author aramsey
 */
public interface GroupDetailsView extends IsWidget,
                                          IsMaskable,
                                          AddGroupMemberSelected.HasAddGroupMemberSelectedHandlers,
                                          DeleteMembersSelected.HasDeleteMembersSelectedHandlers {

    enum MODE {
        ADD,
        EDIT
    }

    interface Presenter extends GroupSaved.HasGroupSavedHandlers,
                                AddGroupMemberSelected.AddGroupMemberSelectedHandler,
                                DeleteMembersSelected.DeleteMembersSelectedHandler {

        /**
         * Initialize the presenter and add the GroupDetailsView to the specified container
         * @param container
         */
        void go(HasOneWidget container, Subject group, MODE mode);

        /**
         * Check whether the GroupDetailsView is valid
         * @return
         */
        boolean isViewValid();

        /**
         * Handling saving the Collaborator List
         */
        void saveGroupSelected();

        void setViewDebugId(String debugId);
    }

    /**
     * Edit the specified Collaborator List
     * @param group
     */
    void edit(Group group, MODE mode);

    /**
     * Get the current Collaborator List from the view
     * @return
     */
    Group getGroup();

    /**
     * Check if the view's form has been filled out properly
     * @return
     */
    boolean isValid();

    /**
     * Get the list of members in the current Collaborator List
     * @return
     */
    List<Subject> getMembers();

    /**
     * Add the specified members to the Collaborator List
     * @param members
     */
    void addMembers(List<Subject> members);

    /**
     * Delete the specified members from the Collaborator List
     * @param members
     */
    void deleteMembers(List<Subject> members);
}
