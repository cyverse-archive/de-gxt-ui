package org.iplantc.de.apps.client.presenter.communities;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppNavigationView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.gin.factory.CommunitiesViewFactory;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

@RunWith(GxtMockitoTestRunner.class)
public class CommunitiesPresenterImplTest {

    @Mock GroupServiceFacade groupServiceFacadeMock;
    @Mock CommunitiesView.Appearance appearanceMock;
    @Mock AppNavigationView appNavigationViewMock;
    @Mock CommunitiesView viewMock;
    @Mock CommunitiesViewFactory viewFactoryMock;
    @Mock TreeStore<Group> treeStoreMock;
    @Mock List<Group> communitiesListMock;
    @Mock Tree<Group, String> treeMock;
    @Mock Group communityMock;
    @Mock TreeSelectionModel<Group> communitySelectionModelMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCaptor;

    CommunitiesPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.create(any())).thenReturn(viewMock);
        when(viewMock.getTree()).thenReturn(treeMock);
        when(appNavigationViewMock.getWidgetCount()).thenReturn(1);
        when(appearanceMock.communities()).thenReturn("Communities");
        when(treeMock.getStore()).thenReturn(treeStoreMock);
        when(treeMock.getSelectionModel()).thenReturn(communitySelectionModelMock);

        uut = new CommunitiesPresenterImpl(viewFactoryMock,
                                           groupServiceFacadeMock,
                                           appearanceMock);

        uut.view = viewMock;
        uut.appNavigationView = appNavigationViewMock;
    }

    @Test
    public void go() {
        CommunitiesPresenterImpl spy = spy(uut);
        spy.go(null, appNavigationViewMock);

        verify(groupServiceFacadeMock).getCommunities(groupListCaptor.capture());

        groupListCaptor.getValue().onSuccess(communitiesListMock);
        verify(spy).createView();
        verify(treeMock).mask(anyString());
        verify(spy).addCommunitiesToTree(eq(communitiesListMock));
        verify(spy).selectDesiredCommunity(eq(null));
        verify(treeMock).unmask();
    }

    @Test
    public void selectDesiredCommunity() {
        HasId hasIdMock = mock(HasId.class);
        when(hasIdMock.getId()).thenReturn("myId");
        when(treeStoreMock.findModelWithKey("myId")).thenReturn(communityMock);

        uut.selectDesiredCommunity(hasIdMock);

        verify(appNavigationViewMock).setActiveWidget(eq(treeMock));
        verify(communitySelectionModelMock).select(eq(communityMock), anyBoolean());
    }
}