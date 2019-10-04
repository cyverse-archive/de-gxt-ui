package org.iplantc.de.teams.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class TeamsPresenterImplTest {

    @Mock TeamsView.TeamsViewAppearance appearanceMock;
    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock CollaboratorsServiceFacade collaboratorsFacadeMock;
    @Mock TeamsView viewMock;
    @Mock TeamsFilter currentFilterMock;
    @Mock Group groupMock;
    @Mock List<Group> groupListMock;
    @Mock List<Subject> subjectListMock;
    @Mock EditTeamDialog editTeamDialogMock;
    @Mock AsyncProviderWrapper<EditTeamDialog> editDlgProviderMock;
    @Mock TeamsViewFactory viewFactoryMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loaderMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock List<String> creatorIdsMock;
    @Mock FastMap<Subject> fastMapSubjectMock;
    @Mock GroupAutoBeanFactory groupAutoBeanFactoryMock;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock Splittable teamSplittableMock;
    @Mock Splittable groupSplittableMock;
    @Mock Splittable groupListSplittableMock;
    @Mock ReactTeamViews.TeamProps teamPropsMock;

    @Captor ArgumentCaptor<AsyncCallback<EditTeamDialog>> editDlgProviderCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> subjectListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Splittable>> groupListSplCaptor;
    @Captor ArgumentCaptor<TeamSaved.TeamSavedHandler> teamSaveCaptor;
    @Captor ArgumentCaptor<LeaveTeamCompleted.LeaveTeamCompletedHandler> leaveTeamCaptor;
    @Captor ArgumentCaptor<AsyncCallback<FastMap<Subject>>> fastMapSubjectCaptor;

    TeamsPresenterImpl uut;

    @Before
    public void setUp() {
        when(appearanceMock.loadingMask()).thenReturn("loading");
        when(viewFactoryMock.create(any())).thenReturn(viewMock);

        uut = new TeamsPresenterImpl(appearanceMock,
                                     serviceFacadeMock,
                                     viewFactoryMock,
                                     groupAutoBeanFactoryMock,
                                     collaboratorsUtilMock){

            @Override
            Group convertSplittableToGroup(Splittable teamSpl) {
                return groupMock;
            }

            @Override
            ReactTeamViews.TeamProps getBaseTeamProps() {
                return teamPropsMock;
            }
        };
        uut.currentFilter = currentFilterMock;
        uut.announcer = announcerMock;
        uut.editTeamDlgProvider = editDlgProviderMock;

        verifyConstructor();
    }

    public void verifyConstructor() {
        verify(viewFactoryMock).create(eq(teamPropsMock));
    }

    @Test
    public void onTeamNameSelected() {
        TeamsPresenterImpl spy = spy(uut);
        TeamSaved saveEventMock = mock(TeamSaved.class);
        LeaveTeamCompleted leaveEventMock = mock(LeaveTeamCompleted.class);
        when(leaveEventMock.getTeam()).thenReturn(groupMock);
        when(saveEventMock.getGroup()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        spy.onTeamNameSelected(teamSplittableMock);

        verify(editDlgProviderMock).get(editDlgProviderCaptor.capture());

        editDlgProviderCaptor.getValue().onSuccess(editTeamDialogMock);
        verify(editTeamDialogMock).show(eq(groupMock));

        verify(editTeamDialogMock).addLeaveTeamCompletedHandler(leaveTeamCaptor.capture());
        leaveTeamCaptor.getValue().onLeaveTeamCompleted(leaveEventMock);
        verify(spy).refreshTeamListing();
    }

    @Test
    public void getTeams() {
        TeamsPresenterImpl spy = spy(uut);
        String filter = "MY_TEAMS";
        String searchTerm = "";

        /** CALL METHOD UNDER TEST **/
        spy.getTeams(filter, searchTerm);

        verify(viewMock).mask();
        verify(serviceFacadeMock).getTeams(eq(true), eq(searchTerm), groupListSplCaptor.capture());

        groupListSplCaptor.getValue().onSuccess(groupListSplittableMock);
        verify(spy).updateViewTeamList(eq(groupListSplittableMock));
    }
}
