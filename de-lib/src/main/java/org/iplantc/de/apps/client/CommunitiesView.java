package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

/**
 * @author aramsey
 *
 * A view in the Apps window that allows users to browse the list of communities
 */
public interface CommunitiesView extends IsWidget,
                                         IsMaskable,
                                         CommunitySelectionChangedEvent.HasCommunitySelectionChangedEventHandlers {
    interface Appearance {

        void setTreeIcons(TreeStyle style);

        String communities();

        String loadingMask();
    }

    /**
     * Handles all the logic and events for the view
     */
    interface Presenter extends CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler,
                                CommunitySelectionChangedEvent.HasCommunitySelectionChangedEventHandlers {

        /**
         * @return the community the user currently has selected
         */
        Group getSelectedCommunity();

        /**
         * @return the view
         */
        CommunitiesView getView();

        /**
         * Initialize the view and populate it with the list of communities
         * @param selectedCommunity
         * @param appNavigationView
         */
        void go(HasId selectedCommunity,
                AppNavigationView appNavigationView);

        /**
         * Sets the base static ID for the view
         * @param baseID
         */
        void setViewDebugId(String baseID);
    }

    /**
     * This provides the "breadcrumbs" or path that will be displayed in the apps
     * grid header when a community is selected
     */
    interface AppCategoryHierarchyProvider {
        List<String> getGroupHierarchy(AppCategory appCategory);
    }

    /**
     * @return The tree that contains all the communities as nodes
     */
    Tree<Group, String> getTree();
}
