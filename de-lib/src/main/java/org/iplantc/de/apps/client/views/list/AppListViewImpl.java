package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

public class AppListViewImpl implements AppsListView {

    HTMLPanel panel;

    private ReactAppListing.AppListingProps props;

    @Inject
    public AppListViewImpl() {
        panel = new HTMLPanel("<div></div>");
        props = new ReactAppListing.AppListingProps();
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void load(AppsListView.Presenter presenter,
                     Splittable apps,
                     String heading,
                     String appTypeFiler,
                     String sortField,
                     String searchRegexPattern,
                     boolean enableTypeFilter,
                     String selectedAppId,
                     String activeView) {

        props.apps = apps;
        props.presenter = presenter;
        props.heading = heading;
        props.appTypeFilter = appTypeFiler;
        props.sortField = sortField;
        props.searchRegexPattern = searchRegexPattern;
        props.enableTypeFilter = enableTypeFilter;
        props.selectedAppId = selectedAppId;
        props.viewType = activeView;
        props.loading = false;
        render();
    }

    @Override
    public void setViewType(String viewType) {
        props.viewType = viewType;
        render();
    }

    @Override
    public void setHeading(String heading) {
        props.heading = heading;
        render();
    }


    @Override
    public void setLoadingMask(boolean loading) {
        props.loading = loading;
        render();
    }

    @Override
    public void setApps(Splittable apps) {
        props.apps = apps;
        render();
    }

    @Override
    public void render() {
        CyVerseReactComponents.render(ReactAppListing.AppListingView, props, panel.getElement());
    }
}
