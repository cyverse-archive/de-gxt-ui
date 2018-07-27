package org.iplantc.de.notifications.client.presenter;

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
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.client.views.dialogs.ApproveJoinRequestDialog;
import org.iplantc.de.notifications.client.views.dialogs.DenyJoinRequestDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

public class JoinTeamRequestPresenter implements JoinTeamRequestView.Presenter {

    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private JoinTeamRequestView view;
    IsHideable requestDlg;
    PayloadTeam payloadTeam;
    NotificationMessage notificationMessage;
    JoinTeamRequestView.JoinTeamRequestAppearance appearance;

    @Inject AsyncProviderWrapper<ApproveJoinRequestDialog> approveRequestDlgProvider;
    @Inject AsyncProviderWrapper<DenyJoinRequestDialog> denyRequestDlgProvider;
    @Inject IplantAnnouncer announcer;
    @Inject EventBus eventBus;

    @Inject
    public JoinTeamRequestPresenter(JoinTeamRequestView view,
                                    JoinTeamRequestView.JoinTeamRequestAppearance appearance,
                                    GroupServiceFacade serviceFacade,
                                    GroupAutoBeanFactory factory) {
        this.view = view;
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
    }

    @Override
    public void go(NotificationMessage notificationMessage, PayloadTeam payloadTeam) {
        this.notificationMessage = notificationMessage;
        this.payloadTeam = payloadTeam;
        Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(payloadTeam));
        view.edit(this, sp);
    }

    @Override
    public void setViewDebugId(String baseID) {
        // view.asWidget().ensureDebugId(baseID + Notifications.JoinRequestIds.JOIN_REQUEST_VIEW);
    }

    @Override
    public void addMemberWithPrivilege(String privilegeType,
                                       ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback) {
        Group team = factory.getGroup().as();
        team.setName(payloadTeam.getTeamName());

        Subject member = factory.getSubject().as();
        member.setId(payloadTeam.getRequesterId());

        serviceFacade.addMembersToTeam(team, wrapSubjectInList(member), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                if (result != null && !result.isEmpty() && result.get(0).isSuccess()) {
                    addPrivilege(team,
                                 PrivilegeType.fromTypeString(privilegeType),
                                 callback,
                                 errorCallback);
                } else {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.addMemberFail(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
                }
            }
        });
    }

    void addPrivilege(Group team,
                      PrivilegeType privilegeType,
                      ReactSuccessCallback callback,
                      ReactErrorCallback errorCallback) {
        UpdatePrivilegeRequestList requestList = getUpdatePrivilegeRequestList(privilegeType);

        serviceFacade.updateTeamPrivileges(team, requestList, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(List<Privilege> result) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
                announcer.schedule(new IplantAnnouncementConfig(appearance.joinTeamSuccess(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
            }
        });
    }

    UpdatePrivilegeRequestList getUpdatePrivilegeRequestList(PrivilegeType privilegeType) {
        UpdatePrivilegeRequestList requestList = factory.getUpdatePrivilegeRequestList().as();

        UpdatePrivilegeRequest request = factory.getUpdatePrivilegeRequest().as();
        request.setSubjectId(payloadTeam.getRequesterId());

        List<PrivilegeType> privileges = Lists.newArrayList();
        if (privilegeType.equals(PrivilegeType.readOptin)) {
            privileges.add(PrivilegeType.read);
            privileges.add(PrivilegeType.optin);
        } else {
            privileges.add(privilegeType);
        }

        request.setPrivileges(privileges);

        requestList.setRequests(Lists.newArrayList(request));
        return requestList;
    }

    @Override
    public void denyRequest(String denyMessage,
                            ReactSuccessCallback callback,
                            ReactErrorCallback errorCallback) {
        Group team = factory.getGroup().as();
        team.setName(payloadTeam.getTeamName());

        HasMessage message = factory.getHasMessage().as();
        message.setMessage(denyMessage);

        serviceFacade.denyRequestToJoinTeam(team, message, payloadTeam.getRequesterId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
                }
            }

            @Override
            public void onSuccess(Void aVoid) {
                announcer.schedule(new IplantAnnouncementConfig(appearance.denyRequestSuccess(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
    }

    List<Subject> wrapSubjectInList(Subject member) {
        return Lists.newArrayList(member);
    }
}

