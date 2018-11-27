package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * The primary view within the Collaboration window.  It is a composite of all other views within
 * the Collaboration window.
 */
public interface CollaborationView extends IsWidget,
                                           IsMaskable {

    enum TAB {
        Collaborators,
        Teams
    }

    /**
     * An appearance class that handles only the strings necessary for the CollaborationView.
     * Any sub-views have their own appearance classes.
     */
    interface CollaborationViewAppearance {

        String getCollaboratorsTabText();

        String getTeamsTabText();
    }

    /**
     * This presenter is responsible for starting up the presenters from the sub-components
     * within the Collaboration window
     */
    interface Presenter {

        /**
         * Initialize the sub-view presenters and add the overall view to the container,
         * most likely for a dialog, specifically the ChooseCollaboratorsDialog
         * @param container
         */
        void go(HasOneWidget container);

        /**
         * Set the debug id for the view
         * @param baseId
         */
        void setViewDebugId(String baseId);

        /**
         * Return all users and teams selected in the Collaboration View
         * @return
         */
        List<Subject> getSelectedCollaborators();
    }

    /**
     * Return the tab panel containing the sub-views
     * @return
     */
    DETabPanel getCollaborationTabPanel();

    /**
     * Set which tab in the view should be active/selected
     * @param selectedTab
     */
    void setActiveTab(TAB selectedTab);

}
