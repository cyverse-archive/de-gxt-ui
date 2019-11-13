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

    @JsProperty
    public static ComponentConstructorFn<ReactTeamViews.TeamProps> EditTeamDialog;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class EditTeamProps extends BaseProps {
        public String parentId;
        public EditTeamView.Presenter presenter;
        public boolean loading;
        public boolean open;
        public CollaboratorsUtil collaboratorsUtil;
        public Splittable team;
        public Splittable privileges;
        public Splittable members;
        public String groupNameRestrictedChars;
        public Splittable selfSubject;
        public String publicUsersId;
    }
}
