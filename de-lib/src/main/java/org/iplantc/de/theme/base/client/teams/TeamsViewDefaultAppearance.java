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

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private TeamsDisplayStrings displayStrings;

    public TeamsViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<TeamsDisplayStrings> create(TeamsDisplayStrings.class));
    }

    public TeamsViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                      IplantResources iplantResources,
                                      TeamsDisplayStrings displayStrings) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
    }

    @Override
    public String leaveTeam() {
        return displayStrings.leaveTeam();
    }

    @Override
    public int nameColumnWidth() {
        return 200;
    }

    @Override
    public String nameColumnLabel() {
        return iplantDisplayStrings.name();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String teamNameLabel() {
        return displayStrings.teamNameLabel();
    }

    @Override
    public String teamDescLabel() {
        return displayStrings.teamDescLabel();
    }

    @Override
    public String membersLabel() {
        return displayStrings.membersLabel();
    }

    @Override
    public String detailsGridEmptyText() {
        return displayStrings.detailsGridEmptyText();
    }

    @Override
    public int editTeamWidth() {
        return 800;
    }

    @Override
    public int editTeamHeight() {
        return 500;
    }

    @Override
    public String editTeamHeading(Group group, boolean isAdmin) {
        if (group == null) {
            return displayStrings.createNewTeam();
        }
        if (isAdmin) {
            return displayStrings.editTeamHeader(group.getSubjectDisplayName());
        }

        return displayStrings.detailsHeading(group.getSubjectDisplayName());
    }

    @Override
    public String nonMembersSectionHeader() {
        return displayStrings.nonMembersSectionHeader();
    }

    @Override
    public String membersSectionHeader() {
        return displayStrings.membersSectionHeader();
    }

    @Override
    public String nonMembersPrivilegeExplanation() {
        return displayStrings.nonMembersPrivilegeExplanation();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.deleteIcon();
    }

    @Override
    public String removeButtonText() {
        return iplantDisplayStrings.remove();
    }

    @Override
    public String memberOptOutExplanation() {
        return displayStrings.memberOptOutExplanation();
    }

    @Override
    public String privilegeColumnLabel() {
        return displayStrings.privilegeColumnLabel();
    }

    @Override
    public String completeRequiredFieldsError() {
        return iplantDisplayStrings.completeRequiredFieldsError();
    }

    @Override
    public String completeRequiredFieldsHeading() {
        return iplantDisplayStrings.warning();
    }

    @Override
    public int privilegeComboWidth() {
        return 300;
    }

    @Override
    public int privilegeColumnWidth() {
        return 350;
    }

    @Override
    public String addPublicUser() {
        return displayStrings.addPublicUser();
    }

    @Override
    public String saveTeamHeader() {
        return displayStrings.saveTeamHeader();
    }

    @Override
    public String updatingTeamMsg() {
        return displayStrings.updatingTeamMsg();
    }

    @Override
    public String updatingPrivilegesMsg() {
        return displayStrings.updatingPrivilegesMsg();
    }

    @Override
    public String updatingMembershipMsg() {
        return displayStrings.updatingMembershipMsg();
    }

    @Override
    public String teamSuccessfullySaved() {
        return displayStrings.teamSuccessfullySaved();
    }

    @Override
    public String miscTeamUpdates() {
        return displayStrings.miscTeamUpdates();
    }

    @Override
    public String teamSearchFailed() {
        return displayStrings.teamSearchFailed();
    }

    @Override
    public String leaveTeamSuccess(Group team) {
        return displayStrings.leaveTeamSuccess(team.getSubjectDisplayName());
    }

    @Override
    public String leaveTeamHeader(Group team) {
        return displayStrings.leaveTeamHeader(team.getSubjectDisplayName());
    }

    @Override
    public SafeHtml leaveTeamWarning() {
        return SafeHtmlUtils.fromTrustedString(displayStrings.leaveTeamWarning());
    }

    @Override
    public int leaveTeamWidth() {
        return 250;
    }

    @Override
    public String leaveTeamFail() {
        return displayStrings.leaveTeamFail();
    }

    @Override
    public String gridHeight() {
        return "150px";
    }

    @Override
    public String deleteTeam() {
        return displayStrings.deleteTeam();
    }

    @Override
    public String requestToJoinTeam() {
        return displayStrings.requestToJoinTeam();
    }

    @Override
    public int editTeamAdjustedHeight(boolean isAdmin, boolean isMember) {
        if (isAdmin) {
            return 500;
        }
        if (isMember) {
            return 350;
        }
        return 125;
    }

    public SafeHtml deleteTeamWarning() {
        return SafeHtmlUtils.fromTrustedString(displayStrings.deleteTeamWarning());
    }

    @Override
    public int deleteTeamWidth() {
        return 250;
    }

    @Override
    public String deleteTeamHeader(Group team) {
        return displayStrings.deleteTeamHeader(team.getSubjectDisplayName());
    }

    @Override
    public String deleteTeamSuccess(Group team) {
        return displayStrings.deleteTeamSuccess(team.getSubjectDisplayName());
    }

    @Override
    public String joinTeamSuccess(Group team) {
        return displayStrings.joinTeamSuccess(team.getSubjectDisplayName());
    }

    @Override
    public String requestMessageLabel() {
        return displayStrings.requestMessageLabel();
    }

    @Override
    public int joinTeamWidth() {
        return 500;
    }

    @Override
    public String sendRequestButton() {
        return displayStrings.sendRequestButton();
    }

    @Override
    public String joinTeamHeader(Group team) {
        return displayStrings.joinTeamHeader(team.getSubjectDisplayName());
    }

    @Override
    public String joinTeamText(Group team) {
        return displayStrings.joinTeamText(team.getSubjectDisplayName());
    }

    @Override
    public String requestToJoinSubmitted(Group team) {
        return displayStrings.requestToJoinSubmitted(team.getSubjectDisplayName());
    }

    @Override
    public int institutionColumnWidth() {
        return 200;
    }

    @Override
    public String institutionColumnLabel() {
        return iplantDisplayStrings.institution();
    }

    @Override
    public String getCreatorNamesFailed() {
        return displayStrings.getCreatorNamesFailed();
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
