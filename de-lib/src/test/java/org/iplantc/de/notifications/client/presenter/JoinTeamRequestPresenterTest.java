package org.iplantc.de.notifications.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasMessage;
import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequest;
import org.iplantc.de.client.models.groups.UpdatePrivilegeRequestList;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.notifications.client.events.JoinTeamApproved;
import org.iplantc.de.notifications.client.events.JoinTeamDenied;
import org.iplantc.de.notifications.client.events.JoinTeamRequestProcessed;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.client.views.dialogs.ApproveJoinRequestDialog;
import org.iplantc.de.notifications.client.views.dialogs.DenyJoinRequestDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class JoinTeamRequestPresenterTest {

    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock JoinTeamRequestView.JoinTeamRequestAppearance appearanceMock;
    @Mock JoinTeamRequestView viewMock;
    @Mock IsHideable requestDlgMock;
    @Mock PayloadTeam payloadTeamMock;
    @Mock NotificationMessage notificationMessageMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock EventBus eventBusMock;
    @Mock AsyncProviderWrapper<ApproveJoinRequestDialog> approveRequestDlgProviderMock;
    @Mock AsyncProviderWrapper<DenyJoinRequestDialog> denyRequestDlgProviderMock;
    @Mock ApproveJoinRequestDialog approveRequestDlgMock;
    @Mock DenyJoinRequestDialog denyRequestDlgMock;
    @Mock HasOneWidget containerMock;
    @Mock DialogHideEvent hideEventMock;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock AutoBean<Subject> subjectAutoBeanMock;
    @Mock Group groupMock;
    @Mock Subject subjectMock;
    @Mock List<Subject> subjectListMock;
    @Mock List<UpdateMemberResult> updateMemberResultListMock;
    @Mock UpdateMemberResult updateMemberResultMock;
    @Mock UpdatePrivilegeRequestList updatePrivilegeRequestListMock;
    @Mock List<Privilege> privilegeListMock;
    @Mock AutoBean<UpdatePrivilegeRequestList> updatePrivilegeRequestListAutoBeanMock;
    @Mock UpdatePrivilegeRequest updatePrivilegeRequestMock;
    @Mock AutoBean<UpdatePrivilegeRequest> updatePrivilegeRequestAutoBeanMock;
    @Mock HasMessage hasMessageMock;
    @Mock AutoBean<HasMessage> hasMessageAutoBeanMock;

    @Captor ArgumentCaptor<SelectEvent.SelectHandler> selectHandlerCaptor;
    @Captor ArgumentCaptor<AsyncCallback<ApproveJoinRequestDialog>> approveRequestDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<DenyJoinRequestDialog>> denyRequestDlgCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> hideDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateResultCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Privilege>>> privilegeListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;

    JoinTeamRequestPresenter uut;

    @Before
    public void setUp() {
        when(payloadTeamMock.getTeamName()).thenReturn("team");
        when(payloadTeamMock.getRequesterName()).thenReturn("name");
        when(payloadTeamMock.getRequesterId()).thenReturn("id");
        when(factoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(groupMock);
        when(factoryMock.getSubject()).thenReturn(subjectAutoBeanMock);
        when(subjectAutoBeanMock.as()).thenReturn(subjectMock);
        when(factoryMock.getHasMessage()).thenReturn(hasMessageAutoBeanMock);
        when(hasMessageAutoBeanMock.as()).thenReturn(hasMessageMock);

        uut = new JoinTeamRequestPresenter(viewMock,
                                           serviceFacadeMock,
                                           factoryMock,
                                           appearanceMock) {
            @Override
            List<Subject> wrapSubjectInList(Subject member) {
                return subjectListMock;
            }

            @Override
            UpdatePrivilegeRequestList getUpdatePrivilegeRequestList(PrivilegeType privilegeType) {
                return updatePrivilegeRequestListMock;
            }
        };
        uut.requestDlg = requestDlgMock;
        uut.payloadTeam = payloadTeamMock;
        uut.notificationMessage = notificationMessageMock;
        uut.announcer = announcerMock;
        uut.eventBus = eventBusMock;
        uut.denyRequestDlgProvider = denyRequestDlgProviderMock;
        uut.approveRequestDlgProvider = approveRequestDlgProviderMock;

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(viewMock).addJoinTeamDeniedHandler(eq(uut));
        verify(viewMock).addJoinTeamApprovedHandler(eq(uut));
    }

    @Test
    public void go() {

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock, requestDlgMock, notificationMessageMock, payloadTeamMock);
        verify(containerMock).setWidget(eq(viewMock));
        verify(viewMock).edit(eq(payloadTeamMock));
    }

    @Test
    public void onJoinTeamApproved() {
        JoinTeamApproved eventMock = mock(JoinTeamApproved.class);
        JoinTeamRequestPresenter spy = spy(uut);
        when(hideEventMock.getHideButton()).thenReturn(Dialog.PredefinedButton.OK);
        when(approveRequestDlgMock.getPrivilegeType()).thenReturn(PrivilegeType.read);

        /** CALL METHOD UNDER TEST **/
        spy.onJoinTeamApproved(eventMock);

        verify(approveRequestDlgProviderMock).get(approveRequestDlgCaptor.capture());

        approveRequestDlgCaptor.getValue().onSuccess(approveRequestDlgMock);
        verify(approveRequestDlgMock).show(eq("name"), eq("team"));
        verify(approveRequestDlgMock).addDialogHideHandler(hideDlgCaptor.capture());

        hideDlgCaptor.getValue().onDialogHide(hideEventMock);
        verify(spy).addMemberWithPrivilege(eq(PrivilegeType.read), eq(approveRequestDlgMock));
    }

    @Test
    public void addMemberWithPrivilege() {
        IsHideable hideableMock = mock(IsHideable.class);
        when(updateMemberResultListMock.isEmpty()).thenReturn(false);
        when(updateMemberResultListMock.get(0)).thenReturn(updateMemberResultMock);
        when(updateMemberResultMock.isSuccess()).thenReturn(true);

        JoinTeamRequestPresenter spy = spy(uut);
        /** CALL METHOD UNDER TEST **/
        spy.addMemberWithPrivilege(PrivilegeType.read, hideableMock);

        verify(groupMock).setName(eq("team"));
        verify(subjectMock).setId(eq("id"));

        verify(serviceFacadeMock).addMembersToTeam(eq(groupMock), eq(subjectListMock), updateResultCaptor.capture());

        updateResultCaptor.getValue().onSuccess(updateMemberResultListMock);
        verify(spy).addPrivilege(eq(groupMock), eq(PrivilegeType.read), eq(hideableMock));
    }

    @Test
    public void addPrivilege() {
        IsHideable hideableMock = mock(IsHideable.class);
        when(appearanceMock.joinTeamSuccess(any(), any())).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.addPrivilege(groupMock, PrivilegeType.read, hideableMock);

        verify(serviceFacadeMock).updateTeamPrivileges(eq(groupMock), eq(updatePrivilegeRequestListMock), privilegeListCaptor.capture());

        privilegeListCaptor.getValue().onSuccess(privilegeListMock);
        verify(announcerMock).schedule(isA(IplantAnnouncementConfig.class));
        verify(requestDlgMock).hide();
        verify(hideableMock).hide();
        verify(eventBusMock).fireEvent(isA(JoinTeamRequestProcessed.class));
    }

    @Test
    public void onJoinTeamDenied() {
        JoinTeamDenied eventMock = mock(JoinTeamDenied.class);
        JoinTeamRequestPresenter spy = spy(uut);
        SelectEvent selectEventMock = mock(SelectEvent.class);
        when(denyRequestDlgMock.getDenyMessage()).thenReturn("denied");

        /** CALL METHOD UNDER TEST **/
        spy.onJoinTeamDenied(eventMock);
        verify(denyRequestDlgProviderMock).get(denyRequestDlgCaptor.capture());

        denyRequestDlgCaptor.getValue().onSuccess(denyRequestDlgMock);
        verify(denyRequestDlgMock).show(eq("name"), eq("team"));
        verify(denyRequestDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());

        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(spy).denyRequest(eq("denied"), eq(denyRequestDlgMock));
    }

    @Test
    public void denyRequest() {
        IsHideable hideableMock = mock(IsHideable.class);
        when(appearanceMock.denyRequestSuccess("name", "team")).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.denyRequest("denied", hideableMock);

        verify(groupMock).setName(eq("team"));
        verify(hasMessageMock).setMessage(eq("denied"));

        verify(serviceFacadeMock).denyRequestToJoinTeam(eq(groupMock), eq(hasMessageMock), eq("id"), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);
        verify(announcerMock).schedule(isA(IplantAnnouncementConfig.class));
        verify(requestDlgMock).hide();
        verify(hideableMock).hide();
        verify(eventBusMock).fireEvent(isA(JoinTeamRequestProcessed.class));
    }

}
