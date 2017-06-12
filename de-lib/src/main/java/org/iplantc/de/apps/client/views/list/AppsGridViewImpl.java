package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.List;

/**
 * Created by jstroot on 3/5/15.
 *
 * @author jstroot
 */
public class AppsGridViewImpl extends ContentPanel implements AppsListView,
                                                              SelectionChangedEvent.SelectionChangedHandler<App> {
    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsGridViewImpl> { }

    private static final AppsGridViewImplUiBinder ourUiBinder = GWT.create(AppsGridViewImplUiBinder.class);

    @UiField(provided = true) final ListStore<App> listStore;
    @UiField ColumnModel cm;
    @UiField Grid<App> grid;
    @UiField GridView<App> gridView;
    private final AppColumnModel acm; // Convenience class

    private final AppsListAppearance appearance;
    private String searchRegexPattern;

    @Inject
    AppsGridViewImpl(AppsListView.AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore) {
        this.appearance = appearance;
        this.listStore = listStore;

        setWidget(ourUiBinder.createAndBindUi(this));
        this.acm = (AppColumnModel) cm;
        grid.getSelectionModel().addSelectionChangedHandler(this);
        gridView.setEmptyText(appearance.noApps());
        new QuickTip(grid).getToolTipConfig().setTrackMouse(true);
    }

    //<editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEvent.AppCommentSelectedEventHandler handler) {
        return acm.addAppCommentSelectedEventHandlers(handler);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return acm.addAppFavoriteSelectedEventHandlers(handler);
    }

    @Override
    public HandlerRegistration addAppFavoritedEventHandler(AppFavoritedEvent.AppFavoritedEventHandler eventHandler) {
        return addHandler(eventHandler, AppFavoritedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return acm.addAppInfoSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return acm.addAppNameSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return acm.addAppRatingDeselectedHandler(handler);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return acm.addAppRatingSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }
    //</editor-fold>

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeading(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            acm.setSearchRegexPattern("");
        }
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        setHeading(Joiner.on(" >> ").join(event.getPath()));

        if (event.getSelectedHierarchy() != null) {
            // Reset Search
            acm.setSearchRegexPattern("");
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
    }

    @Override
    public List<DragSource> getAppsDragSources() {
        List<DragSource> sources = Lists.newArrayList();
        sources.add(new GridDragSource<>(grid));
        return sources;
    }

    @Override
    public App getSelectedItem() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public void select(App app, boolean keepExisting) {
        grid.getSelectionModel().select(app, keepExisting);
    }

    @Override
    public void deselectAll() {
        grid.getSelectionModel().deselectAll();
    }

    @Override
    public void setSearchPattern(final String searchPattern) {
        this.searchRegexPattern = searchPattern;
        acm.setSearchRegexPattern(searchRegexPattern);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        grid.ensureDebugId(baseID + AppsModule.Ids.APP_GRID);
        acm.ensureDebugId(baseID + AppsModule.Ids.APP_GRID);
    }

    @UiFactory
    ColumnModel<App> createColumnModel() {
        return new AppColumnModel(appearance);
    }
}
