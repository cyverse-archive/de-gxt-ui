package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsMaskable;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * An interface for the UI's Team view in the Collaboration window
 */
public interface TeamsView extends IsWidget,
                                   IsMaskable {

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
    }

    /**
     * This presenter is responsible for managing all the events from the TeamsView
     */
    interface Presenter {

    }
}
