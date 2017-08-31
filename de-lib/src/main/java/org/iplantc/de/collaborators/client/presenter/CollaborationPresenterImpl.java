package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.gin.CollaborationViewFactory;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class CollaborationPresenterImpl implements CollaborationView.Presenter {

    private ManageCollaboratorsView.Presenter collabPresenter;
    private TeamsView.Presenter teamPresenter;
    private CollaborationView view;

    @Inject
    public CollaborationPresenterImpl(ManageCollaboratorsView.Presenter collabPresenter,
                                      TeamsView.Presenter teamPresenter,
                                      CollaborationViewFactory viewFactory) {

        this.collabPresenter = collabPresenter;
        this.teamPresenter = teamPresenter;
        this.view = viewFactory.create(collabPresenter.getView(), teamPresenter.getView());
    }

    @Override
    public void go(HasOneWidget container, CollaborationWindowConfig windowConfig) {
        collabPresenter.go(null, ManageCollaboratorsView.MODE.MANAGE);
        teamPresenter.go();

        container.setWidget(view);
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + CollaboratorsModule.Ids.VIEW);
    }
}
