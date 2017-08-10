package org.iplantc.de.teams.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequest;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.views.dialogs.SaveTeamProgressDialog;

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

import java.util.List;
import java.util.stream.Stream;

@RunWith(GwtMockitoTestRunner.class)
public class EditTeamPresenterImplTest {

    @Mock EditTeamView viewMock;
    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock TeamsView.TeamsViewAppearance appearanceMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock SaveTeamProgressDialog progressDlgMock;
    @Mock UserInfo userInfoMock;
    @Mock AsyncProviderWrapper<SaveTeamProgressDialog> progressDialogProviderMock;
    @Mock Group groupMock;
    @Mock AutoBean<Privilege> privilegeAutoBeanMock;
    @Mock Privilege privilegeMock;
    @Mock Subject subjectMock;
    @Mock AutoBean<Subject> subjectAutoBeanMock;
    @Mock List<PrivilegeType> privilegeTypeListMock;
    @Mock List<Privilege> memberPrivsMock;
    @Mock List<Privilege> nonMemberPrivsMock;
    @Mock List<Privilege> privilegeListMock;
    @Mock UpdatePrivilegeRequest updateRequestMock;
    @Mock List<UpdatePrivilegeRequest> listUpdateRequestMock;
    @Mock AutoBean<UpdatePrivilegeRequestList> updateRequestListAutoBeanMock;
    @Mock UpdatePrivilegeRequestList updateRequestListMock;
    @Mock Stream<Privilege> privilegeStreamMock;
    @Mock List<Subject> subjectListMock;
    @Mock Stream<Subject> subjectStreamMock;
    @Mock List<UpdateMemberResult> updateMemberResultListMock;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock DEProperties dePropertiesMock;

    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCaptor;
    @Captor ArgumentCaptor<AsyncCallback<SaveTeamProgressDialog>> progressDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateResultCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Privilege>>> privilegeListCaptor;

    EditTeamPresenterImpl uut;

