package org.iplantc.de.collaborators.client.gin;

import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.views.CollaboratorDNDHandler;

/**
 * @author aramsey
 */
public interface ManageCollaboratorsViewFactory {

    ManageCollaboratorsView create(ManageCollaboratorsView.MODE mode, CollaboratorDNDHandler dndHandler);
}
