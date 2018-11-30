package org.iplantc.de.admin.desktop.client.communities;

import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CategorizeButtonClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.communities.events.DeleteCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.EditCommunityClicked;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

/**
 * @author aramsey
 *
 * A view that enables DE admins to manage communities in the DE and tag apps with communities
 */
public interface AdminCommunitiesView extends IsWidget,
                                              HasHandlers,
                                              AppSelectionChangedEvent.AppSelectionChangedEventHandler,
                                              CommunitySelectionChanged.HasCommunitySelectionChangedHandlers,
                                              HierarchySelectedEvent.HasHierarchySelectedEventHandlers,
                                              BeforeAppSearchEvent.HasBeforeAppSearchEventHandlers,
                                              AppSearchResultLoadEvent.HasAppSearchResultLoadEventHandlers,
                                              AddCommunityClicked.HasAddCommunityClickedHandlers,
                                              EditCommunityClicked.HasEditCommunityClickedHandlers,
                                              CategorizeButtonClicked.HasCategorizeButtonClickedHandlers,
                                              DeleteCommunityClicked.HasDeleteCommunityClickedHandlers {

    interface Appearance {

        String addCommunity();

        ImageResource addIcon();

        String deleteCommunity();

        ImageResource deleteIcon();

        String categorize();

        ImageResource blueFolder();

        String editCommunity();

        ImageResource editIcon();

        String emptySearchFieldText();

        String searchFieldWidth();

        String communityPanelHeader();

        String hierarchyPreviewHeader();

        String communityTreePanel();

        String hierarchyTreePanel();

        String loadingMask();

        String name();

        String description();

        String externalAppDND(String appLabels);

        String appAddedToCommunity(String appName, String communityName);

        void setTreeIcons(TreeStyle style);

        String clearCommunitySelection();

        String categorizeDialogWidth();

        String categorizeDialogHeight();

        String selectCommunitiesFor(App targetApp);

        String communityAvusSet(App app, List<Group> selectedCommunities);

        String confirmDeleteCommunityTitle();

        String confirmDeleteCommunityMessage(String communityName);

        String communityDeleted(Group community);

        String failedToAddCommunityAdmin(String adminName, Group community);

        String retagAppsConfirmationTitle();

        String retagAppsCommunityMessage(String name);
    }

    interface Presenter extends CommunitySelectionChanged.CommunitySelectionChangedHandler,
                                HierarchySelectedEvent.HierarchySelectedEventHandler,
                                AddCommunityClicked.AddCommunityClickedHandler,
                                EditCommunityClicked.EditCommunityClickedHandler,
                                CategorizeButtonClicked.CategorizeButtonClickedHandler,
                                DeleteCommunityClicked.DeleteCommunityClickedHandler {
        /**
         * Initializes the view and adds the view to the specified container
         * @param container
         */
        void go(HasOneWidget container);

        /**
         * @return the view
         */
        AdminCommunitiesView getView();

        /**
         * @param el
         * @return the community that is associated with the element
         */
        Group getCommunityFromElement(Element el);

        /**
         * @return the community that is currently selected
         */
        Group getSelectedCommunity();

        /**
         * Sets the base static ID for the view
         * @param id
         */
        void setViewDebugId(String id);

        /**
         * This method is called when a user drags and drops apps to a community in order
         * to tag those apps with that community
         * @param apps
         * @param community
         */
        void appsDNDtoCommunity(List<App> apps, Group community);

        /**
         * This method is called when a user drags and drops a community to an app in
         * order to tag the app with that community
         * @param community
         * @param targetApp
         */
        void communityDNDtoApp(Group community, App targetApp);
    }

    /**
     *
     * @param el
     * @return the community that is associated with the element
     */
    Group getCommunityFromElement(Element el);

    /**
     *
     * @return the currently selected community
     */
    Group getSelectedCommunity();

    /**
     * Display a panel indicating to the user that there are currently no communities
     */
    void showNoCommunitiesPanel();

    /**
     * Display a panel that will present the user with the communities list
     */
    void showCommunitiesPanel();

    /**
     * Deselects all hierarchies
     */
    void deselectHierarchies();

    /**
     * Selects the specified community in the view, typically to load the apps in that community
     * @param community
     */
    void selectCommunity(Group community);

    /**
     * Masks the communities tree, typically while data is loading
     */
    void maskCommunities();

    /**
     * Unmasks the communities tree
     */
    void unmaskCommunities();
}
