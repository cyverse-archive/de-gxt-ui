package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.gin.CollaborationViewFactory;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.teams.client.TeamsView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

public class CollaborationPresenterImpl implements CollaborationView.Presenter {

    private ManageCollaboratorsView.Presenter collabPresenter;
    private TeamsView.Presenter teamPresenter;
    private GroupAutoBeanFactory factory;
    private CollaborationView view;

    @Inject
    public CollaborationPresenterImpl(ManageCollaboratorsView.Presenter collabPresenter,
                                      TeamsView.Presenter teamPresenter,
                                      CollaborationViewFactory viewFactory,
                                      GroupAutoBeanFactory factory) {

        this.collabPresenter = collabPresenter;
        this.teamPresenter = teamPresenter;
        this.factory = factory;
        this.view = viewFactory.create(collabPresenter.getView(), teamPresenter.getView());
    }

    @Override
    public void go(HasOneWidget container) {
        go(container, null);
        teamPresenter.showCheckBoxes();
    }

    @Override
    public void go(HasOneWidget container, CollaborationWindowConfig windowConfig) {
        if (windowConfig != null) {
            view.setActiveTab(windowConfig.getSelectedTab());
        }
        collabPresenter.go();
        teamPresenter.go();

        container.setWidget(view);
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + CollaboratorsModule.Ids.VIEW);
    }

    @Override
    public List<Subject> getSelectedCollaborators() {
        List<Subject> allCollaborators = createEmptySubjectList();

        List<Subject> collaborators = collabPresenter.getSelectedSubjects();
        List<Group> teams = teamPresenter.getSelectedTeams();
        List<Subject> teamsToSubjects = convertTeamsToSubjects(teams);

        allCollaborators.addAll(collaborators);
        allCollaborators.addAll(teamsToSubjects);
        return allCollaborators;
    }

    List<Subject> createEmptySubjectList() {
        return Lists.newArrayList();
    }

    List<Subject> convertTeamsToSubjects(List<Group> teams) {
        return teams.stream()
                    .map(team -> factory.convertGroupToSubject(team))
                    .collect(Collectors.toList());
    }
}
