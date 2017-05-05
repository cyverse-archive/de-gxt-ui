package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * The ManageCollaboratorsView is the user interface for searching and adding collaborators
 * to your list of all collaborators.
 *
 * @author sriram, jstroot
 * 
 */
public interface ManageCollaboratorsView extends IsWidget,
                                                 RemoveCollaboratorSelected.HasRemoveCollaboratorSelectedHandlers,
                                                 DeleteGroupSelected.HasDeleteGroupSelectedHandlers,
                                                 AddGroupSelected.HasAddGroupSelectedHandlers {

    /**
     * Appearance related items for the ManageCollaboratorsView
     */
    interface Appearance {

        SafeHtml renderCheckBoxColumnHeader(String debugId);

        String collaborators();

        String collaboratorsHelp();

        String manageGroups();

        String delete();

        ImageResource deleteIcon();

        String manageCollaborators();

        ImageResource shareIcon();

        String noCollaborators();

        String myCollaborators();

        String selectCollabs();

        ImageResource groupsIcon();

        String collaboratorTab();

        String collaboratorListTab();

        String loadingMask();
    }

    /**
     * Presenter for the ManageCollaboratorsView
     */
    public interface Presenter {

        /**
         * Method used to initialize the presenter
         * @param container The UI container the presenter will put its view into
         * @param mode The mode (manage or select) to put the ManageCollaboratorsView into
         */
        void go (HasOneWidget container, MODE mode);

        /**
         * Add collaborators to the view
         * @param models
         */
        void addAsCollaborators(List<Collaborator> models);

        /**
         * Run a search for Collaborator Lists with the given search term
         * @param searchTerm
         */
        void updateListView(String searchTerm);

        /**
         * Fetch the list of current collaborators
         */
        void loadCurrentCollaborators();

        /**
         * Set the mode (manage or select) to put the ManageCollaboratorsView into
         * @param mode
         */
        void setCurrentMode(MODE mode);

        /**
         * Getter for the ManageCollaboratorsView mode
         * @return
         */
        MODE getCurrentMode();

        /**
         * Returns the list of currently selected collaborators from the ManageCollaboratorsView
         * @return
         */
        List<Collaborator> getSelectedCollaborators();

        /**
         * Clean up any dangling event handlers
         */
        void cleanup();
    }

    /**
     * Add a set of Collaborator Lists to the GroupView
     * @param result
     */
    void addCollabLists(List<Group> result);

    /**
     * Remove a set of Collaborator Lists from the GroupView
     * @param result
     */
    void removeCollabList(Group result);

    /**
     *  The collection of modes the ManageCollaboratorsView can step into
     */
    enum MODE {
        MANAGE, SELECT
    }

    /**
     * Set the list of collaborators in the ManageCollaboratorsView
     * @param models
     */
    void loadData(List<Collaborator> models);

    /**
     * Remove the list of collaborators from the ManageCollaboratorsView
     * @param models
     */
    void removeCollaborators(List<Collaborator> models);

    /**
     * Mask both the ManageCollaboratorsView and GroupView
     * @param maskText
     */
    void mask(String maskText);

    /**
     * Unmask both the ManageCollaboratorsView and GroupView
     */
    void unmask();

    /**
     * Set the mode for the ManageCollaboratorsView
     * @param mode
     */
    void setMode(MODE mode);

    /**
     * Returns the list of currently selected collaborators from the ManageCollaboratorsView
     * @return
     */
    List<Collaborator> getSelectedCollaborators();

    /**
     * Returns the mode the ManageCollaboratorsView is currently set to
     * @return
     */
    MODE getMode();

    /**
     * Add to the list of collaborators in the ManageCollaboratorsView
     * @param models
     */
    void addCollaborators(List<Collaborator> models);

    /**
     * Get the list of collaborators from the ManageCollaboratorsView
     * @return
     */
    List<Collaborator> getCollaborators();
}
