package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsType;

/**
 * @author sriram
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps", name = "listing")
public class ReactAppListing {
    public static ComponentConstructorFn<AppListingProps> AppTileListing;

    static class AppListingProps extends BaseProps {
        public Splittable apps;
        public AppsListView.Presenter presenter;
        public String heading;
        public String appTypeFilter;
        public String sortField;
        public String searchRegexPattern;
        public boolean enableTypeFilter;
        public Splittable selectedApp;
    }
}
