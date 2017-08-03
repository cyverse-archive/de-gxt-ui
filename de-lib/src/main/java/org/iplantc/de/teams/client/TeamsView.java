package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.events.CreateTeamSelected;
import org.iplantc.de.teams.client.events.EditTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.models.TeamsFilter;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * An interface for the UI's Team view in the Collaboration window
 */
public interface TeamsView extends IsWidget,
                                   IsMaskable,
                                   TeamInfoButtonSelected.HasTeamInfoButtonSelectedHandlers,
                                   TeamFilterSelectionChanged.HasTeamFilterSelectionChangedHandlers,
                                   CreateTeamSelected.HasCreateTeamSelectedHandlers,
                                   EditTeamSelected.HasEditTeamSelectedHandlers,
                                   TeamSearchResultLoad.TeamSearchResultLoadHandler {

    /**
     * An appearance class for all string related items in the Teams view
     */
    interface TeamsViewAppearance {

        String teamsMenu();

        String createNewTeam();

        String manageTeam();

        String leaveTeam();

        int nameColumnWidth();

        String nameColumnLabel();

        int descColumnWidth();

        String descColumnLabel();

        int infoColWidth();

        String loadingMask();

        String teamNameLabel();

        String teamDescLabel();

        int teamDetailsWidth();

        int teamDetailsHeight();

        String detailsHeading(Group group);

        String membersLabel();

        String detailsGridEmptyText();

        int editTeamWidth();

        int editTeamHeight();

        String editTeamHeading(Group group);

        ImageResource plusImage();

        String nonMembersSectionHeader();

        String membersSectionHeader();

        String nonMembersPrivilegeExplanation();

        ImageResource deleteIcon();

        String removeButtonText();

        String memberOptOutExplanation();

        String privilegeColumnLabel();

        String completeRequiredFieldsError();

        String completeRequiredFieldsHeading();

        int privilegeComboWidth();

        int privilegeColumnWidth();

        String addPublicUser();

        String saveTeamHeader();

        String updatingTeamMsg();

        String updatingPrivilegesMsg();

        String updatingMembershipMsg();

        String teamSuccessfullySaved();

        String miscTeamUpdates();

        ImageResource editIcon();

        String searchFieldEmptyText();

        String teamSearchFailed();

        String leaveTeamSuccess(Group group);

        String leaveTeamHeader(Group group);

        String leaveTeamWarning();

        int leaveTeamWidth();
    }

    /**
     * This presenter is responsible for managing all the events from the TeamsView
     */
    interface Presenter {

        /**
         * Initialize the Team presenter to begin fetching teams
         */
        void go();
    }

    /**
     * Add the specified groups to the Teams view
     * @param result
     */
    void addTeams(List<Group> result);

    /**
     * Remove any teams from the ListStore
     */
    void clearTeams();

    /**
     * Return the filter currently set in the Team view
     * @return
     */
    TeamsFilter getCurrentFilter();

    /**
     * Update the details of a team in the grid
     * @param team
     */
    void updateTeam(Group team);
}
