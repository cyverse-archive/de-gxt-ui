package org.iplantc.de.notifications.client.views;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true,
        namespace = "CyVerseReactComponents.notifications",
        name = "JoinTeamRequestDialog")
class ReactJoinTeamRequest {

    @JsProperty(namespace = "CyVerseReactComponents.notifications",
                name = "JoinTeamRequestDialog")
    public static ReactClass<JoinTeamProps> joinTeamProps;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class JoinTeamProps extends BaseProps {
        JoinTeamRequestView.Presenter presenter;
        Splittable request;
        boolean dialogOpen;
    }
}