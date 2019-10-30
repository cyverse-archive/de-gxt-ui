package org.iplantc.de.admin.apps.client.views.grid;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.PreviewHierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.SelectOntologyVersionEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Created by jstroot on 3/9/15.
 * @author jstroot
 */
public class AdminAppsGridImpl implements AdminAppsGridView/*,
                                                               SelectionChangedEvent.SelectionChangedHandler<App>*/ {

    HTMLPanel panel;

    private ReactAppsAdmin.AdminAppListingProps props;

    @Inject
    AdminAppsGridImpl() {
    }

    @Override
    public void load(Presenter presenter) {
        props = new ReactAppsAdmin.AdminAppListingProps();
        props.apps = null;
        props.loading = false;
        props.parentId = "adminAppListing";
        render();
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setLoading(boolean loading) {
        props.loading = loading;
        render();
    }

    @Override
    public void setApps(Splittable apps,
                        boolean loading) {
        props.apps = apps;
        props.loading = loading;
        render();
    }

    private void render() {
        CyVerseReactComponents.render(ReactAppsAdmin.AdminAppGridListing, props, panel.getElement());
    }


    @Override
    public void clearAndAdd(Splittable apps) {
        props.apps = apps;
        render();
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
       // setHeading(Joiner.on(" >> ").join(event.getGroupHierarchy()));
    }

    @Override
    public void onHierarchySelected(HierarchySelectedEvent event) {
        //setHeading(Joiner.on(" >> ").join(event.getPath()));
    }

    @Override
    public void onPreviewHierarchySelected(PreviewHierarchySelectedEvent event) {
        //setHeading(Joiner.on(" >> ").join(event.getPath()));
    }


    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChanged event) {
        //setHeading(event.getPath());
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        //unmask();
        //        searchRegexPattern = event.getSearchPattern();
        //        acm.setSearchRegexPattern(searchRegexPattern);

        int total = event.getResults() == null ? 0 : event.getResults().getTotal();
        //setHeading(appearance.searchAppResultsHeader(event.getSearchText(), total));
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        //mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectOntologyVersion(SelectOntologyVersionEvent event) {
       // getHeader().setHTML("&nbsp;");
    }

/*    @Override
    protected void onEnsureDebugId(final String baseID) {
        super.onEnsureDebugId(baseID);

        acm.ensureDebugId(baseID);
        grid.asWidget().ensureDebugId(baseID + Belphegor.AppIds.GRID);
        grid.addViewReadyHandler(new ViewReadyEvent.ViewReadyHandler() {
            @Override
            public void onViewReady(ViewReadyEvent event) {
                StaticIdHelper.getInstance()
                              .gridColumnHeaders(baseID + Belphegor.AppIds.GRID
                                                 + Belphegor.AppIds.COL_HEADER, grid);
            }
        });
    } */

  /*  @Override
    public App getAppFromElement(Element as) {
        Element row = gridView.findRow(as);
        int dropIndex = gridView.findRowIndex(row);
        return listStore.get(dropIndex);
    }

    @Override
    public List<App> getSelectedApps() {
        return grid.getSelectionModel().getSelectedItems();
    }

    @Override
    public void deselectAll() {
        grid.getSelectionModel().deselectAll();
    }

    @Override
    public void removeApp(App selectedApp) {
        App app = listStore.findModelWithKey(selectedApp.getId());
        if (app != null) {
            listStore.remove(app);
        }
    }

   */
}
