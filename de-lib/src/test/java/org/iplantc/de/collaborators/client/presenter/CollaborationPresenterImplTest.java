package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.gin.CollaborationViewFactory;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class CollaborationPresenterImplTest {

    @Mock ManageCollaboratorsView.Presenter collabPresenterMock;
    @Mock TeamsView.Presenter teamPresenterMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock CollaborationView viewMock;
    @Mock CollaborationViewFactory viewFactoryMock;
    @Mock List<Subject> collabListMock;
    @Mock List<Group> teamListMock;
    @Mock List<Subject> teamSubjectListMock;
    @Mock List<Subject> subjectListMock;

    CollaborationPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.create(any(), any())).thenReturn(viewMock);

        uut = new CollaborationPresenterImpl(collabPresenterMock,
                                             teamPresenterMock,
                                             viewFactoryMock,
                                             factoryMock){
            @Override
            List<Subject> convertTeamsToSubjects(List<Group> teams) {
                return teamSubjectListMock;
            }

            @Override
            List<Subject> createEmptySubjectList() {
                return subjectListMock;
            }
        };
    }

    @Test
    public void go() {
        CollaborationPresenterImpl spy = spy(uut);
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock);

        verify(spy).go(eq(containerMock), eq(null));
        verify(teamPresenterMock).showCheckBoxes();
    }

    @Test
    public void go1() {
        HasOneWidget containerMock = mock(HasOneWidget.class);
        CollaborationWindowConfig windowConfigMock = mock(CollaborationWindowConfig.class);
        when(windowConfigMock.getSelectedTab()).thenReturn(CollaborationView.TAB.Teams);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock, windowConfigMock);
        verify(viewMock).setActiveTab(eq(CollaborationView.TAB.Teams));
        verify(collabPresenterMock).go();
        verify(teamPresenterMock).go();

        verify(containerMock).setWidget(eq(viewMock));
    }

    @Test
    public void getSelectedCollaborators() {
        when(collabPresenterMock.getSelectedSubjects()).thenReturn(collabListMock);
        when(teamPresenterMock.getSelectedTeams()).thenReturn(teamListMock);

        /** CALL METHOD UNDER TEST **/
        uut.getSelectedCollaborators();

        verify(collabPresenterMock).getSelectedSubjects();
        verify(teamPresenterMock).getSelectedTeams();

        verify(subjectListMock).addAll(collabListMock);
        verify(subjectListMock).addAll(teamSubjectListMock);
    }

}
