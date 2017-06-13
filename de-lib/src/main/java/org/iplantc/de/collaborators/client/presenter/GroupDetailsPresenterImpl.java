package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.DeleteMembersSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class GroupDetailsPresenterImpl implements GroupDetailsView.Presenter {

    private GroupDetailsView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private GroupView.GroupViewAppearance appearance;
    private HandlerManager handlerManager;
    GroupDetailsView.MODE mode;
    String originalGroup;

    @Inject IplantAnnouncer announcer;
    @Inject UserInfo userInfo;

    @Inject
    public GroupDetailsPresenterImpl(GroupDetailsView view,
                                     GroupServiceFacade serviceFacade,
                                     GroupAutoBeanFactory factory,
                                     GroupView.GroupViewAppearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;
        
        view.addAddGroupMemberSelectedHandler(this);
        view.addDeleteMembersSelectedHandler(this);
    }

    @Override
    public void go(HasOneWidget container, Group group, GroupDetailsView.MODE mode) {
        this.mode = mode;
        container.setWidget(view);

        getGroupMembers(group);
    }

    void getGroupMembers(Group group) {
        if (GroupDetailsView.MODE.EDIT == mode) {
            this.originalGroup = group.getName();
            view.mask(appearance.loadingMask());
            serviceFacade.getMembers(group, new AsyncCallback<List<Subject>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                    view.unmask();
                }

                @Override
                public void onSuccess(List<Subject> result) {
                    view.addMembers(result);
                    view.unmask();
                }
            });
        } else {
            group = factory.getGroup().as();
        }
        view.edit(group, mode);
    }

    @Override
    public boolean isViewValid() {
        return view.isValid();
    }

    @Override
    public void saveGroupSelected() {
        view.mask(appearance.loadingMask());
        Group group = view.getGroup();
        if (group == null) {
            view.unmask();
            return;
        }
        if (GroupDetailsView.MODE.ADD == mode) {
            addGroup(group);
        } else {
            updateGroup(group);
        }
    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId);
    }

    void addGroup(Group group) {
        serviceFacade.addGroup(group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Group result) {
                ensureHandlers().fireEvent(new GroupSaved(result));
                List<Subject> subjects = view.getMembers();
                addGroupMembers(group, subjects);
            }
        });
    }

    void updateGroup(Group group) {
        serviceFacade.updateGroup(originalGroup, group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Group result) {
                ensureHandlers().fireEvent(new GroupSaved(result));
            }
        });
    }

    void addGroupMembers(Group group, List<Subject> subjects) {
        if (subjects != null && !subjects.isEmpty()) {
            serviceFacade.addMembers(group, subjects, new AsyncCallback<List<UpdateMemberResult>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                    view.unmask();
                }

                @Override
                public void onSuccess(List<UpdateMemberResult> result) {
                    List<UpdateMemberResult> failures = getFailResults(result);
                    if (failures != null && !failures.isEmpty()) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.unableToAddMembers(failures)));
                    } else {
                        announcer.schedule(new SuccessAnnouncementConfig(appearance.groupCreatedSuccess(group)));
                    }
                    view.unmask();
                }
            });
        }
    }

    @Override
    public void onAddGroupMemberSelected(AddGroupMemberSelected event) {
        Group group = event.getGroup();
        Subject subject = event.getSubject();

        if (group != null && !Strings.isNullOrEmpty(group.getName()) && subject != null) {
            if (isAddingSelf(subject)) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.collaboratorsSelfAdd()));
                view.unmask();
                return;
            }
            if (isAddingCurrentGroup(subject)) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.groupSelfAdd()));
                view.unmask();
                return;
            }
            view.mask(appearance.loadingMask());
            serviceFacade.addMembers(group, wrapSubjectInList(subject), new AsyncCallback<List<UpdateMemberResult>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                    view.unmask();
                }

                @Override
                public void onSuccess(List<UpdateMemberResult> result) {
                    List<UpdateMemberResult> failures = getFailResults(result);
                    if (failures != null && !failures.isEmpty()) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.unableToAddMembers(failures)));
                    } else {
                        view.addMembers(wrapSubjectInList(subject));
                    }
                    view.unmask();
                }
            });
        }
    }

    boolean isAddingCurrentGroup(Subject subject) {
        if (Subject.GROUP_IDENTIFIER.equals(subject.getSourceId())) {
            return originalGroup.equals(subject.getSubjectDisplayName());
        }
        return false;
    }

    boolean isAddingSelf(Subject subject) {
        return userInfo.getUsername().equals(subject.getId());
    }

    List<Subject> wrapSubjectInList(Subject subject) {
        return Lists.newArrayList(subject);
    }

    @Override
    public void onDeleteMembersSelected(DeleteMembersSelected event) {
        if (GroupDetailsView.MODE.EDIT == mode) {
            List<Subject> subjects = event.getSubjects();
            Group group = event.getGroup();
            if (subjects != null && !subjects.isEmpty()) {
                deleteMembers(subjects, group);
            }
        }
    }

    void deleteMembers(List<Subject> subjects, Group group) {
        serviceFacade.deleteMembers(group, subjects, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<UpdateMemberResult> failures = getFailResults(result);
                if (failures != null && !failures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.memberDeleteFail(failures)));
                } else {
                    view.deleteMembers(subjects);
                }
                view.unmask();
            }
        });
    }

    List<UpdateMemberResult> getFailResults(List<UpdateMemberResult> result) {
        return result.stream()
                                                              .filter(item -> !item.isSuccess())
                                                              .collect(Collectors.toList());
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    @Override
    public HandlerRegistration addGroupSavedHandler(GroupSaved.GroupSavedHandler handler) {
        return ensureHandlers().addHandler(GroupSaved.TYPE, handler);
    }
}
