package org.iplantc.de.collaborators.client.gin;

import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.teams.client.TeamsView;

public interface CollaborationViewFactory {

    CollaborationView create(ManageCollaboratorsView collaboratorsView,
                             TeamsView teamsView);
}
