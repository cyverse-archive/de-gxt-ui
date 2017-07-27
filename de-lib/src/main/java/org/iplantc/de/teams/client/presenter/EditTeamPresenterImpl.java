package org.iplantc.de.teams.client.presenter;

import static org.iplantc.de.teams.client.EditTeamView.GROUPER_ID;
import static org.iplantc.de.teams.client.EditTeamView.SEARCH_MEMBERS_TAG;

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
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.AddPublicUserSelected;
import org.iplantc.de.teams.client.events.RemoveMemberPrivilegeSelected;
import org.iplantc.de.teams.client.events.RemoveNonMemberPrivilegeSelected;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.views.dialogs.SaveTeamProgressDialog;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

public class EditTeamPresenterImpl implements EditTeamView.Presenter,
                                              UserSearchResultSelected.UserSearchResultSelectedEventHandler,
                                              RemoveMemberPrivilegeSelected.RemoveMemberPrivilegeSelectedHandler,
                                              RemoveNonMemberPrivilegeSelected.RemoveNonMemberPrivilegeSelectedHandler,
                                              AddPublicUserSelected.AddPublicUserSelectedHandler {

    private EditTeamView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private TeamsView.TeamsViewAppearance appearance;
    EditTeamView.MODE mode;
    SaveTeamProgressDialog progressDlg;
    HandlerManager handlerManager;
    @Inject UserInfo userInfo;
    @Inject AsyncProviderWrapper<SaveTeamProgressDialog> progressDialogProvider;

    final String ALL_PUBLIC_USERS_NAME;
    final String ALL_PUBLIC_USERS_ID;

    @Inject
    public EditTeamPresenterImpl(EditTeamView view,
                                 GroupServiceFacade serviceFacade,
                                 GroupAutoBeanFactory factory,
                                 TeamsView.TeamsViewAppearance appearance,
                                 DEProperties deProperties) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;
        this.ALL_PUBLIC_USERS_NAME = deProperties.getGrouperAllDisplayName();
        this.ALL_PUBLIC_USERS_ID = deProperties.getGrouperAllId();

        view.addUserSearchResultSelectedEventHandler(this);
        view.addRemoveMemberPrivilegeSelectedHandler(this);
        view.addRemoveNonMemberPrivilegeSelectedHandler(this);
        view.addAddPublicUserSelectedHandler(this);
    }
    @Override
    public void go(HasOneWidget widget, Group group) {
        widget.setWidget(view);

        if (group == null) {
            group = factory.getGroup().as();
            mode = EditTeamView.MODE.CREATE;
            addPublicUser();
        } else {
            mode = EditTeamView.MODE.EDIT;
            getTeamPrivileges(group);
        }

        view.edit(group);
    }

    public void getTeamPrivileges(Group group) {
        view.mask(appearance.loadingMask());
        serviceFacade.getTeamPrivileges(group, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Privilege> privileges) {
                List<Privilege> filteredPrivs = filterPrivs(privileges);
                renamePublicUser(filteredPrivs);
                view.addNonMembers(filteredPrivs);
                view.unmask();
            }
        });
    }

    void renamePublicUser(List<Privilege> filteredPrivs) {
        List<Privilege> publicPrivs = getPublicUserPrivilege(filteredPrivs);
        if (publicPrivs != null && !publicPrivs.isEmpty()) {
            Privilege publicPriv = publicPrivs.get(0);
            publicPriv.getSubject().setName(ALL_PUBLIC_USERS_NAME);
        }
    }

    List<Privilege> filterPrivs(List<Privilege> privileges) {
        return privileges.stream()
                         .filter(privilege -> (privilege.getPrivilegeType() != PrivilegeType.optout
                                               && !isCurrentUserPrivilege(privilege))
                                               && !isDeGrouperPrivilege(privilege))
                         .collect(Collectors.toList());
    }

    boolean isCurrentUserPrivilege(Privilege privilege) {
        return userInfo.getUsername().equals(privilege.getSubject().getId());
    }

    boolean isDeGrouperPrivilege(Privilege privilege) {
        return GROUPER_ID.equals(privilege.getSubject().getId());
    }

    void addPublicUser() {
        Privilege privilege = factory.getPrivilege().as();
        Subject publicUser = createPublicUser();
        privilege.setSubject(publicUser);
        privilege.setPrivilegeType(PrivilegeType.view);

        view.addNonMembers(Lists.newArrayList(privilege));
    }

    Subject createPublicUser() {
        Subject subject = factory.getSubject().as();
        subject.setName(ALL_PUBLIC_USERS_NAME);
        subject.setId(ALL_PUBLIC_USERS_ID);
        return subject;
    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId);
    }

    @Override
    public boolean isViewValid() {
        return view.isValid();
    }

    @Override
    public void saveTeamSelected(IsHideable hideable) {
        if (EditTeamView.MODE.CREATE == mode) {
            progressDialogProvider.get(new AsyncCallback<SaveTeamProgressDialog>() {
                @Override
                public void onFailure(Throwable throwable) {
                    ErrorHandler.post(throwable);
                }

                @Override
                public void onSuccess(SaveTeamProgressDialog dialog) {
                    progressDlg = dialog;
                    createNewTeam(hideable);
                }
            });
        }
    }

    void createNewTeam(IsHideable hideable) {
        view.mask(appearance.loadingMask());
        progressDlg.startProgress(3);

        List<PrivilegeType> publicPrivs = getPublicUserPrivilegeType();
        serviceFacade.addTeam(view.getTeam(), publicPrivs, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                view.unmask();
                progressDlg.hide();
            }

            @Override
            public void onSuccess(Group group) {
                List<Privilege> nonMemberPrivs = view.getNonMemberPrivileges();
                List<Privilege> memberPrivs = view.getMemberPrivileges();
                List<Privilege> allPrivs = createEmptyPrivilegeList();
                allPrivs.addAll(memberPrivs);
                allPrivs.addAll(nonMemberPrivs);
                addPrivilegesToTeam(group, allPrivs, hideable);
            }
        });
    }

    List<PrivilegeType> getPublicUserPrivilegeType() {
        List<Privilege> nonMemberPrivileges = view.getNonMemberPrivileges();
        List<Privilege> publicUserList = getPublicUserPrivilege(nonMemberPrivileges);
        if (publicUserList == null || publicUserList.isEmpty()) {
            return null;
        }
        PrivilegeType privilege = publicUserList.get(0).getPrivilegeType();
        return Lists.newArrayList(privilege);
    }

    List<Privilege> getPublicUserPrivilege(List<Privilege> privileges) {
        return privileges.stream()
                           .filter(privilege -> ALL_PUBLIC_USERS_ID.equals(privilege.getSubject().getId()))
                           .collect(Collectors.toList());
    }

    void addMembersToTeam(Group group, IsHideable hideable) {
        progressDlg.updateProgress();
        Subject self = createSelfSubject();
        List<Privilege> privileges = view.getMemberPrivileges();
        List<Subject> membersToAdd = getSubjectsFromPrivileges(privileges);
        membersToAdd.add(self);
        serviceFacade.addMembersToTeam(group, membersToAdd, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                mode = EditTeamView.MODE.EDIT;
                view.unmask();
                progressDlg.hide();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> updateMemberResults) {
                hideable.hide();
                progressDlg.updateProgress();
                view.unmask();
                ensureHandlers().fireEvent(new TeamSaved(group));
            }
        });
    }

    void addPrivilegesToTeam(Group group, List<Privilege> privileges, IsHideable hideable) {
        progressDlg.updateProgress();

        List<UpdatePrivilegeRequest> updateList = convertPrivilegesToUpdateRequest(privileges);

        UpdatePrivilegeRequestList allUpdates = factory.getUpdatePrivilegeRequestList().as();
        allUpdates.setRequests(updateList);

        serviceFacade.updateTeamPrivileges(group, allUpdates, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                mode = EditTeamView.MODE.EDIT;
                view.unmask();
                progressDlg.hide();
            }

            @Override
            public void onSuccess(List<Privilege> privileges) {
                addMembersToTeam(group, hideable);
            }
        });
    }

    Subject createSelfSubject() {
        Subject subject = factory.getSubject().as();
        subject.setId(userInfo.getUsername());
        return subject;
    }

    @Override
    public void onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
        Subject subject = userSearchResultSelected.getSubject();
        String tag = userSearchResultSelected.getTag();
        Privilege privilege = factory.getPrivilege().as();
        privilege.setSubject(subject);
        privilege.setPrivilegeType(PrivilegeType.read);

        if (SEARCH_MEMBERS_TAG.equals(tag)) {
            view.addMembers(Lists.newArrayList(privilege));
        } else {
            view.addNonMembers(Lists.newArrayList(privilege));
            view.setPublicUserButtonVisibility(!hasPublicUser());
        }

    }

    @Override
    public void onRemoveMemberPrivilegeSelected(RemoveMemberPrivilegeSelected event) {
        Privilege privilege = event.getPrivilege();
        // No service calls required in CREATE mode, remove directly from store
        if (EditTeamView.MODE.CREATE == mode) {
            view.removeMemberPrivilege(privilege);
        }
    }

    @Override
    public void onRemoveNonMemberPrivilegeSelected(RemoveNonMemberPrivilegeSelected event) {
        Privilege privilege = event.getPrivilege();
        // No service calls required in CREATE mode, remove directly from store
        if (EditTeamView.MODE.CREATE == mode) {
            view.removeNonMemberPrivilege(privilege);
            view.setPublicUserButtonVisibility(!hasPublicUser());
        }
    }

    @Override
    public void onAddPublicUserSelected(AddPublicUserSelected event) {
        addPublicUser();
    }

    List<UpdatePrivilegeRequest> convertPrivilegesToUpdateRequest(List<Privilege> privileges) {
        return privileges.stream().map(privilege -> {
            UpdatePrivilegeRequest update = factory.getUpdatePrivilegeRequest().as();
            update.setSubjectId(privilege.getSubject().getId());
            PrivilegeType type = privilege.getPrivilegeType();
            List<PrivilegeType> privilegeTypes = Lists.newArrayList();
            privilegeTypes.add(type);
            update.setPrivileges(privilegeTypes);
            return update;
        }).collect(Collectors.toList());
    }

    List<Privilege> createEmptyPrivilegeList() {
        return Lists.newArrayList();
    }

    List<Subject> getSubjectsFromPrivileges(List<Privilege> privileges) {
        return privileges.stream()
                         .map(Privilege::getSubject)
                         .collect(Collectors.toList());
    }

    boolean hasPublicUser() {
        List<Privilege> nonMemberPrivs = view.getNonMemberPrivileges();
        long publicUserCount = nonMemberPrivs.stream().filter(privilege -> ALL_PUBLIC_USERS_ID.equals(privilege.getSubject().getId())).count();
        return publicUserCount > 0;
    }

    @Override
    public HandlerRegistration addTeamSavedHandler(TeamSaved.TeamSavedHandler handler) {
        return ensureHandlers().addHandler(TeamSaved.TYPE, handler);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

}
