package org.iplantc.de.theme.base.client.collaborators;

import org.iplantc.de.collaborators.client.CollaborationView;

import com.google.gwt.core.client.GWT;

public class CollaborationViewDefaultAppearance implements CollaborationView.CollaborationViewAppearance{

    private CollaborationDisplayStrings displayStrings;

    public CollaborationViewDefaultAppearance() {
        this(GWT.create(CollaborationDisplayStrings.class));
    }

    public CollaborationViewDefaultAppearance(CollaborationDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }

    @Override
    public String getCollaboratorsTabText() {
        return displayStrings.getCollaboratorsTabText();
    }

    @Override
    public String getTeamsTabText() {
        return displayStrings.getTeamsTabText();
    }
}
