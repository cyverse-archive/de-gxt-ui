package org.iplantc.de.teams.client;

import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "teams")
public class ReactTeamViews {

    @JsProperty
    public static ComponentConstructorFn<ReactTeamViews.TeamProps> Teams;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class TeamProps extends BaseProps {
        public TeamsView.Presenter presenter;
        public CollaboratorsUtil collaboratorsUtil;
        public String parentId;
        public boolean loading;
        public Splittable teamListing;
        public Splittable selectedTeams;
        public boolean isSelectable;
    }
}
