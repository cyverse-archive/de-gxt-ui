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
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.LeaveTeamSelected;
import org.iplantc.de.teams.client.events.TeamFilterSelectionChanged;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.client.views.dialogs.LeaveTeamDialog;
import org.iplantc.de.teams.client.views.dialogs.TeamDetailsDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

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
    @Mock TeamsView viewMock;
    @Mock TeamsFilter currentFilterMock;
    @Mock Group groupMock;
    @Mock List<Group> groupListMock;
    @Mock List<Subject> subjectListMock;
    @Mock List<UpdateMemberResult> updateMemberResultListMock;
    @Mock UpdateMemberResult updateMemberResultMock;
    @Mock TeamDetailsDialog detailsDialogMock;
    @Mock LeaveTeamDialog leaveDialogMock;
    @Mock AsyncProviderWrapper<TeamDetailsDialog> detailsDlgProviderMock;
    @Mock AsyncProviderWrapper<LeaveTeamDialog> leaveDlgProviderMock;
    @Mock TeamsViewFactory viewFactoryMock;
    @Mock TeamSearchRpcProxy searchProxyMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loaderMock;
    @Mock IplantAnnouncer announcerMock;

    @Captor ArgumentCaptor<AsyncCallback<TeamDetailsDialog>> detailsDlgProviderCaptor;
    @Captor ArgumentCaptor<AsyncCallback<LeaveTeamDialog>> leaveDlgProviderCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> subjectListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateMemberCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> dialogHideCaptor;

    TeamsPresenterImpl uut;

    @Before
    public void setUp() {
        when(appearanceMock.loadingMask()).thenReturn("loading");
        when(viewFactoryMock.create(any())).thenReturn(viewMock);

        uut = new TeamsPresenterImpl(appearanceMock,
                                     serviceFacadeMock,
                                     viewFactoryMock,
                                     searchProxyMock){
            @Override
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> getPagingLoader() {
                return loaderMock;
            }
        };
        uut.currentFilter = currentFilterMock;
        uut.detailsDlgProvider = detailsDlgProviderMock;
        uut.leaveTeamDlgProvider = leaveDlgProviderMock;
        uut.announcer = announcerMock;

        verifyConstructor();
    }

    public void verifyConstructor() {
        verify(viewFactoryMock).create(eq(loaderMock));
        verify(viewMock).addTeamInfoButtonSelectedHandler(eq(uut));
        verify(viewMock).addTeamFilterSelectionChangedHandler(eq(uut));
        verify(viewMock).addCreateTeamSelectedHandler(eq(uut));
        verify(viewMock).addEditTeamSelectedHandler(eq(uut));
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
    public void onTeamInfoButtonSelected() {
        TeamInfoButtonSelected eventMock = mock(TeamInfoButtonSelected.class);
        when(eventMock.getGroup()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.onTeamInfoButtonSelected(eventMock);

        verify(serviceFacadeMock).getTeamMembers(eq(groupMock), subjectListCaptor.capture());

        subjectListCaptor.getValue().onSuccess(subjectListMock);
        verify(detailsDlgProviderMock).get(detailsDlgProviderCaptor.capture());

        detailsDlgProviderCaptor.getValue().onSuccess(detailsDialogMock);
        verify(detailsDialogMock).show(eq(groupMock), eq(subjectListMock));
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

        /** CALL METHOD UNDER TEST **/
        uut.getMyTeams();

        verify(viewMock).mask(eq("loading"));
        verify(serviceFacadeMock).getMyTeams(groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);
        verify(viewMock).clearTeams();
        verify(viewMock).addTeams(eq(groupListMock));
        verify(viewMock).unmask();
    }

    @Test
    public void getAllTeams() {
        /** CALL METHOD UNDER TEST **/
        uut.getAllTeams();

        verify(viewMock).mask(eq("loading"));
        verify(serviceFacadeMock).getTeams(groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);
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
    public void onLeaveTeamSelected() {
        LeaveTeamSelected eventMock = mock(LeaveTeamSelected.class);
        when(eventMock.getGroup()).thenReturn(groupMock);
        DialogHideEvent hideEventMock = mock(DialogHideEvent.class);
        when(hideEventMock.getHideButton()).thenReturn(Dialog.PredefinedButton.YES);

        TeamsPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onLeaveTeamSelected(eventMock);

        verify(leaveDlgProviderMock).get(leaveDlgProviderCaptor.capture());

        leaveDlgProviderCaptor.getValue().onSuccess(leaveDialogMock);

        verify(leaveDialogMock).show(eq(groupMock));
        verify(leaveDialogMock).addDialogHideHandler(dialogHideCaptor.capture());

        dialogHideCaptor.getValue().onDialogHide(hideEventMock);
        verify(spy).leaveTeam(eq(groupMock));
    }

    @Test
    public void leaveTeam() {
        when(updateMemberResultListMock.isEmpty()).thenReturn(false);
        when(updateMemberResultListMock.get(0)).thenReturn(updateMemberResultMock);
        when(updateMemberResultMock.isSuccess()).thenReturn(true);
        when(appearanceMock.leaveTeamSuccess(any())).thenReturn("success");
        when(appearanceMock.leaveTeamFail()).thenReturn("fail");

        /** CALL METHOD UNDER TEST **/
        uut.leaveTeam(groupMock);

        verify(serviceFacadeMock).leaveTeam(eq(groupMock), updateMemberCaptor.capture());

        updateMemberCaptor.getValue().onSuccess(updateMemberResultListMock);

        verify(announcerMock).schedule(isA(IplantAnnouncementConfig.class));
        verify(viewMock).removeTeam(eq(groupMock));
    }

}
