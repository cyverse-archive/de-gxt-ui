package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author sriram, jstroot
 * 
 */
public interface ManageCollaboratorsView extends IsWidget,
                                                 RemoveCollaboratorSelected.HasRemoveCollaboratorSelectedHandlers{

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

    public interface Presenter {

        void go (HasOneWidget container, MODE mode);

        void addAsCollaborators(List<Collaborator> models);

        void updateListView(String searchTerm);

        void loadCurrentCollaborators();

        void setCurrentMode(MODE mode);

        MODE getCurrentMode();

        List<Collaborator> getSelectedCollaborators();

        void cleanup();
    }

    void addCollabLists(List<Group> result);

    enum MODE {
        MANAGE, SELECT
    }

    void loadData(List<Collaborator> models);

    void removeCollaborators(List<Collaborator> models);

    void mask(String maskText);

    void unmask();

    void setMode(MODE mode);

    List<Collaborator> getSelectedCollaborators();

    MODE getMode();

    void addCollaborators(List<Collaborator> models);
}
