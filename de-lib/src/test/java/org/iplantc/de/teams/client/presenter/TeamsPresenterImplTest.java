package org.iplantc.de.teams.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamNameSelected;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.EditTeamDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

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
    @Mock TeamSearchRpcProxy searchProxyMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loaderMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock List<String> creatorIdsMock;
    @Mock FastMap<Subject> fastMapSubjectMock;

    @Captor ArgumentCaptor<AsyncCallback<EditTeamDialog>> editDlgProviderCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> subjectListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;
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
                                     collaboratorsFacadeMock,
                                     viewFactoryMock,
                                     searchProxyMock){
            @Override
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> getPagingLoader() {
                return loaderMock;
            }

            @Override
            List<String> getCreatorIds(List<Group> teams) {
                return creatorIdsMock;
            }

            @Override
            void addCreatorToTeams(List<Group> teams, FastMap<Subject> creatorFastMap) {
            }
        };
        uut.currentFilter = currentFilterMock;
        uut.announcer = announcerMock;
        uut.editTeamDlgProvider = editDlgProviderMock;

        verifyConstructor();
    }

    public void verifyConstructor() {
        verify(viewFactoryMock).create(eq(loaderMock));
        verify(viewMock).addTeamNameSelectedHandler(eq(uut));
        verify(viewMock).addTeamFilterSelectionChangedHandler(eq(uut));
        verify(viewMock).addCreateTeamSelectedHandler(eq(uut));
        verify(searchProxyMock).addTeamSearchResultLoadHandler(eq(uut));
        verify(searchProxyMock).addTeamSearchResultLoadHandler(eq(viewMock));
    }

    @Test
    public void go() {
        TeamsPresenterImpl spy = spy(uut);
        when(viewMock.getCurrentFilter()).thenReturn(currentFilterMock);

        /** CALL METHOD UNDER TEST **/
        spy.go();
        verify(spy).getSelectedTeams();
    }

    @Test
    public void onTeamNameSelected() {
        TeamNameSelected eventMock = mock(TeamNameSelected.class);
        TeamSaved saveEventMock = mock(TeamSaved.class);
        LeaveTeamCompleted leaveEventMock = mock(LeaveTeamCompleted.class);
        when(leaveEventMock.getTeam()).thenReturn(groupMock);
        when(saveEventMock.getGroup()).thenReturn(groupMock);
        when(eventMock.getTeam()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.onTeamNameSelected(eventMock);

        verify(editDlgProviderMock).get(editDlgProviderCaptor.capture());

        editDlgProviderCaptor.getValue().onSuccess(editTeamDialogMock);
        verify(editTeamDialogMock).show(eq(groupMock));

        verify(editTeamDialogMock).addTeamSavedHandler(teamSaveCaptor.capture());
        teamSaveCaptor.getValue().onTeamSaved(saveEventMock);
        verify(viewMock).updateTeam(eq(groupMock));

        verify(editTeamDialogMock).addLeaveTeamCompletedHandler(leaveTeamCaptor.capture());
        leaveTeamCaptor.getValue().onLeaveTeamCompleted(leaveEventMock);
        verify(viewMock).removeTeam(eq(groupMock));
    }

    @Test
    public void onTeamFilterSelectionChanged_currentFilter() {
        TeamsPresenterImpl spy = spy(uut);
        TeamFilterSelectionChanged eventMock = mock(TeamFilterSelectionChanged.class);
        when(eventMock.getFilter()).thenReturn(currentFilterMock);

        /** CALL METHOD UNDER TEST **/
        spy.onTeamFilterSelectionChanged(eventMock);
        verify(spy, times(0)).getSelectedTeams();
    }

    @Test
    public void onTeamFilterSelectionChanged_newFilter() {
        TeamsPresenterImpl spy = spy(uut);
        TeamFilterSelectionChanged eventMock = mock(TeamFilterSelectionChanged.class);
        TeamsFilter filterMock = TeamsFilter.MY_TEAMS;
        when(eventMock.getFilter()).thenReturn(filterMock);

        /** CALL METHOD UNDER TEST **/
        spy.onTeamFilterSelectionChanged(eventMock);
        verify(spy).getSelectedTeams();
    }

    @Test
    public void getSelectedTeams_newFilterAll() {
        TeamsPresenterImpl spy = spy(uut);
        spy.currentFilter = TeamsFilter.ALL;

        /** CALL METHOD UNDER TEST **/
        spy.getSelectedTeams();
        verify(spy).getAllTeams();
    }

    @Test
    public void getSelectedTeams_newFilterMine() {
        TeamsPresenterImpl spy = spy(uut);
        spy.currentFilter = TeamsFilter.MY_TEAMS;

        /** CALL METHOD UNDER TEST **/
        spy.getSelectedTeams();
        verify(spy).getMyTeams();
    }

    @Test
    public void getMyTeams() {
        TeamsPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.getMyTeams();

        verify(viewMock).mask(eq("loading"));
        verify(serviceFacadeMock).getMyTeams(groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);
        verify(spy).getTeamCreatorNames(eq(groupListMock));
    }

    @Test
    public void getAllTeams() {
        TeamsPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.getAllTeams();

        verify(viewMock).mask(eq("loading"));
        verify(serviceFacadeMock).getTeams(groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);
        verify(spy).getTeamCreatorNames(eq(groupListMock));
    }

    @Test
    public void addTeamsToView() {
        when(groupListMock.isEmpty()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        uut.addTeamsToView(groupListMock);
        verify(viewMock).clearTeams();
        verify(viewMock).addTeams(eq(groupListMock));
        verify(viewMock).unmask();
    }

    @Test
    public void onTeamSearchResultLoad() {
        TeamSearchResultLoad eventMock = mock(TeamSearchResultLoad.class);
        when(eventMock.getSearchResults()).thenReturn(groupListMock);
        when(groupListMock.isEmpty()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        uut.onTeamSearchResultLoad(eventMock);

        verify(viewMock).clearTeams();
        verify(viewMock).addTeams(eq(groupListMock));
    }

    @Test
    public void getTeamCreatorNames_onSuccess() {
        TeamsPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.getTeamCreatorNames(groupListMock);

        verify(collaboratorsFacadeMock).getUserInfo(eq(creatorIdsMock), fastMapSubjectCaptor.capture());

        fastMapSubjectCaptor.getValue().onSuccess(fastMapSubjectMock);
        verify(spy).addCreatorToTeams(eq(groupListMock), eq(fastMapSubjectMock));
        verify(spy).addTeamsToView(eq(groupListMock));
    }

    @Test
    public void getTeamCreatorNames_onFailure() {
        TeamsPresenterImpl spy = spy(uut);
        when(appearanceMock.getCreatorNamesFailed()).thenReturn("fail");

        /** CALL METHOD UNDER TEST **/
        spy.getTeamCreatorNames(groupListMock);

        verify(collaboratorsFacadeMock).getUserInfo(eq(creatorIdsMock), fastMapSubjectCaptor.capture());

        fastMapSubjectCaptor.getValue().onFailure(mock(Throwable.class));
        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(spy).addTeamsToView(eq(groupListMock));
    }
}
