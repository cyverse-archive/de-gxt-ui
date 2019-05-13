package org.iplantc.de.apps.widgets.client.view;


import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 *
 * Created by sriram on 04/10/19
 *
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps", name = "quickLaunch")
public class ReactQuickLaunch {

    public static ComponentConstructorFn<CreateQLProps> CreateQuickLaunchDialog;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class CreateQLProps extends BaseProps {
       public  AppLaunchView.Presenter presenter;
       public  String appName;
       public boolean isOwner;
       public boolean dialogOpen;
       public String baseDebugId;
    }

}
