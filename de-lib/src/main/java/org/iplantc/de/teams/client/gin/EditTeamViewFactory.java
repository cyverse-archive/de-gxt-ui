package org.iplantc.de.teams.client.gin;

import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.ReactTeamViews;

/**
 * @author aramsey
 */
public interface EditTeamViewFactory {
    EditTeamView create(ReactTeamViews.EditTeamProps baseProps);
}
