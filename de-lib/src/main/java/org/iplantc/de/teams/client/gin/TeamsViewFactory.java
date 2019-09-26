package org.iplantc.de.teams.client.gin;

import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;

public interface TeamsViewFactory {
    TeamsView create(ReactTeamViews.TeamProps baseProps);
}
