package org.iplantc.de.communities.client.views;

import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.communities.client.ManageCommunitiesView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "communities")
public class ReactCommunities {

    public static ComponentConstructorFn<CommunitiesProps> ManageCommunitiesView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class CommunitiesProps extends BaseProps {
        public String parentId;
        public ManageCommunitiesView.Presenter presenter;
        public CollaboratorsUtil collaboratorsUtil;
        public Splittable currentUser;
    }
}
