package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;

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
public class ManageCollaboratorsPresenterTest {

    @Mock ManageCollaboratorsViewFactory factoryMock;
    @Mock GroupServiceFacade groupServiceFacadeMock;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock ManageCollaboratorsView viewMock;
    @Mock HandlerRegistration addCollabHandlerRegistrationMock;
    @Mock Group groupMock;
    @Mock List<Group> groupListMock;
    @Mock Collaborator collaboratorMock;
    @Mock List<Collaborator> collaboratorListMock;
    @Mock Widget viewWidgetMock;
    @Mock EventBus eventBusMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCallbackCaptor;

    private ManageCollaboratorsPresenter uut;

    @Before
    public void setUp() {
        when(factoryMock.create(ManageCollaboratorsView.MODE.MANAGE)).thenReturn(viewMock);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);

        uut = new ManageCollaboratorsPresenter(factoryMock,
                                               groupServiceFacadeMock);

        uut.collaboratorsUtil = collaboratorsUtilMock;
        uut.eventBus = eventBusMock;
        uut.view = viewMock;
        uut.addCollabHandlerRegistration = addCollabHandlerRegistrationMock;
    }

    @Test
    public void go() {
        HasOneWidget containerMock = mock(HasOneWidget.class);
        ManageCollaboratorsPresenter spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, ManageCollaboratorsView.MODE.MANAGE);

        verify(factoryMock).create(eq(ManageCollaboratorsView.MODE.MANAGE));
        verify(viewMock).addRemoveCollaboratorSelectedHandler(eq(spy));
        verify(spy).loadCurrentCollaborators();
        verify(spy).updateListView();
        verify(spy).addEventHandlers();
        verify(containerMock).setWidget(eq(viewWidgetMock));
    }

    @Test
    public void addAsCollaborators() {

        /** CALL METHOD UNDER TEST **/
        uut.addAsCollaborators(collaboratorListMock);

        verify(collaboratorsUtilMock).addCollaborators(eq(collaboratorListMock), voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(viewMock).addCollaborators(eq(collaboratorListMock));
    }

    @Test
    public void updateListView() {

        String searchTerm = "search";

        /** CALL METHOD UNDER TEST **/
        uut.updateListView(searchTerm);

        verify(groupServiceFacadeMock).getGroups(eq(searchTerm), groupListCallbackCaptor.capture());

        groupListCallbackCaptor.getValue().onSuccess(groupListMock);
        verify(viewMock).addCollabLists(groupListMock);

    }

    @Test
    public void onRemoveCollaboratorSelected() {
        RemoveCollaboratorSelected eventMock = mock(RemoveCollaboratorSelected.class);
        when(eventMock.getCollaborators()).thenReturn(collaboratorListMock);

        /** CALL METHOD UNDER TEST **/
        uut.onRemoveCollaboratorSelected(eventMock);
        verify(eventMock).getCollaborators();

        verify(collaboratorsUtilMock).removeCollaborators(eq(collaboratorListMock), voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(viewMock).removeCollaborators(eq(collaboratorListMock));
    }

    @Test
    public void loadCurrentCollaborators() {
        when(collaboratorsUtilMock.getCurrentCollaborators()).thenReturn(collaboratorListMock);

        /** CALL METHOD UNDER TEST **/
        uut.loadCurrentCollaborators();

        verify(viewMock).mask(anyString());
        verify(collaboratorsUtilMock).getCollaborators(voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(viewMock).unmask();
        verify(viewMock).loadData(eq(collaboratorListMock));
    }

    @Test
    public void setCurrentMode() {

        /** CALL METHOD UNDER TEST **/
        uut.setCurrentMode(ManageCollaboratorsView.MODE.SELECT);
        verify(viewMock).setMode(eq(ManageCollaboratorsView.MODE.SELECT));
    }


    @Test
    public void cleanup() {
        /** CALL METHOD UNDER TEST **/
        uut.cleanup();

        verify(addCollabHandlerRegistrationMock).removeHandler();
    }

}
