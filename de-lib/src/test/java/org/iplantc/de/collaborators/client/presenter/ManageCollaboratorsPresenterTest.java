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
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
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
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author aramsey 
 */
@RunWith(GwtMockitoTestRunner.class)
public class ManageCollaboratorsPresenterTest {

    @Mock ManageCollaboratorsViewFactory factoryMock;
    @Mock GroupAutoBeanFactory groupFactoryMock;
    @Mock GroupServiceFacade groupServiceFacadeMock;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock ManageCollaboratorsView viewMock;
    @Mock HandlerRegistration addCollabHandlerRegistrationMock;
    @Mock Group groupMock;
    @Mock List<Group> groupListMock;
    @Mock Subject subjectMock;
    @Mock List<Subject> subjectListMock;
    @Mock CollaboratorsServiceFacade collabServiceFacadeMock;
    @Mock Widget viewWidgetMock;
    @Mock EventBus eventBusMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock GroupView.GroupViewAppearance groupAppearanceMock;
    @Mock GroupDetailsDialog groupDetailsDialogMock;
    @Mock AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialogProvider;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock Stream<Group> groupStreamMock;
    @Mock Group defaultGroup;
    @Mock UpdateMemberResult updateResultMock;
    @Mock Stream<UpdateMemberResult> updateMemberResultStreamMock;
    @Mock List<UpdateMemberResult> updateMemberResultsMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Group>>> groupListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<GroupDetailsDialog>> groupDetailsDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateMemberCaptor;


    private ManageCollaboratorsPresenter uut;

    @Before
    public void setUp() {
        when(factoryMock.create(ManageCollaboratorsView.MODE.MANAGE)).thenReturn(viewMock);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);
        when(groupMock.getName()).thenReturn("name");
        when(groupFactoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(groupMock);
        when(defaultGroup.getName()).thenReturn(Group.DEFAULT_GROUP);
        when(groupFactoryMock.getDefaultGroup()).thenReturn(defaultGroup);

        uut = new ManageCollaboratorsPresenter(factoryMock,
                                               groupFactoryMock,
                                               groupServiceFacadeMock,
                                               collabServiceFacadeMock,
                                               groupAppearanceMock) {
            @Override
            String getCollaboratorNames(List<Subject> subjects) {
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
        verify(spy).updateListView();
        verify(spy).addEventHandlers();
        verify(viewMock).addDeleteGroupSelectedHandler(eq(spy));
        verify(containerMock).setWidget(eq(viewWidgetMock));
    }

    @Test
    public void addAsCollaborators() {
        when(updateMemberResultsMock.stream()).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.filter(any())).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.collect(any())).thenReturn(null);

        /** CALL METHOD UNDER TEST **/
        uut.addAsCollaborators(subjectListMock);

        verify(groupServiceFacadeMock).addMembers(eq(defaultGroup), eq(subjectListMock), updateMemberCaptor.capture());

        updateMemberCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(viewMock).addCollaborators(eq(subjectListMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void updateListView() {
        when(groupAppearanceMock.loadingMask()).thenReturn("loading");
        when(groupListMock.stream()).thenReturn(groupStreamMock);
        when(groupStreamMock.filter(any())).thenReturn(groupStreamMock);
        when(groupStreamMock.collect(any())).thenReturn(groupListMock);

        /** CALL METHOD UNDER TEST **/
        uut.updateListView();

        verify(groupServiceFacadeMock).getGroups(groupListCallbackCaptor.capture());

        groupListCallbackCaptor.getValue().onSuccess(groupListMock);
        verify(viewMock).addCollabLists(eq(groupListMock));

    }

    @Test
    public void onRemoveCollaboratorSelected() {
        RemoveCollaboratorSelected eventMock = mock(RemoveCollaboratorSelected.class);
        when(eventMock.getSubjects()).thenReturn(subjectListMock);
        when(updateMemberResultsMock.stream()).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.filter(any())).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.collect(any())).thenReturn(null);

        /** CALL METHOD UNDER TEST **/
        uut.onRemoveCollaboratorSelected(eventMock);
        verify(eventMock).getSubjects();

        verify(groupServiceFacadeMock).deleteMembers(eq(defaultGroup), eq(subjectListMock), updateMemberCaptor.capture());

        updateMemberCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(viewMock).removeCollaborators(eq(subjectListMock));
    }

    @Test
    public void loadCurrentCollaborators() {

        /** CALL METHOD UNDER TEST **/
        uut.loadCurrentCollaborators();

        verify(viewMock).maskCollaborators(anyString());
        verify(groupServiceFacadeMock).getMembers(eq(defaultGroup), collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(subjectListMock);
        verify(viewMock).unmaskCollaborators();
        verify(viewMock).loadData(eq(subjectListMock));
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
        when(groupAppearanceMock.groupDeleteSuccess(groupMock)).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.deleteGroup(groupMock);

        verify(groupServiceFacadeMock).deleteGroup(eq(groupMock), groupCallbackCaptor.capture());

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
