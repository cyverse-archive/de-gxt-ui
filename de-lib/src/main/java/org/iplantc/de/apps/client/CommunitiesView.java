package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.widgets.DETabPanel;

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

    String COMMUNITIES_ROOT = "myCommunitiesRootNode";

    interface Appearance {

        void setTreeIcons(TreeStyle style);

        String communities();

        String loadingMask();

        String failedToLoadCommunities();
    }

    /**
     * Handles all the logic and events for the view
     */
    interface Presenter extends CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler,
                                CommunitySelectionChangedEvent.HasCommunitySelectionChangedEventHandlers,
                                AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler,
                                AppSearchResultLoadEvent.AppSearchResultLoadEventHandler {

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
         * @param deTabPanel
         */
        void go(HasId selectedCommunity,
                DETabPanel deTabPanel);

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
