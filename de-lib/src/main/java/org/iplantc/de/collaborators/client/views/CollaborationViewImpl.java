package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;

public class CollaborationViewImpl extends Composite implements CollaborationView {

    interface MyUiBinder extends UiBinder<Widget, CollaborationViewImpl> {
    }

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField DETabPanel collaborationTabs;
    @UiField(provided = true) ManageCollaboratorsView collaboratorsView;
    @UiField(provided = true) TeamsView teamsView;
    @UiField(provided = true) CollaborationViewAppearance appearance;

    @Inject
    public CollaborationViewImpl(@Assisted ManageCollaboratorsView collaboratorsView,
                                 @Assisted TeamsView teamsView,
                                 CollaborationView.CollaborationViewAppearance appearance) {
        this.collaboratorsView = collaboratorsView;
        this.teamsView = teamsView;
        this.appearance = appearance;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public DETabPanel getCollaborationTabPanel() {
        return collaborationTabs;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        collaborationTabs.setTabDebugId(collaboratorsView.asWidget(), baseID + CollaboratorsModule.Ids.COLLABORATORS_TAB);
        collaboratorsView.asWidget().ensureDebugId(baseID + CollaboratorsModule.Ids.COLLABORATORS_TAB);

        collaborationTabs.setTabDebugId(teamsView.asWidget(), baseID + CollaboratorsModule.Ids.TEAMS_TAB);
        teamsView.asWidget().ensureDebugId(baseID + CollaboratorsModule.Ids.COLLABORATORS_TAB);

    }
}
