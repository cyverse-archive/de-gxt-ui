package org.iplantc.de.theme.base.client.teams;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * The default appearance that will be used for the Teams view
 * @author aramsey
 */
public class TeamsViewDefaultAppearance implements TeamsView.TeamsViewAppearance {

    private TeamsDisplayStrings displayStrings;

    public TeamsViewDefaultAppearance() {
        this(GWT.<TeamsDisplayStrings> create(TeamsDisplayStrings.class));
    }

    public TeamsViewDefaultAppearance(TeamsDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }

    @Override
    public String leaveTeamSuccess(String teamName) {
        return displayStrings.leaveTeamSuccess(teamName);
    }

    @Override
    public String leaveTeamFail() {
        return displayStrings.leaveTeamFail();
    }

    @Override
    public String deleteTeamSuccess(String teamName) {
        return displayStrings.deleteTeamSuccess(teamName);
    }

    @Override
    public String joinTeamSuccess(String teamName) {
        return displayStrings.joinTeamSuccess(teamName);
    }

    @Override
    public String requestToJoinSubmitted(String teamName) {
        return displayStrings.requestToJoinSubmitted(teamName);
    }

    @Override
    public SafeHtml editTeamHelpText() {
        return displayStrings.editTeamHelpText();
    }

    @Override
    public String teamsHelp() {
        return displayStrings.teamsHelp();
    }

    @Override
    public int windowMinWidth() {
        return 200;
    }

    @Override
    public int windowMinHeight() {
        return 300;
    }

    @Override
    public String windowHeading() {
        return displayStrings.teamsMenu();
    }

    @Override
    public String windowWidth() {
        return "600px";
    }

    @Override
    public String windowHeight() {
        return "400px";
    }
}
