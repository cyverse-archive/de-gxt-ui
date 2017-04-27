package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.any;
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
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.collaborators.client.views.dialogs.GroupDetailsDialog;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

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
    @Mock GroupView.GroupViewAppearance groupAppearance;
    @Mock GroupDetailsDialog groupDetailsDialogMock;
    @Mock AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialogProvider;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Collaborator>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<GroupDetailsDialog>> groupDetailsDialogCaptor;


    private ManageCollaboratorsPresenter uut;

    @Before
    public void setUp() {
        when(factoryMock.create(ManageCollaboratorsView.MODE.MANAGE)).thenReturn(viewMock);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);

        uut = new ManageCollaboratorsPresenter(factoryMock,
                                               groupServiceFacadeMock,
                                               collabServiceFacadeMock,
                                               groupAppearance) {
            @Override
            String getCollaboratorNames(List<Collaborator> collaborators) {
                return "names";
            }
        };

        uut.collaboratorsUtil = collaboratorsUtilMock;
        uut.eventBus = eventBusMock;
        uut.view = viewMock;
        uut.addCollabHandlerRegistration = addCollabHandlerRegistrationMock;
        uut.announcer = announcerMock;
        uut.groupDetailsDialog = groupDetailsDialogProvider;
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
//        verify(spy).updateListView();
        verify(spy).addEventHandlers();
        verify(viewMock).addDeleteGroupSelectedHandler(eq(spy));
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
        /** CALL METHOD UNDER TEST **/
        uut.updateListView();

        verify(groupServiceFacadeMock).getGroups(groupListCallbackCaptor.capture());

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

        verify(viewMock).maskCollaborators(anyString());
        verify(collabServiceFacadeMock).getCollaborators(collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(collaboratorListMock);
        verify(viewMock).unmaskCollaborators();
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
    public void onDeleteGroupSelected() {
        when(groupMock.getName()).thenReturn("name");
        when(groupAppearance.groupDeleteSuccess(groupMock)).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.deleteGroup(groupMock);

        verify(groupServiceFacadeMock).deleteGroup(eq("name"), groupCallbackCaptor.capture());

        groupCallbackCaptor.getValue().onSuccess(groupMock);
        verify(viewMock).removeCollabList(eq(groupMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));

    }

    @Test
    public void onAddGroupSelected() {
        AddGroupSelected eventMock = mock(AddGroupSelected.class);

        /** CALL METHOD UNDER TEST **/
        uut.onAddGroupSelected(eventMock);
        verify(groupDetailsDialogProvider).get(groupDetailsDialogCaptor.capture());

        groupDetailsDialogCaptor.getValue().onSuccess(groupDetailsDialogMock);
        verify(groupDetailsDialogMock).show();
        verify(groupDetailsDialogMock).addGroupSavedHandler(any());
    }

    @Test
    public void onGroupNameSelected() {
        GroupNameSelected eventMock = mock(GroupNameSelected.class);
        when(eventMock.getGroup()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.onGroupNameSelected(eventMock);
        verify(groupDetailsDialogProvider).get(groupDetailsDialogCaptor.capture());

        groupDetailsDialogCaptor.getValue().onSuccess(groupDetailsDialogMock);
        verify(groupDetailsDialogMock).show(eq(groupMock));
    }


}
