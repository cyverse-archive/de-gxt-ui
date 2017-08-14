package org.iplantc.de.teams.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamSearchResultLoad;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

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

    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;

    TeamSearchRpcProxy uut;

    @Before
    public void setUp() {
        when(loadConfigMock.getFilters()).thenReturn(filterConfigsMock);
        when(filterConfigsMock.isEmpty()).thenReturn(false);
        when(filterConfigsMock.get(0)).thenReturn(filterConfigMock);
        when(filterConfigMock.getValue()).thenReturn("search");

        uut = new TeamSearchRpcProxy(appearanceMock,
                                     searchFacadeMock,
                                     announcerMock){
            @Override
            PagingLoadResultBean<Group> getLoadResult(List<Group> result,
                                                      FilterPagingLoadConfig loadConfig) {
                return loadResultMock;
            }
        };
        uut.handlerManager = handlerManagerMock;
        uut.lastQueryText = "";
    }

    @Test
    public void load() {

        /** CALL METHOD UNDER TEST **/
        uut.load(loadConfigMock, callbackMock);

        verify(searchFacadeMock).searchTeams(eq("search"),
                                             groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(groupListMock);

        verify(handlerManagerMock).fireEvent(isA(TeamSearchResultLoad.class));
        verify(callbackMock).onSuccess(eq(loadResultMock));
    }

}