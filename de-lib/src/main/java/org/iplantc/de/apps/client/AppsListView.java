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

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * @author aramsey, sriram
 */
@JsType
public interface AppsListView extends IsWidget {
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
        @SuppressWarnings("unusable-by-js")
        App getSelectedApp();

        @JsIgnore
        String getActiveView();

        @JsIgnore
        void setActiveView(String activeView);

        @JsIgnore
        void loadApps(Splittable apps);

        @SuppressWarnings("unusable-by-js")
        void onTypeFilterChanged(String filter);

        @SuppressWarnings("unusable-by-js")
        void onAppSelectionChanged(Splittable selectedApps);

        @SuppressWarnings("unusable-by-js")
        void onAppNameSelected(Splittable appSplittable);

        @SuppressWarnings("unusable-by-js")
        void onAppInfoSelected(Splittable appSplittable);

        @SuppressWarnings("unusable-by-js")
        void onAppFavoriteSelected(Splittable appSplittable);

        @SuppressWarnings("unusable-by-js")
        void onAppCommentSelected(Splittable appSplittable);

        @SuppressWarnings("unusable-by-js")
        void onRequestSort(String sortField, String sortDir);

        @SuppressWarnings("unusable-by-js")
        void onAppRatingDeselected(final Splittable appSplittable);

        @SuppressWarnings("unusable-by-js")
        void onAppRatingSelected(final Splittable appSplittable,
                                 int score);
    }

    void load(AppsListView.Presenter presenter,
              String activeView,
              String sortField,
              String sortDir,
              String baseId);

    void disableTypeFilter(boolean disable);

    void setSearchRegexPattern(String pattern);

    void setHeading(String heading);

    void setLoadingMask(boolean loading);

    void setApps(Splittable apps, boolean loading);

    void render();

    void setViewType(String viewType);

    void loadSearchResults(Splittable apps,
                           String searchRegexPattern,
                           String heading,
                           boolean loading);


    void setTypeFilter(String filter);

    void setSortInfo(String sortField, String sortDir);

}
