package org.iplantc.de.teams.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class TeamSearchRpcProxyTest {

    @Mock TeamsView.TeamsViewAppearance appearanceMock;
    @Mock GroupServiceFacade searchFacadeMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock FilterPagingLoadConfig loadConfigMock;
    @Mock AsyncCallback<PagingLoadResult<Group>> callbackMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock List<FilterConfig> filterConfigsMock;
    @Mock FilterConfig filterConfigMock;
    @Mock List<Group> groupListMock;
    @Mock Group groupMock;
    @Mock PagingLoadResultBean<Group> loadResultMock;
    @Mock CollaboratorsServiceFacade collabServiceFacadeMock;
    @Mock List<String> creatorIdsMock;
    @Mock FastMap<Subject> creatorFastMapMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<FastMap<Subject>>> fastMapCaptor;

    TeamSearchRpcProxy uut;

    @Before
    public void setUp() {
        when(loadConfigMock.getFilters()).thenReturn(filterConfigsMock);
        when(filterConfigsMock.isEmpty()).thenReturn(false);
        when(filterConfigsMock.get(0)).thenReturn(filterConfigMock);
        when(filterConfigMock.getValue()).thenReturn("search");

        uut = new TeamSearchRpcProxy(appearanceMock,
                                     searchFacadeMock,
                                     collabServiceFacadeMock,
                                     announcerMock){
            @Override
            PagingLoadResultBean<Group> getLoadResult(List<Group> result,
                                                      FilterPagingLoadConfig loadConfig) {
                return loadResultMock;
            }

            @Override
            List<String> getCreatorIds(List<Group> teams) {
                return creatorIdsMock;
            }
        };
        uut.handlerManager = handlerManagerMock;
        uut.lastQueryText = "";
    }

    @Test
    public void load() {
        TeamSearchRpcProxy spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.load(loadConfigMock, callbackMock);

        verify(searchFacadeMock).searchTeams(eq("search"),
                                             groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);

        verify(spy).getTeamCreatorNames(eq(groupListMock), eq(loadConfigMock), eq(callbackMock));
    }

    @Test
    public void getTeamCreatorNames() {
        TeamSearchRpcProxy spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.getTeamCreatorNames(groupListMock, loadConfigMock, callbackMock);

        verify(collabServiceFacadeMock).getUserInfo(eq(creatorIdsMock), fastMapCaptor.capture());
        fastMapCaptor.getValue().onSuccess(creatorFastMapMock);
        verify(spy).addCreatorToTeams(eq(groupListMock), eq(creatorFastMapMock));
        verify(spy).sendResults(eq(groupListMock), eq(loadConfigMock), eq(callbackMock));

    }

}
