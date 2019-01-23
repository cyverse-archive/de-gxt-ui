package org.iplantc.de.communities.client.views;

import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.communities.client.ManageCommunitiesView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents.communities.communities", name = "ManageCommunitiesView")
public class ReactCommunities {

    @JsProperty(namespace = "CyVerseReactComponents.communities.communities", name = "ManageCommunitiesView")
    public static ReactClass<CommunitiesProps> CommunitiesView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class CommunitiesProps extends BaseProps {
        public String parentId;
        public ManageCommunitiesView.Presenter presenter;
        public CollaboratorsUtil collaboratorsUtil;
        public Splittable currentUser;
    }
}
