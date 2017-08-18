package org.iplantc.de.theme.base.client.teams;

import com.google.gwt.i18n.client.Messages;

/**
 * The interface for all the textual strings used in the Teams view
 * @author aramsey
 */
public interface TeamsDisplayStrings extends Messages {
    String teamsMenu();

    String createNewTeam();

    String manageTeam();

    String leaveTeam();

    String teamInfoBtnToolTip();

    String teamNameLabel();

    String teamDescLabel();

    String detailsHeading(String subjectDisplayName);

    String membersLabel();

    String detailsGridEmptyText();

    String editTeamHeader(String teamName);

    String nonMembersSectionHeader();

    String membersSectionHeader();

    String nonMembersPrivilegeExplanation();

    String memberOptOutExplanation();

    String privilegeColumnLabel();

    String addPublicUser();

    String saveTeamHeader();

    String updatingTeamMsg();

    String updatingPrivilegesMsg();

    String updatingMembershipMsg();

    String miscTeamUpdates();

    String teamSuccessfullySaved();

    String searchFieldEmptyText();

    String teamSearchFailed();

    String leaveTeamSuccess(String subjectDisplayName);

    String leaveTeamWarning();

    String leaveTeamHeader(String subjectDisplayName);

    String leaveTeamFail();

    String deleteTeam();

    String joinTeam();

    String requestToJoinTeam();
}
