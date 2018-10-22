package org.iplantc.de.admin.desktop.client.communities;

import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
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

import java.util.List;

/**
 * @author aramsey
 */
public interface AdminCommunitiesView extends IsWidget,
                                              HasHandlers,
                                              AppSelectionChangedEvent.AppSelectionChangedEventHandler,
                                              CommunitySelectionChanged.HasCommunitySelectionChangedHandlers,
                                              HierarchySelectedEvent.HasHierarchySelectedEventHandlers,
                                              BeforeAppSearchEvent.HasBeforeAppSearchEventHandlers,
                                              AppSearchResultLoadEvent.HasAppSearchResultLoadEventHandlers,
                                              AddCommunityClicked.HasAddCommunityClickedHandlers,
                                              EditCommunityClicked.HasEditCommunityClickedHandlers {

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
    }

    interface Presenter extends CommunitySelectionChanged.CommunitySelectionChangedHandler,
                                HierarchySelectedEvent.HierarchySelectedEventHandler,
                                AddCommunityClicked.AddCommunityClickedHandler,
                                EditCommunityClicked.EditCommunityClickedHandler {
        void go(HasOneWidget container);

        AdminCommunitiesView getView();

        Group getCommunityFromElement(Element el);

        Group getSelectedCommunity();

        void setViewDebugId(String id);

        void appsDNDtoCommunity(List<App> apps, Group community);

        void communityDNDtoApp(Group community, App targetApp);
    }

    Group getCommunityFromElement(Element el);

    Group getSelectedCommunity();

    void showNoCommunitiesPanel();

    void showCommunitiesPanel();

    void deselectHierarchies();

    void selectCommunity(Group community);
}
