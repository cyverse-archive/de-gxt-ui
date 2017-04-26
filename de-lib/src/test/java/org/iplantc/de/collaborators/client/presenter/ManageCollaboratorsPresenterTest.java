package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

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
    @Mock CollaboratorsServiceFacade collabServiceFacadeMock;
    @Mock Widget viewWidgetMock;
    @Mock EventBus eventBusMock;
    @Mock IplantAnnouncer announcerMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Collaborator>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCallbackCaptor;

    private ManageCollaboratorsPresenter uut;

    @Before
    public void setUp() {
        when(factoryMock.create(ManageCollaboratorsView.MODE.MANAGE)).thenReturn(viewMock);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);

        uut = new ManageCollaboratorsPresenter(factoryMock,
                                               groupServiceFacadeMock,
                                               collabServiceFacadeMock) {
            @Override
            String getCollaboratorNames(List<Collaborator> collaborators) {
                return "names";
            }

            @Override
            List<Group> getGroupList(Group result) {
                return groupListMock;
            }
        };

        uut.collaboratorsUtil = collaboratorsUtilMock;
        uut.eventBus = eventBusMock;
        uut.view = viewMock;
        uut.addCollabHandlerRegistration = addCollabHandlerRegistrationMock;
        uut.announcer = announcerMock;
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
        verify(viewMock).addAddGroupSelectedHandler(eq(spy));
        verify(containerMock).setWidget(eq(viewWidgetMock));
    }

    @Test
    public void addAsCollaborators() {

        /** CALL METHOD UNDER TEST **/
        uut.addAsCollaborators(collaboratorListMock);

        verify(collabServiceFacadeMock).addCollaborators(eq(collaboratorListMock), voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(viewMock).addCollaborators(eq(collaboratorListMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
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

        verify(collabServiceFacadeMock).removeCollaborators(eq(collaboratorListMock), voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(viewMock).removeCollaborators(eq(collaboratorListMock));
    }

    @Test
    public void loadCurrentCollaborators() {

        /** CALL METHOD UNDER TEST **/
        uut.loadCurrentCollaborators();

        verify(viewMock).mask(anyString());
        verify(collabServiceFacadeMock).getCollaborators(collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(collaboratorListMock);
        verify(viewMock).unmask();
        verify(viewMock).loadData(eq(collaboratorListMock));
        verify(eventBusMock).fireEvent(isA(CollaboratorsLoadedEvent.class));
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

    @Test
    public void onAddGroupSelected() {
        AddGroupSelected eventMock = mock(AddGroupSelected.class);
        when(eventMock.getGroup()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.onAddGroupSelected(eventMock);
        verify(groupServiceFacadeMock).addGroup(eq(groupMock), groupCallbackCaptor.capture());
        groupCallbackCaptor.getValue().onSuccess(groupMock);

        verify(viewMock).addCollabLists(eq(groupListMock));

    }

}
