package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.dom.client.Element;
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
                                                 AddGroupSelected.HasAddGroupSelectedHandlers,
                                                 GroupNameSelected.HasGroupNameSelectedHandlers,
                                                 UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers {
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

        String addGroup();

        ImageResource addIcon();

        SafeHtml groupNameLabel();

        String groupDescriptionLabel();

        int groupDetailsWidth();

        int groupDetailsHeight();

        String groupDetailsHeading(Subject subject);

        String completeRequiredFieldsError();

        String deleteGroupConfirmHeading(List<Subject> groups);

        String deleteGroupConfirm(List<Subject> groups);

        String unableToAddMembers(List<UpdateMemberResult> failures);

        String loadingMask();

        String groupCreatedSuccess(Group group);

        String memberDeleteFail(List<UpdateMemberResult> subject);

        String collaboratorsSelfAdd();

        String groupSelfAdd();

        String collaboratorRemoveConfirm(String names);

        String collaboratorAddConfirm(String names);

        String addCollabErrorMsg();

        String memberAddToGroupsSuccess(Subject subject);

        String groupNameValidationMsg(String restrictedChars);

        String invalidChars(String restrictedChar);

        String nameHeader();

        String institutionOrDescriptionHeader();

        String onlyDNDToListSupported();

        String membersAddedToGroupSuccess(Subject group, List<UpdateMemberResult> userSuccesses);

        String windowHeading();

        String windowWidth();

        String windowHeight();

        int windowMinWidth();

        String retainPermissionsHeader();

        String retainPermissionsMessage();

        String retainPermissionsBtn();

        String removePermissionsBtn();

        int retainPermissionsWidth();
    }

    /**
     * Presenter for the ManageCollaboratorsView
     */
    public interface Presenter {

        /**
         * Method used to initialize the presenter
         * @param container The UI container the presenter will put its view into
         */
        void go (HasOneWidget container);

        /**
         * Add collaborators to the view
         * @param models
         */
        void addAsCollaborators(List<Subject> models);

        /**
         * Fetch the list of all Collaborator Lists for this user
         */
        void getGroups();

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
        List<Subject> getSelectedSubjects();

        /**
         * Returns the view
         * @return
         */
        ManageCollaboratorsView getView();
    }

    /**
     * Update an existing Collaborator List in the view
     * @param group
     */
    void updateCollabList(Subject group);

    /**
     * Remove the collaborators with the specified IDs
     * @param userIds
     */
    void removeCollaboratorsById(List<String> userIds);

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
    void loadData(List<Subject> models);

    /**
     * Remove the list of collaborators from the ManageCollaboratorsView
     * @param models
     */
    void removeCollaborators(List<Subject> models);

    /**
     * Mask only the ManageCollaboratorsView
     * @param maskText
     */
    void maskCollaborators(String maskText);

    /**
     * Unmask the ManageCollaboratorsView
     */
    void unmaskCollaborators();

    /**
     * Set the mode for the ManageCollaboratorsView
     * @param mode
     */
    void setMode(MODE mode);

    /**
     * Returns the list of currently selected collaborators from the ManageCollaboratorsView
     * @return
     */
    List<Subject> getSelectedSubjects();

    /**
     * Returns the mode the ManageCollaboratorsView is currently set to
     * @return
     */
    MODE getMode();

    /**
     * Add to the list of collaborators in the ManageCollaboratorsView
     * @param models
     */
    void addCollaborators(List<Subject> models);

    /**
     * Get the list of collaborators from the ManageCollaboratorsView
     * @return
     */
    List<Subject> getCollaborators();

    /**
     * Return the corresponding Subject in the grid for the given element
     * @param as
     * @return
     */
    Subject getSubjectFromElement(Element as);
}
