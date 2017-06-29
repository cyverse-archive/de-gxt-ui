package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.collaborators.client.views.dialogs.GroupDetailsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
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
import java.util.Map;
import java.util.function.Consumer;
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
    @Mock ManageCollaboratorsView.Appearance groupAppearanceMock;
    @Mock GroupDetailsDialog groupDetailsDialogMock;
    @Mock AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialogProvider;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock Stream<Group> groupStreamMock;
    @Mock Group defaultGroup;
    @Mock UpdateMemberResult updateResultMock;
    @Mock Stream<UpdateMemberResult> updateMemberResultStreamMock;
    @Mock List<UpdateMemberResult> updateMemberResultsMock;
    @Mock UpdateMemberResult updateMemberResultMock;
    @Mock Consumer<Group> groupConsumerMock;
    @Mock UserInfo userInfoMock;
    @Mock ManageCollaboratorsPresenter.AddMemberToGroupCallback memberToGroupCallbackMock;
    @Mock List<ManageCollaboratorsPresenter.AddMemberToGroupCallback> memberToGroupCallbackListsMock;
    @Mock Map<Boolean, List<Subject>> mapIsGroupMock;
    @Mock Map<Boolean, List<UpdateMemberResult>> mapIsSuccessMock;
    @Mock ManageCollaboratorsPresenter.ParentDeleteSubjectsCallback parentCallbackMock;
    @Mock List<Throwable> throwablesMock;
    @Mock List<String> stringListMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> subjectListCallbackConverter;
    @Captor ArgumentCaptor<AsyncCallback<GroupDetailsDialog>> groupDetailsDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateMemberCaptor;
    @Captor ArgumentCaptor<Consumer<Group>> groupConsumerCaptor;
    @Captor ArgumentCaptor<Consumer<Subject>> subjectConsumerCaptor;
    @Captor ArgumentCaptor<ManageCollaboratorsPresenter.ParentDeleteSubjectsCallback> parentCallbackCaptor;


    private ManageCollaboratorsPresenter uut;
    private ManageCollaboratorsPresenter.ParentDeleteSubjectsCallback parentCallback;

    @Before
    public void setUp() {
        when(factoryMock.create(ManageCollaboratorsView.MODE.MANAGE)).thenReturn(viewMock);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);
        when(groupMock.getName()).thenReturn("name");
        when(groupFactoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(groupMock);
        when(defaultGroup.getName()).thenReturn(Group.DEFAULT_GROUP);
        when(groupFactoryMock.getDefaultGroup()).thenReturn(defaultGroup);
        when(groupAppearanceMock.loadingMask()).thenReturn("loading");

        uut = new ManageCollaboratorsPresenter(factoryMock,
                                               groupFactoryMock,
                                               groupServiceFacadeMock,
                                               collabServiceFacadeMock,
                                               groupAppearanceMock) {
            @Override
            String getCollaboratorNames(List<Subject> subjects) {
                return "names";
            }

            @Override
            List<Subject> wrapSubjectInList(Subject subject) {
                return subjectListMock;
            }

            @Override
            List<AddMemberToGroupCallback> createAddMemberToGroupCallbackList() {
                return memberToGroupCallbackListsMock;
            }

            @Override
            List<Subject> excludeDefaultGroup(List<Subject> result) {
                return subjectListMock;
            }

            @Override
            Map<Boolean, List<Subject>> mapIsGroup(List<Subject> models) {
                return mapIsGroupMock;
            }

            @Override
            Map<Boolean, List<UpdateMemberResult>> mapIsSuccessResults(List<UpdateMemberResult> totalResults) {
                return mapIsSuccessMock;
            }

            @Override
            ParentDeleteSubjectsCallback createParentDeleteSubjectsCallback() {
                return parentCallbackMock;
            }

            @Override
            String getSubjectNames(List<UpdateMemberResult> userSuccesses, List<Group> groups) {
                return "names";
            }

            @Override
            List<String> getCollaboratorIds(List<UpdateMemberResult> userSuccesses,
                                            List<Group> successGroups) {
                return stringListMock;
            }

            @Override
            AddMemberToGroupCallback createAddMemberToGroupCallback() {
                return memberToGroupCallbackMock;
            }
        };

        uut.collaboratorsUtil = collaboratorsUtilMock;
        uut.eventBus = eventBusMock;
        uut.view = viewMock;
        uut.addCollabHandlerRegistration = addCollabHandlerRegistrationMock;
        uut.announcer = announcerMock;
        uut.groupDetailsDialog = groupDetailsDialogProvider;
        uut.userInfo = userInfoMock;

        parentCallback = uut.new ParentDeleteSubjectsCallback();
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
        verify(spy).getGroups();
        verify(spy).addEventHandlers();
        verify(containerMock).setWidget(eq(viewWidgetMock));
    }

    @Test
    public void addEventHandlers() {

        /** CALL METHOD UNDER TEST **/
        uut.addEventHandlers();

        verify(viewMock).addAddGroupSelectedHandler(eq(uut));
        verify(viewMock).addGroupNameSelectedHandler(eq(uut));
        verify(viewMock).addUserSearchResultSelectedEventHandler(eq(uut));
        verify(viewMock).addRemoveCollaboratorSelectedHandler(eq(uut));
    }

    @Test
    public void addAsCollaborators() {
        when(updateMemberResultsMock.isEmpty()).thenReturn(true);
        when(groupAppearanceMock.collaboratorAddConfirm(any())).thenReturn("success");
        when(mapIsSuccessMock.get(anyBoolean())).thenReturn(updateMemberResultsMock);

        /** CALL METHOD UNDER TEST **/
        uut.addAsCollaborators(subjectListMock);

        verify(groupServiceFacadeMock).addMembers(eq(defaultGroup), eq(subjectListMock), updateMemberCaptor.capture());

        updateMemberCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(viewMock).addCollaborators(eq(subjectListMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void getGroups() {
        when(groupAppearanceMock.loadingMask()).thenReturn("loading");
        when(groupListMock.stream()).thenReturn(groupStreamMock);
        when(groupStreamMock.filter(any())).thenReturn(groupStreamMock);
        when(groupStreamMock.collect(any())).thenReturn(groupListMock);

        /** CALL METHOD UNDER TEST **/
        uut.getGroups();

        verify(groupServiceFacadeMock).getGroups(subjectListCallbackConverter.capture());

        subjectListCallbackConverter.getValue().onSuccess(subjectListMock);
        verify(viewMock).addCollaborators(eq(subjectListMock));
        verify(viewMock).unmaskCollaborators();
    }

    @Test
    public void onRemoveCollaboratorSelected() {
        RemoveCollaboratorSelected eventMock = mock(RemoveCollaboratorSelected.class);
        ManageCollaboratorsPresenter spy = spy(uut);
        when(eventMock.getSubjects()).thenReturn(subjectListMock);
        when(mapIsGroupMock.get(anyBoolean())).thenReturn(subjectListMock);
        when(subjectListMock.isEmpty()).thenReturn(true);
        when(groupAppearanceMock.deleteGroupConfirm(any())).thenReturn("delete");
        when(groupAppearanceMock.deleteGroupConfirmHeading(any())).thenReturn("deleteHeader");

        /** CALL METHOD UNDER TEST **/
        spy.onRemoveCollaboratorSelected(eventMock);
        verify(eventMock).getSubjects();

        verify(spy).removeCollaborators(any(), eq(subjectListMock));
    }

    @Test
    public void removeCollaborators() {
        ManageCollaboratorsPresenter spy = spy(uut);
        when(spy.getCallbackSize(any(), any())).thenReturn(2);
        when(groupAppearanceMock.memberDeleteFail(any())).thenReturn("fail");
        when(groupAppearanceMock.collaboratorRemoveConfirm(anyString())).thenReturn("removed");
        when(updateMemberResultMock.getSubjectName()).thenReturn("name");

        /** CALL METHOD UNDER TEST **/
        spy.removeCollaborators(subjectListMock, subjectListMock);
        verify(viewMock).maskCollaborators(eq("loading"));
        verify(parentCallbackMock).setCallbackCounter(eq(2));
        verify(spy).removeCollaboratorsFromDefault(eq(subjectListMock), eq(parentCallbackMock));
        verify(spy).deleteGroups(eq(subjectListMock), eq(parentCallbackMock));
    }

    @Test
    public void publicDeleteSubjectsCallback_noFailures() {
        when(throwablesMock.isEmpty()).thenReturn(true);
        when(updateMemberResultsMock.isEmpty()).thenReturn(true);
        when(groupAppearanceMock.collaboratorRemoveConfirm(anyString())).thenReturn("confirmed");
        when(mapIsSuccessMock.get(anyBoolean())).thenReturn(updateMemberResultsMock);

        /** CALL METHOD UNDER TEST **/
        parentCallback.whenDone(updateMemberResultsMock, groupListMock, throwablesMock);

        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(viewMock).removeCollaboratorsById(stringListMock);
        verify(viewMock).unmaskCollaborators();
    }

    @Test
    public void publicDeleteSubjectsCallback_withFailures() {
        when(throwablesMock.isEmpty()).thenReturn(true);
        when(updateMemberResultsMock.isEmpty()).thenReturn(false);
        when(groupAppearanceMock.collaboratorRemoveConfirm(anyString())).thenReturn("removed");
        when(groupAppearanceMock.memberDeleteFail(any())).thenReturn("fail");
        when(mapIsSuccessMock.get(anyBoolean())).thenReturn(updateMemberResultsMock);

        /** CALL METHOD UNDER TEST **/
        parentCallback.whenDone(updateMemberResultsMock, groupListMock, throwablesMock);

        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(viewMock).removeCollaboratorsById(stringListMock);
        verify(viewMock).unmaskCollaborators();
    }

    @Test
    public void deleteGroups() {
        when(groupFactoryMock.convertSubjectToGroup(any())).thenReturn(groupMock);
        when(subjectListMock.isEmpty()).thenReturn(false);

        /**CALL METHOD UNDER TEST **/
        uut.deleteGroups(subjectListMock, parentCallbackMock);

        verify(subjectListMock).forEach(subjectConsumerCaptor.capture());
        subjectConsumerCaptor.getValue().accept(subjectMock);
        verify(groupServiceFacadeMock).deleteGroup(eq(groupMock), isA(ManageCollaboratorsPresenter.DeleteGroupChildCallback.class));
    }

    @Test
    public void removeCollaboratorsFromDefault() {
        when(groupFactoryMock.getDefaultGroup()).thenReturn(defaultGroup);
        when(subjectListMock.isEmpty()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        uut.removeCollaboratorsFromDefault(subjectListMock, parentCallbackMock);
        verify(groupServiceFacadeMock).deleteMembers(eq(defaultGroup),
                                                     eq(subjectListMock),
                                                     isA(ManageCollaboratorsPresenter.DeleteUsersChildCallback.class));
    }

    @Test
    public void loadCurrentCollaborators() {

        /** CALL METHOD UNDER TEST **/
        uut.loadCurrentCollaborators();

        verify(viewMock).maskCollaborators(anyString());
        verify(groupServiceFacadeMock).getMembers(eq(defaultGroup), collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(subjectListMock);
        verify(viewMock).unmaskCollaborators();
        verify(viewMock).addCollaborators(eq(subjectListMock));
        verify(eventBusMock).fireEvent(isA(CollaboratorsLoadedEvent.class));
    }

    @Test
    public void setCurrentMode() {

        /** CALL METHOD UNDER TEST **/
        uut.setCurrentMode(ManageCollaboratorsView.MODE.SELECT);
        verify(viewMock).setMode(eq(ManageCollaboratorsView.MODE.SELECT));
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
        when(eventMock.getSubject()).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.onGroupNameSelected(eventMock);
        verify(groupDetailsDialogProvider).get(groupDetailsDialogCaptor.capture());

        groupDetailsDialogCaptor.getValue().onSuccess(groupDetailsDialogMock);
        verify(groupDetailsDialogMock).show(eq(groupMock));
    }

    @Test
    public void addMemberToGroups() {
        when(subjectListMock.size()).thenReturn(1);
        when(updateMemberResultsMock.isEmpty()).thenReturn(true);
        when(groupAppearanceMock.memberAddToGroupsSuccess(any())).thenReturn("success");
        when(groupAppearanceMock.unableToAddMembers(any())).thenReturn("fail");
        when(groupFactoryMock.convertSubjectToGroup(any())).thenReturn(groupMock);

        /** CALL METHOD UNDER TEST **/
        uut.addMemberToGroups(subjectMock, subjectListMock);

        verify(subjectListMock).forEach(subjectConsumerCaptor.capture());
        subjectConsumerCaptor.getValue().accept(subjectMock);

        verify(groupServiceFacadeMock).addMembers(eq(groupMock), eq(subjectListMock), updateMemberCaptor.capture());

        updateMemberCaptor.getValue().onSuccess(updateMemberResultsMock);
    }

    @Test
    public void onUserSearchResultSelected_selfAdd() {
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(subjectMock.getId()).thenReturn("id");
        when(userInfoMock.getUsername()).thenReturn("id");
        when(groupAppearanceMock.collaboratorsSelfAdd()).thenReturn("selfAdd");

        /** CALL METHOD UNDER TEST **/
        uut.onUserSearchResultSelected(eventMock);
        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
    }

    @Test
    public void onUserSearchResultSelected_alreadyAddedCollaborator() {
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(subjectMock.getId()).thenReturn("newID");
        when(userInfoMock.getUsername()).thenReturn("id");
        when(viewMock.getCollaborators()).thenReturn(subjectListMock);
        when(collaboratorsUtilMock.isCurrentCollaborator(any(), any())).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        uut.onUserSearchResultSelected(eventMock);

        verifyZeroInteractions(announcerMock);
    }

    @Test
    public void onUserSearchResultSelected_newCollaborator() {
        ManageCollaboratorsPresenter spy = spy(uut);
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(subjectMock.getId()).thenReturn("newID");
        when(userInfoMock.getUsername()).thenReturn("id");
        when(viewMock.getCollaborators()).thenReturn(subjectListMock);
        when(collaboratorsUtilMock.isCurrentCollaborator(any(), any())).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        spy.onUserSearchResultSelected(eventMock);

        verify(spy).addAsCollaborators(any());
    }

    @Test
    public void onUserSearchResultSelected_newCollaborator_quickAdd() {
        ManageCollaboratorsPresenter spy = spy(uut);
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(subjectMock.getId()).thenReturn("newID");
        when(userInfoMock.getUsername()).thenReturn("id");
        when(viewMock.getSelectedSubjects()).thenReturn(subjectListMock);
        when(subjectListMock.isEmpty()).thenReturn(false);
        when(mapIsGroupMock.get(anyBoolean())).thenReturn(subjectListMock);
        when(collaboratorsUtilMock.isCurrentCollaborator(any(), any())).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        spy.onUserSearchResultSelected(eventMock);

        verify(spy).addMemberToGroups(eq(subjectMock), eq(subjectListMock));
    }

}