    @Before
    public void setUp() {
        when(factoryMock.getPrivilege()).thenReturn(privilegeAutoBeanMock);
        when(privilegeAutoBeanMock.as()).thenReturn(privilegeMock);
        when(factoryMock.getSubject()).thenReturn(subjectAutoBeanMock);
        when(subjectAutoBeanMock.as()).thenReturn(subjectMock);
        when(factoryMock.getUpdatePrivilegeRequestList()).thenReturn(updateRequestListAutoBeanMock);
        when(updateRequestListAutoBeanMock.as()).thenReturn(updateRequestListMock);
        when(updateRequestListMock.getRequests()).thenReturn(listUpdateRequestMock);
        when(factoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(groupMock);
        when(appearanceMock.loadingMask()).thenReturn("loading");
        when(dePropertiesMock.getGrouperAllId()).thenReturn("GrouperAll");
        when(dePropertiesMock.getGrouperAllDisplayName()).thenReturn("All Public Users");

        uut = new EditTeamPresenterImpl(viewMock,
                                        serviceFacadeMock,
                                        factoryMock,
                                        appearanceMock,
                                        dePropertiesMock) {
            @Override
            List<PrivilegeType> getPublicUserPrivilege() {
                return privilegeTypeListMock;
            }

            @Override
            List<Privilege> createEmptyPrivilegeList() {
                return privilegeListMock;
            }

            @Override
            Subject createSelfSubject() {
                return subjectMock;
            }

            @Override
            List<Subject> getSubjectsFromPrivileges(List<Privilege> privileges) {
                return subjectListMock;
            }

            @Override
            HandlerManager createHandlerManager() {
                return handlerManagerMock;
            }

            @Override
            List<UpdatePrivilegeRequest> convertPrivilegesToUpdateRequest(List<Privilege> privileges) {
                return listUpdateRequestMock;
            }
        };
        uut.userInfo = userInfoMock;
        uut.progressDialogProvider = progressDialogProviderMock;
        uut.progressDlg = progressDlgMock;

        verifyConstructor();
    }

    public void verifyConstructor() {
        verify(viewMock).addUserSearchResultSelectedEventHandler(eq(uut));
        verify(viewMock).addRemoveMemberPrivilegeSelectedHandler(eq(uut));
        verify(viewMock).addRemoveNonMemberPrivilegeSelectedHandler(eq(uut));
        verify(viewMock).addAddPublicUserSelectedHandler(eq(uut));
    }

    @Test
    public void go_editMode() {
        HasOneWidget widgetMock = mock(HasOneWidget.class);
        EditTeamPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.go(widgetMock, groupMock);

        assertEquals(spy.mode, EditTeamView.MODE.EDIT);
        verify(viewMock).edit(eq(groupMock));
        verify(spy).addPublicUser();
    }

    @Test
    public void go_createMode() {
        HasOneWidget widgetMock = mock(HasOneWidget.class);
        EditTeamPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.go(widgetMock, null);

        assertEquals(spy.mode, EditTeamView.MODE.CREATE);
        verify(viewMock).edit(eq(groupMock));
        verify(spy).addPublicUser();
    }

    @Test
    public void addPublicUser() {

        /** CALL METHOD UNDER TEST **/
        uut.addPublicUser();

        verify(privilegeMock).setSubject(eq(subjectMock));
        verify(privilegeMock).setPrivilegeType(eq(PrivilegeType.view));

        verify(viewMock).addNonMembers(anyList());
    }

    @Test
    public void saveTeamSelected_createMode() {
        IsHideable hideableMock = mock(IsHideable.class);
        EditTeamPresenterImpl spy = spy(uut);

        spy.mode = EditTeamView.MODE.CREATE;

        /** CALL METHOD UNDER TEST **/
        spy.saveTeamSelected(hideableMock);

        verify(progressDialogProviderMock).get(progressDialogCaptor.capture());

        progressDialogCaptor.getValue().onSuccess(progressDlgMock);
        verify(spy).createNewTeam(eq(hideableMock));
    }

    @Test
    public void createNewTeam() {
        IsHideable hideableMock = mock(IsHideable.class);
        EditTeamPresenterImpl spy = spy(uut);
        when(viewMock.getTeam()).thenReturn(groupMock);
        when(viewMock.getMemberPrivileges()).thenReturn(memberPrivsMock);
        when(viewMock.getNonMemberPrivileges()).thenReturn(nonMemberPrivsMock);

        /** CALL METHOD UNDER TEST **/
        spy.createNewTeam(hideableMock);

        verify(viewMock).mask(eq("loading"));

        verify(serviceFacadeMock).addTeam(eq(groupMock),
                                          eq(privilegeTypeListMock),
                                          groupCaptor.capture());

        groupCaptor.getValue().onSuccess(groupMock);
        verify(privilegeListMock).addAll(memberPrivsMock);
        verify(privilegeListMock).addAll(nonMemberPrivsMock);
        verify(spy).addPrivilegesToTeam(eq(groupMock), eq(privilegeListMock), eq(hideableMock));
    }

    @Test
    public void getPublicUserPrivilege() {
        when(nonMemberPrivsMock.stream()).thenReturn(privilegeStreamMock);
        when(privilegeStreamMock.filter(any())).thenReturn(privilegeStreamMock);
        when(privilegeStreamMock.collect(any())).thenReturn(nonMemberPrivsMock);
        when(nonMemberPrivsMock.isEmpty()).thenReturn(false);
        when(nonMemberPrivsMock.get(0)).thenReturn(privilegeMock);
        when(privilegeMock.getPrivilegeType()).thenReturn(PrivilegeType.read);

        uut.getPublicUserPrivilege();
    }

    @Test
    public void addMembersToTeam() {
        IsHideable hideableMock = mock(IsHideable.class);
        when(viewMock.getMemberPrivileges()).thenReturn(memberPrivsMock);

        /** CALL METHOD UNDER TEST **/
        uut.addMembersToTeam(groupMock, hideableMock);

        verify(subjectListMock).add(subjectMock);
        verify(serviceFacadeMock).addMembersToTeam(eq(groupMock),
                                                   eq(subjectListMock),
                                                   updateResultCaptor.capture());

        updateResultCaptor.getValue().onSuccess(updateMemberResultListMock);
        verify(hideableMock).hide();
        verify(viewMock).unmask();
    }

    @Test
    public void addPrivilegesToTeam() {
        IsHideable hideableMock = mock(IsHideable.class);
        EditTeamPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.addPrivilegesToTeam(groupMock, privilegeListMock, hideableMock);

        verify(updateRequestListMock).setRequests(listUpdateRequestMock);
        verify(serviceFacadeMock).updateTeamPrivileges(eq(groupMock),
                                                       eq(updateRequestListMock),
                                                       privilegeListCaptor.capture());

        privilegeListCaptor.getValue().onSuccess(privilegeListMock);
        verify(spy).addMembersToTeam(eq(groupMock),
                                     eq(hideableMock));
    }

    @Test
    public void onUserSearchResultSelected_members() {
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(eventMock.getTag()).thenReturn(EditTeamView.SEARCH_MEMBERS_TAG);

        /** CALL METHOD UNDER TEST **/
        uut.onUserSearchResultSelected(eventMock);

        verify(privilegeMock).setSubject(eq(subjectMock));
        verify(privilegeMock).setPrivilegeType(eq(PrivilegeType.read));

        verify(viewMock).addMembers(anyList());
    }

    @Test
    public void onUserSearchResultSelected_nonMembers() {
        UserSearchResultSelected eventMock = mock(UserSearchResultSelected.class);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(eventMock.getTag()).thenReturn(EditTeamView.SEARCH_NON_MEMBERS_TAG);

        /** CALL METHOD UNDER TEST **/
        uut.onUserSearchResultSelected(eventMock);

        verify(privilegeMock).setSubject(eq(subjectMock));
        verify(privilegeMock).setPrivilegeType(eq(PrivilegeType.read));

        verify(viewMock).addNonMembers(anyList());
    }
}
