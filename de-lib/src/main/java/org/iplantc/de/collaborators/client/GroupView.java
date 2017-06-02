package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * The GroupView is used within the Collaborators window as a way to provide users
 * the ability to manage and create custom, private lists of collaborators so as to easily
 * share data to collaborators in bulk.
 *
 * @author aramsey
 */
public interface GroupView extends IsWidget,
                                   DeleteGroupSelected.HasDeleteGroupSelectedHandlers,
                                   AddGroupSelected.HasAddGroupSelectedHandlers,
                                   GroupNameSelected.HasGroupNameSelectedHandlers {

    /**
     * Appearance related items for the GroupView
     */
    interface GroupViewAppearance {

        String addGroup();

        ImageResource addIcon();

        String deleteGroup();

        ImageResource deleteIcon();

        int nameColumnWidth();

        String nameColumnLabel();

        int descriptionColumnWidth();

        String descriptionColumnLabel();

        String noCollabLists();

        SafeHtml groupNameLabel();

        String groupDescriptionLabel();

        String delete();

        String noCollaborators();

        int groupDetailsWidth();

        int groupDetailsHeight();

        String groupDetailsHeading(Group group);

        String completeRequiredFieldsError();

        String deleteGroupConfirmHeading(Group group);

        String deleteGroupConfirm(Group group);

        String groupDeleteSuccess(Group group);

        String unableToAddMembers(List<UpdateMemberResult> failures);

        String loadingMask();

        String groupCreatedSuccess(Group group);

        String memberDeleteFail(List<UpdateMemberResult> subject);
    }

    /**
     * Add Collaborator Lists to the GroupView
     * @param result
     */
    void addCollabLists(List<Group> result);

    /**
     * Mask the GroupView with the specified loading mask text
     * @param loadingMask
     */
    void mask(String loadingMask);

    /**
     * Unmask the GroupView
     */
    void unmask();

    /**
     * Remove a Collaborator List from the GroupView
     * @param result
     */
    void removeCollabList(Group result);

    /**
     * Update an existing Collaborator List with the specified information
     * @param group
     */
    void updateCollabList(Group group);

    /**
     * Set the mode to match the ManageCollaboratorsView mode
     * @param mode
     */
    void setMode(ManageCollaboratorsView.MODE mode);

    /**
     * Return the list of currently selected Collaborator Lists
     * @return
     */
    List<Group> getSelectedCollabLists();
}
