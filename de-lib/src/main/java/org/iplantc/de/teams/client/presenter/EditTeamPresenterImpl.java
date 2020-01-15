package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.DeleteTeamCompleted;
import org.iplantc.de.teams.client.events.JoinTeamCompleted;
import org.iplantc.de.teams.client.events.LeaveTeamCompleted;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.gin.EditTeamViewFactory;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.List;

public class EditTeamPresenterImpl implements EditTeamView.Presenter {

    private EditTeamView view;
    private EditTeamViewFactory viewFactory;
    private GroupServiceFacade serviceFacade;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private IplantValidationConstants iplantValidationConstants;
    private DEProperties deProperties;
    private TeamsView.TeamsViewAppearance appearance;
    private CollaboratorsUtil collaboratorsUtil;
    private UserInfo userInfo;
    HandlerManager handlerManager;
    Group originalGroup;
    @Inject IplantAnnouncer announcer;

    @Inject
    public EditTeamPresenterImpl(EditTeamViewFactory viewFactory,
                                 GroupServiceFacade serviceFacade,
                                 CollaboratorsServiceFacade collaboratorsServiceFacade,
                                 CollaboratorsUtil collaboratorsUtil,
                                 IplantValidationConstants iplantValidationConstants,
                                 UserInfo userInfo,
                                 DEProperties deProperties,
                                 TeamsView.TeamsViewAppearance appearance) {
        this.viewFactory = viewFactory;
        this.serviceFacade = serviceFacade;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
        this.collaboratorsUtil = collaboratorsUtil;
        this.iplantValidationConstants = iplantValidationConstants;
        this.userInfo = userInfo;
        this.deProperties = deProperties;
        this.appearance = appearance;
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void go(Group group) {
        this.view = viewFactory.create(getBaseProps());
        view.mask();

        if (group != null) {
            getTeamPrivileges(group);
        } else {
            view.edit(null, null, null);
        }
    }

    @Override
    public void closeEditTeamDlg() {
        view.close();
    }

    void getTeamPrivileges(Group group) {
        serviceFacade.getTeamPrivileges(group, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable privileges) {
                if (privileges != null) {
                    privileges = privileges.get("privileges");
                } else {
                    privileges = StringQuoter.createIndexed();
                }
                getTeamMembers(group, privileges);
            }
        });
    }

    void getTeamMembers(Group group, Splittable privileges) {
        serviceFacade.getTeamMembers(group, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable subjects) {
                if (subjects != null) {
                    subjects = subjects.get("members");
                } else {
                    subjects = StringQuoter.createIndexed();
                }
                view.edit(convertGroupToSplittable(group), privileges, subjects);
            }
        });
    }

    @Override
    public void updateTeam(String originalName, String name, String description) {
        view.mask();
        serviceFacade.updateTeam(originalName, name, description, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Group group) {
                view.close();
                ensureHandlers().fireEvent(new TeamSaved());
            }
        });
    }

    @Override
    public void searchCollaborators(String searchTerm,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback) {
        collaboratorsServiceFacade.searchCollaborators(searchTerm, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<Subject> result) {
                Splittable data = StringQuoter.createIndexed();
                result.forEach(subject -> {
                    Splittable splSubject = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(subject));
                    splSubject.assign(data, data.size());
                });
                callback.onSuccess(data);
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void saveTeamSelected(String name,
                                 Splittable createTeamRequest,
                                 Splittable updatePrivilegeReq,
                                 String[] memberIds) {
        view.mask();

        serviceFacade.addTeam(createTeamRequest, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Group group) {
                updatePrivilegesToTeam(group.getName(), updatePrivilegeReq, memberIds, null);
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void updatePrivilegesToTeam(String teamName,
                                       Splittable updatePrivilegeReq,
                                       String[] memberIds,
                                       ReactSuccessCallback callback) {
        view.mask();
        serviceFacade.updateTeamPrivileges(teamName, updatePrivilegeReq, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable ignoreMe) {
                if (memberIds != null) {
                    addMembersToTeam(teamName, memberIds, callback);
                }
                else {
                    if (callback != null) {
                        view.unmask();
                        callback.onSuccess(null);
                    }
                }
            }
        });
    }

    void addMembersToTeam(String groupName, String[] memberIds, ReactSuccessCallback callback) {
        serviceFacade.addMembersToTeam(groupName, Lists.newArrayList(memberIds), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> updateMemberResults) {
                if (callback != null) {
                    view.unmask();
                    callback.onSuccess(null);
                } else {
                    view.close();
                    ensureHandlers().fireEvent(new TeamSaved());
                }
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void removeMemberAndPrivilege(String originalName,
                                         String subjectId,
                                         Splittable updatePrivilegeReq,
                                         ReactSuccessCallback callback) {
        view.mask();

        serviceFacade.deleteTeamMembers(originalName, Lists.newArrayList(subjectId), false, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                updatePrivilegesToTeam(originalName, updatePrivilegeReq, null, callback);
            }
        });

    }

    @Override
    public void leaveTeamSelected(String teamName, ReactSuccessCallback callback) {
        view.mask();
        serviceFacade.leaveTeam(teamName, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                if (result != null && !result.isEmpty()) {
                    UpdateMemberResult updateMemberResult = result.get(0);
                    if (updateMemberResult.isSuccess()) {
                        announcer.schedule(new IplantAnnouncementConfig(appearance.leaveTeamSuccess(teamName)));
                        callback.onSuccess(null);
                        view.close();
                        ensureHandlers().fireEvent(new LeaveTeamCompleted());
                    } else {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.leaveTeamFail()));
                        view.unmask();
                    }
                }
            }
        });
    }

    @Override
    public void deleteTeamSelected(String teamName, ReactSuccessCallback callback) {
        view.mask();
        serviceFacade.deleteTeam(teamName, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.postReact(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Group group) {
                announcer.schedule(new IplantAnnouncementConfig(appearance.deleteTeamSuccess(teamName)));
                callback.onSuccess(null);
                view.close();
                ensureHandlers().fireEvent(new DeleteTeamCompleted());
            }
        });
    }

    @Override
    public void joinTeamSelected(String teamName, ReactErrorCallback errorCallback) {
        view.mask();
        serviceFacade.joinTeam(teamName, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                view.unmask();
                ErrorHandler.postReact(throwable);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> updateMemberResults) {
                if (updateMemberResults != null && !updateMemberResults.isEmpty()) {
                    UpdateMemberResult updateMemberResult = updateMemberResults.get(0);
                    if (updateMemberResult.isSuccess()) {
                        announcer.schedule(new IplantAnnouncementConfig(appearance.joinTeamSuccess(teamName)));
                        ensureHandlers().fireEvent(new JoinTeamCompleted());
                        view.close();
                    } else {
                        errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, null);
                        view.unmask();
                    }
                }
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void sendRequestToJoin(String teamName,
                                  Splittable hasMessage,
                                  ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {
        view.mask();
        serviceFacade.requestToJoinTeam(teamName, hasMessage, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                view.unmask();
                ErrorHandler.postReact(throwable);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                announcer.schedule(new IplantAnnouncementConfig(appearance.requestToJoinSubmitted(teamName)));
                view.close();
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId);
    }

    Splittable getSelfSubject() {
        Splittable self = StringQuoter.createSplittable();
        StringQuoter.create(userInfo.getUsername()).assign(self, "id");
        StringQuoter.create(getSelfFullName()).assign(self, "name");

        return self;
    }

    String getSelfFullName() {
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        String name = Strings.isNullOrEmpty(firstName) ? "" : firstName + " ";
        name += Strings.isNullOrEmpty(lastName) ? "" : lastName;
        return name;
    }

    @Override
    public HandlerRegistration addTeamSavedHandler(TeamSaved.TeamSavedHandler handler) {
        return ensureHandlers().addHandler(TeamSaved.TYPE, handler);
    }

    @Override
    public HandlerRegistration addLeaveTeamCompletedHandler(LeaveTeamCompleted.LeaveTeamCompletedHandler handler) {
        return ensureHandlers().addHandler(LeaveTeamCompleted.TYPE, handler);
    }

    @Override
    public HandlerRegistration addDeleteTeamCompletedHandler(DeleteTeamCompleted.DeleteTeamCompletedHandler handler) {
        return ensureHandlers().addHandler(DeleteTeamCompleted.TYPE, handler);
    }

    @Override
    public HandlerRegistration addJoinTeamCompletedHandler(JoinTeamCompleted.JoinTeamCompletedHandler handler) {
        return ensureHandlers().addHandler(JoinTeamCompleted.TYPE, handler);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    Splittable convertGroupToSplittable(Group group) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(group));
    }

    ReactTeamViews.EditTeamProps getBaseProps() {
        ReactTeamViews.EditTeamProps props = new ReactTeamViews.EditTeamProps();
        props.parentId = Teams.Ids.EDIT_TEAM_DIALOG;
        props.presenter = this;
        props.collaboratorsUtil = collaboratorsUtil;
        props.groupNameRestrictedChars = iplantValidationConstants.restrictedGroupNameChars();
        props.selfSubject = getSelfSubject();
        props.publicUsersId = deProperties.getGrouperAllId();

        return props;
    }
}
