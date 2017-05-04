package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.GroupSaved;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * The GroupDetailsView is used within the GroupDetailsDialog.  It allows users to
 * edit/create Collaborator Lists and add members to those lists.
 * @author aramsey
 */
public interface GroupDetailsView extends IsWidget {

    interface Presenter extends GroupSaved.HasGroupSavedHandlers {
    enum MODE {
        ADD,
        EDIT
    }

        /**
         * Initialize the presenter and add the GroupDetailsView to the specified container
         * @param container
         */
        void go(HasOneWidget container, Group group, MODE mode);

        /**
         * Check whether the GroupDetailsView is valid
         * @return
         */
        boolean isViewValid();

        /**
         * Clear any EventBus handlers that are no longer needed
         */
        void clearHandlers();

        /**
         * Handling saving the Collaborator List
         */
        void saveGroupSelected();
    }

    /**
     * Edit the specified Collaborator List
     * @param group
     */
    void edit(Group group, MODE mode);

    /**
     * Clear any EventBus handlers that can now be removed
     */
    void clearHandlers();

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
    List<Collaborator> getCollaborators();

    /**
     * Add the specified members to the Collaborator List
     * @param members
     */
    void addMembers(List<Collaborator> members);
}
