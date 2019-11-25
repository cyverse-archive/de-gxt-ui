package org.iplantc.de.admin.apps.client;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 2/26/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps", name = "admin")
public class ReactAppsAdmin {

    @JsProperty
    public static ComponentConstructorFn<AppStatsProps> AppStats;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class AppStatsProps extends BaseProps {
        AdminAppStatsGridView.Presenter presenter;
    }

    @JsProperty
    public static ComponentConstructorFn<AdminAppDetailsProps> AdminAppDetails;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AdminAppDetailsProps extends BaseProps {
        public AdminAppsGridView.Presenter presenter;
        public Splittable app;
        public Splittable details;
        public String restrictedChars;
        public String restrictedStartingChars;
        public String createDocWikiUrl;
        public String documentationTemplateUrl;
        public String parentId;
        public boolean open;
    }

    @JsProperty
    public static  ComponentConstructorFn<AppPublicationRequestProps> AppPublicationRequests;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AppPublicationRequestProps extends BaseProps {
       public AppPublicationRequestView.Presenter presenter;
       public String parentId;
       public Splittable requests;
       public boolean loading;
    }

    @JsProperty
    public static ComponentConstructorFn<AdminAppsListingProps> AdminAppsGridListing;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AdminAppsListingProps extends BaseProps {
        public String baseId;
        public Splittable apps;
        public boolean loading;
        public String parentId;
        public String heading;
        public AdminAppsGridView.Presenter presenter;
    }

}

