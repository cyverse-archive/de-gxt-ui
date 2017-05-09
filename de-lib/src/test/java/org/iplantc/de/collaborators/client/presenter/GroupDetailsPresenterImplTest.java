package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.GroupSaved;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class GroupDetailsPresenterImplTest {

    @Mock GroupDetailsView viewMock;
    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock GroupView.GroupViewAppearance appearanceMock;
    @Mock Group groupMock;
    @Mock Group newGroupMock;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock List<Collaborator> collaboratorListMock;
    @Mock HandlerManager handlerManagerMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Collaborator>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;

    private GroupDetailsPresenterImpl uut;

    @Before
    public void setUp() {

        when(factoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(newGroupMock);

        uut = new GroupDetailsPresenterImpl(viewMock,
                                            serviceFacadeMock,
                                            factoryMock,
                                            appearanceMock) {
            @Override
            HandlerManager ensureHandlers() {
                return handlerManagerMock;
            }
        };

    }

    @Test
    public void go_editGroup() {
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock, groupMock);

        verify(containerMock).setWidget(eq(viewMock));
        verify(serviceFacadeMock).getMembers(eq(groupMock), collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(collaboratorListMock);
        verify(viewMock).addMembers(eq(collaboratorListMock));
        verify(viewMock).edit(eq(groupMock));
    }

    @Test
    public void go_newGroup() {
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock, null);

        verify(containerMock).setWidget(eq(viewMock));
        verify(viewMock).edit(eq(newGroupMock));
    }

    @Test
    public void saveGroupSelected_newGroup() {
        when(viewMock.getGroup()).thenReturn(groupMock);
        GroupDetailsPresenterImpl spy = Mockito.spy(uut);
        spy.isNewGroup = true;

        /** CALL METHOD UNDER TEST **/
        spy.saveGroupSelected();

        verify(spy).addGroup(eq(groupMock));
    }

    @Test
    public void addGroup() {

        /** CALL METHOD UNDER TEST **/
        uut.addGroup(groupMock);
        verify(serviceFacadeMock).addGroup(eq(groupMock), groupCallbackCaptor.capture());
        groupCallbackCaptor.getValue().onSuccess(groupMock);

        verify(handlerManagerMock).fireEvent(isA(GroupSaved.class));

    }
}
