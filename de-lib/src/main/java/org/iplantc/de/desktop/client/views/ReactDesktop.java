package org.iplantc.de.desktop.client.views;

import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 4/27/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.desktop", name = "DesktopView")
public class ReactDesktop {

    @JsProperty(namespace = "CyVerseReactComponents.desktop", name = "DesktopView")
    public static ReactClass<DesktopProps> desktopProps;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class DesktopProps extends BaseProps {
        DesktopView.Presenter presenter;
        MessageServiceFacade messageServiceFacade;
        Splittable[] windowConfigList;
        String desktopContainerId;
        boolean isNewUser;
        int unseen_count;
    }

}
