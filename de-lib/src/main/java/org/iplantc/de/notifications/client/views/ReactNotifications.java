package org.iplantc.de.notifications.client.views;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 7/10/18.
 */
@JsType(isNative = true,
        namespace = "CyVerseReactComponents.notifications.notifications",
        name = "NotificationView")
public class ReactNotifications {

    @JsProperty(namespace = "CyVerseReactComponents.notifications.notifications",
                name = "NotificationView")
    public static ReactClass<NotificationsProps> notifiProps;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class NotificationsProps extends BaseProps {
        NotificationView.Presenter presenter;
    }
}

