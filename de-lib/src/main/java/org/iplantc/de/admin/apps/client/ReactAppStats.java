package org.iplantc.de.admin.apps.client;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 2/26/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps.admin", name = "AppStats")
public class ReactAppStats {

    @JsProperty(namespace = "CyVerseReactComponents.apps.admin", name = "AppStats")
    public static ReactClass<AppStatsProps> appStats;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class AppStatsProps extends BaseProps {
        AdminAppStatsGridView.Appearance appearance;
        AdminAppStatsGridView.Presenter presenter;
    }

}