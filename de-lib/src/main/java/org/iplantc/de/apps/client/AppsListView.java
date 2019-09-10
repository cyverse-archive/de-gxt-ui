package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.RunAppSelected;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.dnd.core.client.DragSource;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * @author aramsey
 */
@JsType
public interface AppsListView extends IsWidget/*   ,
                                      IsMaskable,
                                      AppSelectionChangedEvent.HasAppSelectionChangedEventHandlers,
                                      AppInfoSelectedEvent.HasAppInfoSelectedEventHandlers,
                                      AppNameSelectedEvent.HasAppNameSelectedEventHandlers,
                                      AppFavoriteSelectedEvent.HasAppFavoriteSelectedEventHandlers,
                                      AppCommentSelectedEvent.HasAppCommentSelectedEventHandlers,
                                      AppRatingSelected.HasAppRatingSelectedEventHandlers,
                                      AppRatingDeselected.HasAppRatingDeselectedHandlers,
                                      AppSearchResultLoadEvent.AppSearchResultLoadEventHandler,
                                      AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler,
                                      AppFavoritedEvent.HasAppFavoritedEventHandlers,
                                      BeforeAppSearchEvent.BeforeAppSearchEventHandler,
                                      OntologyHierarchySelectionChangedEvent.OntologyHierarchySelectionChangedEventHandler,
                                      CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler*/ {
    String GRID_VIEW = "grid";
    String TILE_VIEW = "tile";

    interface AppsListAppearance {

        String appLaunchWithoutToolError();

        String appRemoveFailure();

        String beforeAppSearchLoadingMask();

        String favServiceFailure();

        String getAppsLoadingMask();

        String integratedByColumnLabel();

        String nameColumnLabel();

        String ratingColumnLabel();

        String searchAppResultsHeader(String searchText, int total);

        String agaveAuthRequiredTitle();

        String agaveAuthRequiredMsg();

        String sortLabel();

        String appLoadError();

        String noApps();

        int executionSystemWidth();

        String executionSystemLabel();

        int menuColumnWidth();
    }

    /**
     * This presenter is responsible for updating/maintaining the {@code ListStore} associated with the
     * view. It fires store related events for other presenters. \
     *
     * To update the {@code ListStore}, it listens for {@link AppCategory} selection and search result
     * load events.
     */
    @JsType
    interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter,
                                AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler,
                                AppSearchResultLoadEvent.AppSearchResultLoadEventHandler,
                                DeleteAppsSelected.DeleteAppsSelectedHandler,
                                RunAppSelected.RunAppSelectedHandler,
                                BeforeAppSearchEvent.BeforeAppSearchEventHandler,
                                OntologyHierarchySelectionChangedEvent.OntologyHierarchySelectionChangedEventHandler,
                                SwapViewButtonClickedEvent.SwapViewButtonClickedEventHandler,
                                AppSelectionChangedEvent.HasAppSelectionChangedEventHandlers,
                                AppInfoSelectedEvent.HasAppInfoSelectedEventHandlers,
                                CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler {
        App getSelectedApp();

        @JsIgnore
        List<DragSource> getAppsDragSources();

        @JsIgnore
        void setViewDebugId(String baseID);

        @JsIgnore
        void loadApps(Splittable apps);

        @JsIgnore
        String getActiveView();

        @JsIgnore
        void setActiveView(String activeView);
    }

 /*   List<DragSource> getAppsDragSources();

    App getSelectedItem();

    void select(App app, boolean keepExisting);

    void deselectAll();

    void setSearchPattern(String searchPattern);

    void setHeading(String text);

    void setAppTypeFilter(AppTypeFilter filter);

    void enableAppTypeFilter(boolean enabled);*/
}
