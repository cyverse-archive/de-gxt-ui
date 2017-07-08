package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.models.TeamsFilter;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * An interface for the UI's Team view in the Collaboration window
 */
public interface TeamsView extends IsWidget,
                                   IsMaskable,
                                   TeamInfoButtonSelected.HasTeamInfoButtonSelectedHandlers,
                                   TeamFilterSelectionChanged.HasTeamFilterSelectionChangedHandlers {

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
    }

    /**
     * This presenter is responsible for managing all the events from the TeamsView
     */
    interface Presenter {

        void go();
    }

    void addTeams(List<Group> result);

    void clearTeams();

    TeamsFilter getCurrentFilter();
}
