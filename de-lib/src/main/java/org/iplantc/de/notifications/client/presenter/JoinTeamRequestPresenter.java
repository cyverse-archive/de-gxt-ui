package org.iplantc.de.notifications.client.presenter;

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
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.notifications.client.events.JoinTeamApproved;
import org.iplantc.de.notifications.client.events.JoinTeamDenied;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.client.views.dialogs.DenyJoinRequestDialog;
import org.iplantc.de.notifications.client.views.dialogs.SetMemberPrivilegeDialog;
import org.iplantc.de.notifications.shared.Notifications;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;

public class JoinTeamRequestPresenter implements JoinTeamRequestView.Presenter,
                                                 JoinTeamApproved.JoinTeamApprovedHandler,
                                                 JoinTeamDenied.JoinTeamDeniedHandler {

    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private JoinTeamRequestView.JoinTeamRequestAppearance appearance;
    private JoinTeamRequestView view;
    private IsHideable requestDlg;
    private PayloadTeam payloadTeam;

    @Inject AsyncProviderWrapper<SetMemberPrivilegeDialog> setPrivilegeDlgProvider;
    @Inject AsyncProviderWrapper<DenyJoinRequestDialog> denyRequestDlgProvider;
    @Inject IplantAnnouncer announcer;

    @Inject
    public JoinTeamRequestPresenter(JoinTeamRequestView view,
                                    GroupServiceFacade serviceFacade,
                                    GroupAutoBeanFactory factory,
                                    JoinTeamRequestView.JoinTeamRequestAppearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;

        view.addJoinTeamApprovedHandler(this);
        view.addJoinTeamDeniedHandler(this);
    }

    @Override
    public void go(HasOneWidget container, IsHideable requestDlg, PayloadTeam payloadTeam) {
        this.requestDlg = requestDlg;
        this.payloadTeam = payloadTeam;

        container.setWidget(view);
        view.edit(payloadTeam);
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.asWidget().ensureDebugId(baseID + Notifications.JoinRequestIds.JOIN_REQUEST_VIEW);
    }

    @Override
    public void onJoinTeamApproved(JoinTeamApproved event) {
        setPrivilegeDlgProvider.get(new AsyncCallback<SetMemberPrivilegeDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(SetMemberPrivilegeDialog dialog) {
                dialog.show(payloadTeam.getRequesterName(), payloadTeam.getTeamName());
                dialog.addDialogHideHandler(event -> {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.OK) && dialog.getPrivilegeType() != null) {
                        addMemberWithPrivilege(dialog.getPrivilegeType(), dialog);
                    }
                });
            }
        });
    }

    void addMemberWithPrivilege(PrivilegeType privilegeType, IsHideable privilegeDlg) {
        Group team = factory.getGroup().as();
        team.setName(payloadTeam.getTeamName());

        Subject member = factory.getSubject().as();
        member.setId(payloadTeam.getRequesterId());

        serviceFacade.addMembersToTeam(team, Lists.newArrayList(member), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                if (result != null && !result.isEmpty() && result.get(0).isSuccess()) {
                    addPrivilege(team, privilegeType, privilegeDlg);
                } else {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.addMemberFail(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
                }
            }
        });
    }

    void addPrivilege(Group team, PrivilegeType privilegeType, IsHideable privilegeDlg) {
        UpdatePrivilegeRequestList requestList = getUpdatePrivilegeRequestList(privilegeType);

        serviceFacade.updateTeamPrivileges(team, requestList, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Privilege> result) {
                announcer.schedule(new IplantAnnouncementConfig(appearance.joinTeamSuccess(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
                requestDlg.hide();
                privilegeDlg.hide();
            }
        });
    }

    UpdatePrivilegeRequestList getUpdatePrivilegeRequestList(PrivilegeType privilegeType) {
        UpdatePrivilegeRequestList requestList = factory.getUpdatePrivilegeRequestList().as();

        UpdatePrivilegeRequest request = factory.getUpdatePrivilegeRequest().as();
        request.setSubjectId(payloadTeam.getRequesterId());
        request.setPrivileges(Lists.newArrayList(privilegeType));

        requestList.setRequests(Lists.newArrayList(request));
        return requestList;
    }

    @Override
    public void onJoinTeamDenied(JoinTeamDenied event) {
        denyRequestDlgProvider.get(new AsyncCallback<DenyJoinRequestDialog>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(DenyJoinRequestDialog dialog) {
                dialog.show(payloadTeam.getRequesterName(), payloadTeam.getTeamName());
                dialog.addOkButtonSelectHandler(event -> {
                    denyRequest(dialog.getDenyMessage(), dialog);
                });
            }
        });
    }

    void denyRequest(String denyMessage, IsHideable hideable) {
        Group team = factory.getGroup().as();
        team.setName(payloadTeam.getTeamName());

        HasMessage message = factory.getHasMessage().as();
        message.setMessage(denyMessage);

        serviceFacade.denyRequestToJoinTeam(team, message, payloadTeam.getRequesterId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                announcer.schedule(new IplantAnnouncementConfig(appearance.denyRequestSuccess(payloadTeam.getRequesterName(), payloadTeam.getTeamName())));
                requestDlg.hide();
                hideable.hide();
            }
        });
    }
}
