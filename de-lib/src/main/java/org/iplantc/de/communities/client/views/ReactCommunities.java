package org.iplantc.de.communities.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents.communities.communities", name = "CommunitiesView")
public class ReactCommunities {

    @JsProperty(namespace = "CyVerseReactComponents.communities.communities", name = "CommunitiesView")
    public static ReactClass<CommunitiesProps> CommunitiesView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class CommunitiesProps extends BaseProps {
        public String parentId;
        public org.iplantc.de.communities.client.CommunitiesView.Presenter presenter;
        public CollaboratorsUtil collaboratorsUtil;
        public Splittable currentUser;
    }
}
