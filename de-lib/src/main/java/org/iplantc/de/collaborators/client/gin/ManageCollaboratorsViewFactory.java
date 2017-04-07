package org.iplantc.de.collaborators.client.gin;

import org.iplantc.de.collaborators.client.views.ManageCollaboratorsView;

/**
 * @author aramsey
 */
public interface ManageCollaboratorsViewFactory {

    ManageCollaboratorsView create(ManageCollaboratorsView.MODE mode);
}
