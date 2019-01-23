package org.iplantc.de.notifications.client;

import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.client.views.NotificationView.Presenter;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 7/10/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "notifications")
public class ReactNotifications {

    @JsProperty
    public static ComponentConstructorFn<NotificationsProps> NotificationView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class NotificationsProps extends BaseProps {
        public Presenter presenter;
        public String baseDebugId;
        public String category;
    }

    @JsProperty
    public static ComponentConstructorFn<JoinTeamProps> JoinTeamRequestDialog;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class JoinTeamProps extends BaseProps {
        public JoinTeamRequestView.Presenter presenter;
        public Splittable request;
        public boolean dialogOpen;
    }


    @JsProperty
    public static ComponentConstructorFn<DenyTeamProps> DenyJoinRequestDetailsDialog;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class DenyTeamProps extends BaseProps {
        public String teamName;
        public String adminMessage;
        public boolean dialogOpen;
    }

    @JsProperty
    public static ComponentConstructorFn<HistoryProps> RequestHistoryDialog;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class HistoryProps extends BaseProps {
        public Splittable[] history;
        public boolean dialogOpen;
        public String name;
        public String category;
    }
}


