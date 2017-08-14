package org.iplantc.de.teams.client.presenter;

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
import java.util.Map;
import java.util.Set;
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
    Group originalGroup;
    @Inject UserInfo userInfo;
    @Inject AsyncProviderWrapper<SaveTeamProgressDialog> progressDialogProvider;

    final String ALL_PUBLIC_USERS_NAME;
    final String ALL_PUBLIC_USERS_ID;
    final String GROUPER_ID;

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
        this.GROUPER_ID = deProperties.getGrouperId();

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
            group.setSourceId(Group.GROUP_IDENTIFIER);

            mode = EditTeamView.MODE.CREATE;
            addPublicUser();
        } else {
            mode = EditTeamView.MODE.EDIT;
            getTeamPrivileges(group);
            originalGroup = copy(group);
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
                if (privileges != null && !privileges.isEmpty()) {
                    List<Privilege> filteredPrivs = filterExtraPrivileges(privileges);
                    renamePublicUser(filteredPrivs);
                    getTeamMembers(group, filteredPrivs);
                } else {
                    view.unmask();
                }
            }
        });
    }

    void getTeamMembers(Group group, List<Privilege> privileges) {
        serviceFacade.getTeamMembers(group, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Subject> subjects) {
                List<Subject> filteredSubjects = filterOutCurrentUser(subjects);
                Map<Boolean, List<Privilege>> mapIsMemberPriv = getMapIsMemberPrivilege(privileges, filteredSubjects);
                view.addMembers(mapIsMemberPriv.get(true));
                view.addNonMembers(mapIsMemberPriv.get(false));
                view.unmask();
            }
        });
    }

    @Override
    public void saveTeamSelected(IsHideable hideable) {
        progressDialogProvider.get(new AsyncCallback<SaveTeamProgressDialog>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(SaveTeamProgressDialog dialog) {
                progressDlg = dialog;
                if (EditTeamView.MODE.CREATE == mode) {
                    createNewTeam(hideable);
                } else {
                    updateTeam(hideable);
                }
            }
        });
    }

    void updateTeam(IsHideable hideable) {
        view.mask(appearance.loadingMask());
        progressDlg.startProgress(3);

        Group group = view.getTeam();
        serviceFacade.updateTeam(originalGroup.getName(), group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                view.unmask();
            }

            @Override
            public void onSuccess(Group group) {
                addPrivilegesToTeam(group, hideable);
            }
        });
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
                addPrivilegesToTeam(group, hideable);
            }
        });
    }

    void addPrivilegesToTeam(Group group, IsHideable hideable) {
        progressDlg.updateProgress();

        List<Privilege> nonMemberPrivs = view.getNonMemberPrivileges();
        List<Privilege> memberPrivs = view.getMemberPrivileges();
        List<Privilege> allPrivs = createEmptyPrivilegeList();
        allPrivs.addAll(memberPrivs);
        allPrivs.addAll(nonMemberPrivs);

        List<UpdatePrivilegeRequest> updateList = convertPrivilegesToUpdateRequest(allPrivs);

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
        } else {
            removeMemberAndPrivilege(privilege);
        }
    }

    void removeMemberAndPrivilege(Privilege privilege) {
        view.mask(appearance.loadingMask());

        Subject subject = privilege.getSubject();

        serviceFacade.deleteTeamMembers(originalGroup, Lists.newArrayList(subject), false, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                removePrivilege(privilege, true);
            }
        });

    }

    @Override
    public void onRemoveNonMemberPrivilegeSelected(RemoveNonMemberPrivilegeSelected event) {
        Privilege privilege = event.getPrivilege();
        // No service calls required in CREATE mode, remove directly from store
        if (EditTeamView.MODE.CREATE == mode) {
            view.removeNonMemberPrivilege(privilege);
            view.setPublicUserButtonVisibility(!hasPublicUser());
        } else {
            view.mask(appearance.loadingMask());
            removePrivilege(privilege, false);
        }
    }

    void removePrivilege(Privilege privilege, boolean isMember) {
        privilege.setPrivilegeType(null);

        List<UpdatePrivilegeRequest> updateList = convertPrivilegesToUpdateRequest(Lists.newArrayList(privilege));

        UpdatePrivilegeRequestList allUpdates = factory.getUpdatePrivilegeRequestList().as();
        allUpdates.setRequests(updateList);

        serviceFacade.updateTeamPrivileges(originalGroup, allUpdates, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Privilege> result) {
                if (isMember) {
                    view.removeMemberPrivilege(privilege);
                } else {
                    view.removeNonMemberPrivilege(privilege);
                    view.setPublicUserButtonVisibility(!hasPublicUser());
                }
                view.unmask();
            }
        });
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
    public void onAddPublicUserSelected(AddPublicUserSelected event) {
        addPublicUser();
    }

    public Map<Boolean,List<Privilege>> getMapIsMemberPrivilege(List<Privilege> privileges, List<Subject> members) {
        List<String> memberIds = createEmptyStringList();
        if (members != null && !members.isEmpty()) {
            memberIds = members.stream().map(Subject::getId).collect(Collectors.toList());

            privileges = addMembersWithPublicPrivilege(privileges, members);
        }

        List<String> finalMemberIds = memberIds;
        return privileges.stream()
                         .collect(Collectors.partitioningBy(privilege -> finalMemberIds.contains(privilege.getSubject().getId())));

    }

    /**
     * The grouper privilege endpoints return the smallest list possible.  If the public user
     * has view permissions, for example, and the team creator assigns view permissions to individual
     * users, those privileges will not be returned from the privileges endpoint since those privileges
     * are already implied by the public user privileges.
     * @param privileges
     * @param members
     * @return
     */
    List<Privilege> addMembersWithPublicPrivilege(List<Privilege> privileges, List<Subject> members) {
        List<Privilege> publicPrivs = getPublicUserPrivilege(privileges);
        if (publicPrivs == null || publicPrivs.isEmpty()) {
            return privileges;
        }

        Set<String> privilegeIds = privileges.stream()
                                             .map(privilege -> privilege.getSubject().getId())
                                             .collect(Collectors.toSet());
        List<Subject> membersWithoutPrivs = members.stream()
                                                   .filter(subject -> !privilegeIds.contains(subject.getId()))
                                                   .collect(Collectors.toList());

        PrivilegeType publicPrivType = publicPrivs.get(0).getPrivilegeType();

        List<Privilege> allPrivs = Lists.newArrayList();
        List<Privilege> missingMemberPrivs = membersWithoutPrivs.stream().map(subject -> {
            Privilege privilege = factory.getPrivilege().as();
            privilege.setSubject(subject);
            privilege.setPrivilegeType(publicPrivType);
            return privilege;
        }).collect(Collectors.toList());
        allPrivs.addAll(privileges);
        allPrivs.addAll(missingMemberPrivs);

        return allPrivs;
    }

    List<UpdatePrivilegeRequest> convertPrivilegesToUpdateRequest(List<Privilege> privileges) {
        return privileges.stream().map(privilege -> {
            UpdatePrivilegeRequest update = factory.getUpdatePrivilegeRequest().as();
            update.setSubjectId(privilege.getSubject().getId());
            PrivilegeType type = privilege.getPrivilegeType();
            List<PrivilegeType> privilegeTypes = Lists.newArrayList();
            if (type != null) {
                privilegeTypes.add(type);
            }
            update.setPrivileges(privilegeTypes);
            return update;
        }).collect(Collectors.toList());
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

    List<Privilege> filterExtraPrivileges(List<Privilege> privileges) {
        return privileges.stream()
                         .filter(privilege -> (privilege.getPrivilegeType() != PrivilegeType.optout
                                               && !isCurrentUserPrivilege(privilege))
                                               && !isDeGrouperPrivilege(privilege))
                         .collect(Collectors.toList());
    }

    List<Subject> filterOutCurrentUser(List<Subject> subjects) {
        return subjects.stream().filter(subject -> !isCurrentUser(subject)).collect(
                Collectors.toList());
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

    boolean isCurrentUser(Subject subject) {
        return userInfo.getUsername().equals(subject.getId());
    }

    boolean isCurrentUserPrivilege(Privilege privilege) {
        return userInfo.getUsername().equals(privilege.getSubject().getId());
    }

    boolean isDeGrouperPrivilege(Privilege privilege) {
        return GROUPER_ID.equals(privilege.getSubject().getId());
    }

    Group copy(Group group) {
        Group copy = factory.getGroup().as();
        copy.setName(group.getName());
        copy.setDisplayName(group.getDisplayName());
        copy.setDescription(group.getDescription());
        return copy;
    }

    Subject createSelfSubject() {
        Subject subject = factory.getSubject().as();
        subject.setId(userInfo.getUsername());
        return subject;
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

    void renamePublicUser(List<Privilege> filteredPrivs) {
        List<Privilege> publicPrivs = getPublicUserPrivilege(filteredPrivs);
        if (publicPrivs != null && !publicPrivs.isEmpty()) {
            Privilege publicPriv = publicPrivs.get(0);
            publicPriv.getSubject().setName(ALL_PUBLIC_USERS_NAME);
        }
    }

    List<Privilege> createEmptyPrivilegeList() {
        return Lists.newArrayList();
    }

    List<String> createEmptyStringList() {
        return Lists.newArrayList();
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
