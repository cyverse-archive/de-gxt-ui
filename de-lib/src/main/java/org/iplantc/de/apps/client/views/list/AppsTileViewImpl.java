package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.Comparator;

/**
 * @author aramsey
 */
public class AppsTileViewImpl implements AppsListView {

    @Override
    public Widget asWidget() {
        return panel;
    }

    class AppRatingComparator implements Comparator<App> {

        @Override
        public int compare(App o1, App o2) {
            if (o1.getRating().getAverage() < o2.getRating().getAverage()) return -1;
            if (o1.getRating().getAverage() > o2.getRating().getAverage()) return 1;
            return 0;
        }
    }

    enum SortChoice {
        Name,
        Rating,
        Integrator
    }

    HTMLPanel panel;

    private ReactAppListing.AppListingProps props;

    @Inject
    AppsTileViewImpl() {
        panel = new HTMLPanel("<div></div>");
        props = new ReactAppListing.AppListingProps();

        //  appTileCell.setHasHandlers(this);
       // appTileCell.setCardUrl(deProperties.getAppsCardUrl(), deProperties.getAppsCardUrlOptions());

        // setWidget(ourUiBinder.createAndBindUi(this));

      //  listView.getSelectionModel().addSelectionChangedHandler(this);
     //   listView.setCell(this.appTileCell);
    }

    public void load(AppsListView.Presenter presenter,
                     Splittable apps,
                     String heading,
                     String appTypeFiler,
                     String sortField,
                     String searchRegexPattern,
                     boolean enableTypeFilter,
                     String selectedAppId) {

        props.apps = apps;
        props.presenter = presenter;
        props.heading = heading;
        props.appTypeFilter = appTypeFiler;
        props.sortField = sortField;
        props.searchRegexPattern = searchRegexPattern;
        props.enableTypeFilter = enableTypeFilter;
        props.selectedAppId = selectedAppId;
        CyVerseReactComponents.render(ReactAppListing.AppTileListing, props, panel.getElement());
    }

 /*     public Store.StoreSortInfo<App> getSortInfo() {
      SortChoice sortField = sortBox.getCurrentValue();

        switch(sortField) {
            case Name:
                return new Store.StoreSortInfo<>(properties.name(), SortDir.ASC);
            case Integrator:
                return new Store.StoreSortInfo<>(properties.integratorName(), SortDir.ASC);
            case Rating:
                return new Store.StoreSortInfo<>(new AppRatingComparator(), SortDir.DESC);
            default:
                return null;
        }
    }*/

 /*   @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return addHandler(handler, AppNameSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return addHandler(handler, AppInfoSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEvent.AppCommentSelectedEventHandler handler) {
        return addHandler(handler, AppCommentSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return addHandler(handler, AppFavoriteSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoritedEventHandler(AppFavoritedEvent.AppFavoritedEventHandler eventHandler) {
        return addHandler(eventHandler, AppFavoritedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return addHandler(handler, AppRatingDeselected.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return addHandler(handler, AppRatingSelected.TYPE);
    }
*/
/*    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeading(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        setHeading(Joiner.on(" >> ").join(event.getPath()));

        if (event.getSelectedHierarchy() != null) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChangedEvent event) {
        setHeading(Joiner.on(" >> ").join(event.getPath()));

        if (event.getCommunitySelection() != null) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        int total = event.getResults() == null ? 0 : event.getResults().size();
        setHeading(appearance.searchAppResultsHeader(event.getSearchText(), total));
        unmask();
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<App> event) {
        fireEvent(new AppSelectionChangedEvent(event.getSelection()));
    }*/

/*    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        listView.ensureDebugId(baseID + AppsModule.Ids.APP_TILES);
        appTileCell.setDebugBaseId(baseID + AppsModule.Ids.APP_TILES);
    }*/

  /*  @Override
    public List<DragSource> getAppsDragSources() {
        List<DragSource> sources = Lists.newArrayList();
        sources.add(new ListViewDragSource<>(listView));
        return sources;
    }

    @Override
    public App getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    @Override
    public void select(App app, boolean keepExisting) {
        listView.getSelectionModel().select(app, keepExisting);
    }

    @Override
    public void deselectAll() {
        listView.getSelectionModel().deselectAll();
    }

    @Override
    public void setSearchPattern(String searchPattern) {
        appTileCell.setSearchRegexPattern(searchPattern);
    }

    @Override
    public void setHeading(String text) {

    }

    @Override
    public void setAppTypeFilter(AppTypeFilter filter) {
        typeFilter.setFilter(filter);
    }

    @Override
    public void enableAppTypeFilter(boolean enabled) {
        if(enabled) {
            typeFilter.enable();
        } else {
            typeFilter.disbale();
        }
    }*/
}
