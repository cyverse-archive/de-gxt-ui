package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.AppTypeFilter;
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
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void load(AppsListView.Presenter presenter,
                     String activeView,
                     String sortField,
                     String sortDir,
                     String baseId) {

        props = new ReactAppListing.AppListingProps();
        props.presenter = presenter;
        props.baseId = baseId;
        props.apps = null;
        props.heading = "";
        props.typeFilter = AppTypeFilter.ALL.getFilterString();
        props.sortField = sortField;
        props.sortDir = sortDir;
        props.searchText = "";
        props.disableTypeFilter = false;
        props.selectedAppId = null;
        props.viewType = activeView;
        props.loading = true;
        render();
    }

    @Override
    public void disableTypeFilter(boolean disable) {
        props.disableTypeFilter = disable;
        render();
    }

    @Override
    public void setSearchRegexPattern(String pattern) {
        props.searchText = pattern;
        render();
    }

    @Override
    public void setViewType(String viewType) {
        props.viewType = viewType;
        render();
    }

    @Override
    public void loadSearchResults(Splittable apps,
                                  String searchRegexPattern,
                                  String heading,
                                  boolean loading) {
        props.apps = apps;
        props.heading = heading;
        props.searchText = searchRegexPattern != null ? searchRegexPattern : "";
        props.loading = loading;
        render();
    }

    @Override
    public void setTypeFilter(String filter) {
        props.typeFilter = filter;
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
    public void setApps(Splittable apps, boolean loading) {
        props.searchText = "";
        props.apps = apps;
        props.loading = loading;
        render();
    }

    @Override
    public void render() {
        CyVerseReactComponents.render(ReactAppListing.AppListingView, props, panel.getElement());
    }

    @Override
    public void setSortInfo(String sortField, String sortDir) {
        props.sortField = sortField;
        props.sortDir = sortDir;
        render();
    }
}
