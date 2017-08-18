package org.iplantc.de.theme.base.client.teams;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

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
    public String teamsMenu() {
        return displayStrings.teamsMenu();
    }

    @Override
    public String createNewTeam() {
        return displayStrings.createNewTeam();
    }

    @Override
    public String manageTeam() {
        return displayStrings.manageTeam();
    }

    @Override
    public String leaveTeam() {
        return displayStrings.leaveTeam();
    }

    @Override
    public int nameColumnWidth() {
        return 300;
    }

    @Override
    public String nameColumnLabel() {
        return iplantDisplayStrings.name();
    }

    @Override
    public int descColumnWidth() {
        return 500;
    }

    @Override
    public String descColumnLabel() {
        return iplantDisplayStrings.description();
    }

    @Override
    public int infoColWidth() {
        return 20;
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
    public int teamDetailsWidth() {
        return 500;
    }

    @Override
    public int teamDetailsHeight() {
        return 350;
    }

    @Override
    public String detailsHeading(Group group) {
        return displayStrings.detailsHeading(group.getExtension());
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
        return 600;
    }

    @Override
    public int editTeamHeight() {
        return 500;
    }

    @Override
    public String editTeamHeading(Group group) {
        if (group == null) {
            return displayStrings.createNewTeam();
        } else {
            return displayStrings.editTeamHeader(group.getSubjectDisplayName());
        }
    }

    @Override
    public ImageResource plusImage() {
        return iplantResources.add();
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
        return 250;
    }

    @Override
    public int privilegeColumnWidth() {
        return 300;
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
    public ImageResource editIcon() {
        return iplantResources.edit();
    }

    @Override
    public String searchFieldEmptyText() {
        return displayStrings.searchFieldEmptyText();
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
    public String leaveTeamWarning() {
        return displayStrings.leaveTeamWarning();
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
    public String joinTeam() {
        return displayStrings.joinTeam();
    }

    @Override
    public String requestToJoinTeam() {
        return displayStrings.requestToJoinTeam();
    }
}
