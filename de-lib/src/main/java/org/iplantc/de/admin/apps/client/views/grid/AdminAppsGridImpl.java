package org.iplantc.de.admin.apps.client.views.grid;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.PreviewHierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.SelectOntologyVersionEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.common.base.Joiner;
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

    private ReactAppsAdmin.AdminAppsListingProps props;

    @Inject
    AdminAppsGridImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void load(Presenter presenter) {
        props = new ReactAppsAdmin.AdminAppsListingProps();
        props.apps = null;
        props.loading = false;
        props.parentId = "adminAppListing";
        props.heading = "";
        props.presenter = presenter;
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
        CyVerseReactComponents.render(ReactAppsAdmin.AdminAppsGridListing, props, panel.getElement());
    }


    @Override
    public void clearAndAdd(Splittable apps) {
       /* props.apps = apps;
        render();*/
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        props.heading = Joiner.on(" >> ").join(event.getGroupHierarchy());
        render();
    }

    @Override
    public void onHierarchySelected(HierarchySelectedEvent event) {
        props.heading =   Joiner.on(" >> ").join(event.getPath()) ;
        render();
    }

    @Override
    public void onPreviewHierarchySelected(PreviewHierarchySelectedEvent event) {
        props.heading = Joiner.on(" >> ").join(event.getPath());
        render();
    }


    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChanged event) {
        props.heading = event.getPath();
        render();
    }


    @Override
    public void loadSearchResults(Splittable apps, String heading) {
        props.apps = apps;
        props.heading = heading;
        props.loading = false;
        render();
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        props.loading = true;
        render();
    }

    @Override
    public void onSelectOntologyVersion(SelectOntologyVersionEvent event) {
         props.heading = "";
        render();
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
