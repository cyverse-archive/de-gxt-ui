package org.iplantc.de.theme.base.client.teams;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * The interface for all the textual strings used in the Teams view
 * @author aramsey
 */
public interface TeamsDisplayStrings extends Messages {
    String teamsMenu();

    String teamInfoBtnToolTip();

    String leaveTeamSuccess(String subjectDisplayName);

    String leaveTeamFail();

    String deleteTeamSuccess(String subjectDisplayName);

    String joinTeamSuccess(String teamName);

    String requestToJoinSubmitted(String teamName);

    SafeHtml editTeamHelpText();

    String teamsHelp();
}
