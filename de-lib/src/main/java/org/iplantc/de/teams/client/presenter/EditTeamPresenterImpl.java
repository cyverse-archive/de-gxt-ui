package org.iplantc.de.teams.client.presenter;

import static org.iplantc.de.teams.client.EditTeamView.ALL_PUBLIC_USERS_ID;
import static org.iplantc.de.teams.client.EditTeamView.ALL_PUBLIC_USERS_NAME;
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
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditTeamPresenterImpl implements EditTeamView.Presenter,
                                              UserSearchResultSelected.UserSearchResultSelectedEventHandler {

    private EditTeamView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private TeamsView.TeamsViewAppearance appearance;
    EditTeamView.MODE mode;
    @Inject UserInfo userInfo;

    @Inject
    public EditTeamPresenterImpl(EditTeamView view,
                                 GroupServiceFacade serviceFacade,
                                 GroupAutoBeanFactory factory,
                                 TeamsView.TeamsViewAppearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;

        view.addUserSearchResultSelectedEventHandler(this);
    }
    @Override
    public void go(HasOneWidget widget, Group group) {
        widget.setWidget(view);

        if (group == null) {
            group = factory.getGroup().as();
            mode = EditTeamView.MODE.CREATE;
        } else {
            mode = EditTeamView.MODE.EDIT;
        }

        view.edit(group);
        addPublicUser();
    }

    void addPublicUser() {
        Privilege privilege = factory.getPrivilege().as();
        Subject subject = factory.getSubject().as();
        subject.setName(ALL_PUBLIC_USERS_NAME);
        subject.setId(ALL_PUBLIC_USERS_ID);
        privilege.setSubject(subject);
        privilege.setPrivilegeType(PrivilegeType.view);

        view.addNonMembers(Lists.newArrayList(privilege));
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
            createNewTeam(hideable);
        }
    }

    void createNewTeam(IsHideable hideable) {
        List<Privilege> nonMemberPrivileges = view.getNonMemberPrivileges();
        Privilege publicUser = nonMemberPrivileges.stream()
                                                  .filter(privilege -> ALL_PUBLIC_USERS_NAME.equals(
                                                          privilege.getSubject().getName()))
                                                  .collect(Collectors.toList())
                                                  .get(0);
        PrivilegeType privilege = publicUser.getPrivilegeType();
        serviceFacade.addTeam(view.getTeam(), privilege, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
            }

            @Override
            public void onSuccess(Group group) {
                addMembersToTeam(group, hideable);
            }
        });
    }

    void addMembersToTeam(Group group, IsHideable hideable) {
        Subject self = createSelfSubject();
        List<Privilege> privileges = view.getMemberPrivileges();
        List<Subject> membersToAdd = privileges.stream()
                                               .map(Privilege::getSubject)
                                               .collect(Collectors.toList());
        membersToAdd.add(self);

        serviceFacade.addMembersToTeam(group, membersToAdd, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                mode = EditTeamView.MODE.EDIT;
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> updateMemberResults) {
                List<Privilege> nonMemberPrivs = view.getNonMemberPrivileges();
                privileges.addAll(nonMemberPrivs);
                addPrivilegesToTeam(group, privileges, hideable);
            }
        });
    }

    void addPrivilegesToTeam(Group group, List<Privilege> privileges, IsHideable hideable) {
        List<UpdatePrivilegeRequest> updateList = Lists.newArrayList();

        privileges.forEach(new Consumer<Privilege>() {
            @Override
            public void accept(Privilege privilege) {
                UpdatePrivilegeRequest update = factory.getUpdatePrivilegeRequest().as();
                update.setSubjectId(privilege.getSubject().getId());
                update.setPrivileges(Lists.newArrayList(privilege.getPrivilegeType()));
                updateList.add(update);
            }
        });

        serviceFacade.updateTeamPrivileges(group, updateList, new AsyncCallback<List<Privilege>>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(throwable);
                mode = EditTeamView.MODE.EDIT;
            }

            @Override
            public void onSuccess(List<Privilege> privileges) {
                hideable.hide();
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
        }

    }
}
