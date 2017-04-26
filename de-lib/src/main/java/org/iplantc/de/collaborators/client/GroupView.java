package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupView extends IsWidget,
                                   GroupNameSelected.GroupNameSelectedHandler,
                                   AddGroupSelected.HasAddGroupSelectedHandlers,
                                   DeleteGroupSelected.HasDeleteGroupSelectedHandlers {

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
    }

    void addCollabLists(List<Group> result);

    void mask(String loadingMask);

    void unmask();

    void removeCollabList(Group result);
}