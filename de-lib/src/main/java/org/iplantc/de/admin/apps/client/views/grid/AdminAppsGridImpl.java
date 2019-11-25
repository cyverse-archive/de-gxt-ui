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
 * @author jstroot sriram
 */
public class AdminAppsGridImpl implements AdminAppsGridView {

    HTMLPanel panel;

    private ReactAppsAdmin.AdminAppsListingProps props;

    @Inject
    AdminAppsGridImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void load(Presenter presenter, String baseId) {
        props = new ReactAppsAdmin.AdminAppsListingProps();
        props.baseId = baseId;
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
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
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


    public void setSearchResultsHeader(String heading) {
        props.heading = heading;
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
}
