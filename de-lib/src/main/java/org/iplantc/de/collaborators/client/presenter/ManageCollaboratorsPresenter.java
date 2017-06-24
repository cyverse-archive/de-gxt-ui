/**
 * 
 */
package org.iplantc.de.collaborators.client.presenter;

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
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.presenter.callbacks.ParentAddMemberToGroupCallback;
import org.iplantc.de.collaborators.client.presenter.callbacks.ParentDeleteSubjectsCallback;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.collaborators.client.views.dialogs.GroupDetailsDialog;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sriram
 * 
 */
public class ManageCollaboratorsPresenter implements ManageCollaboratorsView.Presenter,
                                                     RemoveCollaboratorSelected.RemoveCollaboratorSelectedHandler,
                                                     AddGroupSelected.AddGroupSelectedHandler,
                                                     GroupNameSelected.GroupNameSelectedHandler,
                                                     UserSearchResultSelected.UserSearchResultSelectedEventHandler {

    public class AddMemberToGroupCallback implements AsyncCallback<List<UpdateMemberResult>> {
        ParentAddMemberToGroupCallback parent;

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
            parent.done(null);
        }

        @Override
        public void onSuccess(List<UpdateMemberResult> result) {
            parent.done(result);
        }

        public void setParent(ParentAddMemberToGroupCallback parent) {
            this.parent = parent;
        }
    }

    private class DeleteUsersChildCallback implements AsyncCallback<List<UpdateMemberResult>> {

        private ParentDeleteSubjectsCallback parentCallback;

        public DeleteUsersChildCallback(ParentDeleteSubjectsCallback parentCallback) {
            this.parentCallback = parentCallback;
        }

        @Override
        public void onSuccess(List<UpdateMemberResult> result) {
            if (parentCallback != null) {
                parentCallback.done(result);
            }
        }

        @Override
        public void onFailure(Throwable caught) {
            if (parentCallback != null) {
                parentCallback.done(caught);
                return;
            }
            ErrorHandler.post(caught);
            view.unmaskCollaborators();
        }
    }

    private class DeleteGroupChildCallback implements AsyncCallback<Group> {
        private ParentDeleteSubjectsCallback parentCallback;

        public DeleteGroupChildCallback(ParentDeleteSubjectsCallback parentCallback) {
            this.parentCallback = parentCallback;
        }

        @Override
        public void onFailure(Throwable caught) {
            if (parentCallback != null) {
                parentCallback.done(caught);
                return;
            }
            ErrorHandler.post(caught);
            view.unmaskCollaborators();
        }

        @Override
        public void onSuccess(Group result) {
            if (parentCallback != null) {
                parentCallback.done(result);
            }
        }
    }

    @Inject CollaboratorsUtil collaboratorsUtil;
    @Inject EventBus eventBus;
    @Inject IplantAnnouncer announcer;
    @Inject UserInfo userInfo;
    private ManageCollaboratorsViewFactory factory;
    private GroupAutoBeanFactory groupFactory;
    private GroupServiceFacade groupServiceFacade;
    private CollaboratorsServiceFacade collabServiceFacade;
    private ManageCollaboratorsView.Appearance groupAppearance;
    ManageCollaboratorsView view;
    HandlerRegistration addCollabHandlerRegistration;

    @Inject AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialog;

    @Inject
    public ManageCollaboratorsPresenter(ManageCollaboratorsViewFactory factory,
                                        GroupAutoBeanFactory groupFactory,
                                        GroupServiceFacade groupServiceFacade,
                                        CollaboratorsServiceFacade collabServiceFacade,
                                        ManageCollaboratorsView.Appearance groupAppearance) {
        this.factory = factory;
        this.groupFactory = groupFactory;
        this.groupServiceFacade = groupServiceFacade;
        this.collabServiceFacade = collabServiceFacade;
        this.groupAppearance = groupAppearance;
    }

    void addEventHandlers() {
        view.addAddGroupSelectedHandler(this);
        view.addGroupNameSelectedHandler(this);
        view.addUserSearchResultSelectedEventHandler(this);
        view.addRemoveCollaboratorSelectedHandler(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.commons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget )
     */
    @Override
    public void go(HasOneWidget container, ManageCollaboratorsView.MODE mode) {
        this.view = factory.create(mode);
        loadCurrentCollaborators();
        getGroups();
        addEventHandlers();
        container.setWidget(view.asWidget());
    }

    @Override
    public void onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
        Subject subject = userSearchResultSelected.getSubject();
        if (!userInfo.getUsername()
                     .equals(subject.getId())) {
            List<Subject> selectedSubjects = view.getSelectedSubjects();
            List<Subject> selectedGroups = mapIsGroup(selectedSubjects).get(true);
            if (selectedGroups != null && !selectedGroups.isEmpty()) {
                addMemberToGroups(subject, selectedGroups);
            } else {
                if (!collaboratorsUtil.isCurrentCollaborator(subject, view.getCollaborators())) {
                    addAsCollaborators(Arrays.asList(subject));
                }
            }

        } else {
            announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.collaboratorsSelfAdd()));
        }
    }

    void addMemberToGroups(Subject member, List<Subject> selectedCollaboratorLists) {
        List<AddMemberToGroupCallback> childCallbacks = getAddMemberToGroupCallbackList();

        selectedCollaboratorLists.forEach(new Consumer<Subject>() {
            @Override
            public void accept(Subject subject) {
                AddMemberToGroupCallback callback = getAddMemberToGroupCallback();
                Group group = groupFactory.convertSubjectToGroup(subject);
                groupServiceFacade.addMembers(group, wrapSubjectInList(member), callback);
                childCallbacks.add(callback);
            }
        });

        new ParentAddMemberToGroupCallback(childCallbacks) {

            @Override
            public void handleSuccess(List<UpdateMemberResult> totalResults) {
                List<UpdateMemberResult> failures = mapIsSuccessResults(totalResults).get(false);
                if (failures != null && !failures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.unableToAddMembers(failures)));
                } else {
                    announcer.schedule(new SuccessAnnouncementConfig(groupAppearance.memberAddToGroupsSuccess(member)));
                }
            }
        };
    }

    List<Subject> wrapSubjectInList(Subject subject) {
        return Lists.newArrayList(subject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#addAsCollaborators
     * (java.util.List)
     */
    @Override
    public void addAsCollaborators(final List<Subject> models) {
        groupServiceFacade.addMembers(groupFactory.getDefaultGroup(), models, new AsyncCallback<List<UpdateMemberResult>>() {

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<UpdateMemberResult> failures = mapIsSuccessResults(result).get(false);
                if (failures != null && !failures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.unableToAddMembers(failures)));
                } else {
                    // remove added models from search results
                    view.addCollaborators(models);
                    String names = getCollaboratorNames(models);

                    announcer.schedule(new SuccessAnnouncementConfig(groupAppearance.collaboratorAddConfirm(
                            names)));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(groupAppearance.addCollabErrorMsg(), caught);
            }
        });

    }

    String getCollaboratorNames(List<Subject> subjects) {
        Stream<Subject> stream = subjects.stream();

        Stream<String> stringStream = stream.map(Subject::getSubjectDisplayName);
        List<String> names = stringStream.collect(Collectors.toList());
        return Joiner.on(", ").join(names);
    }

    String getSubjectNames(List<UpdateMemberResult> userSuccesses, List<Group> groups) {
        List<String> userNames = userSuccesses.stream()
                                              .map(UpdateMemberResult::getSubjectName)
                                              .collect(Collectors.toList());
        List<String> groupNames = groups.stream()
                                        .map(Group::getSubjectDisplayName)
                                        .collect(Collectors.toList());
        userNames.addAll(groupNames);
        return Joiner.on(", ").join(userNames);
    }

    @Override
    public void getGroups() {
        groupServiceFacade.getGroups(new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCollaborators();
            }

            @Override
            public void onSuccess(List<Subject> result) {
                List<Subject> filteredGroups = excludeDefaultGroup(result);
                view.addCollaborators(filteredGroups);
                view.unmaskCollaborators();
            }
        });
    }

    List<Subject> excludeDefaultGroup(List<Subject> result) {
        return result.stream()
                     .filter(subject -> !Group.DEFAULT_GROUP.equals(subject.getName()))
                     .collect(Collectors.toList());
    }

    @Override
    public void onRemoveCollaboratorSelected(RemoveCollaboratorSelected event) {
        List<Subject> models = event.getSubjects();
        Map<Boolean, List<Subject>> mapIsGroup = mapIsGroup(models);
        List<Subject> selectedGroups = mapIsGroup.get(true);
        List<Subject> selectedUsers = mapIsGroup.get(false);
        if (selectedGroups != null && !selectedGroups.isEmpty()) {
            ConfirmMessageBox deleteAlert = new ConfirmMessageBox(groupAppearance.deleteGroupConfirmHeading(selectedGroups),
                                                                  groupAppearance.deleteGroupConfirm(selectedGroups));
            deleteAlert.show();
            deleteAlert.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        removeCollaborators(selectedGroups, selectedUsers);
                    }
                }
            });
        } else {
            removeCollaborators(null, selectedUsers);
        }
    }

    void removeCollaborators(List<Subject> selectedGroups, List<Subject> selectedUsers) {
        view.maskCollaborators(groupAppearance.loadingMask());
        ParentDeleteSubjectsCallback parentCallback = new ParentDeleteSubjectsCallback() {
            @Override
            public void whenDone(List<UpdateMemberResult> totalResults,
                                 List<Group> successGroups,
                                 List<Throwable> failures) {
                if (failures != null && !failures.isEmpty()) {
                    ErrorHandler.post(failures);
                }
                Map<Boolean, List<UpdateMemberResult>> mapIsSuccess =
                        mapIsSuccessResults(totalResults);
                List<UpdateMemberResult> userFailures = mapIsSuccess.get(false);
                List<UpdateMemberResult> userSuccesses = mapIsSuccess.get(true);
                if (userFailures != null && !userFailures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.memberDeleteFail(userFailures)));
                }
                String names = getSubjectNames(userSuccesses, successGroups);
                announcer.schedule(new SuccessAnnouncementConfig(groupAppearance.collaboratorRemoveConfirm(
                        names)));

                List<String> collaboratorIds = getCollaboratorIds(userSuccesses, successGroups);
                view.removeCollaboratorsById(collaboratorIds);
                view.unmaskCollaborators();
            }
        };

        int callbackSize = getCallbackSize(selectedUsers, selectedGroups);

        parentCallback.setCallbackCounter(callbackSize);

        removeCollaboratorsFromDefault(selectedUsers, parentCallback);
        deleteGroups(selectedGroups, parentCallback);
    }

    void deleteGroups(List<Subject> selectedGroups, ParentDeleteSubjectsCallback parentCallback) {
        if (selectedGroups != null && !selectedGroups.isEmpty()) {
            selectedGroups.forEach(subject -> {
                Group group = groupFactory.convertSubjectToGroup(subject);
                groupServiceFacade.deleteGroup(group, new DeleteGroupChildCallback(parentCallback));
            });
        }
    }

    void removeCollaboratorsFromDefault(List<Subject> selectedUsers,
                                        ParentDeleteSubjectsCallback parentCallback) {
        if (selectedUsers != null && !selectedUsers.isEmpty()) {
            groupServiceFacade.deleteMembers(groupFactory.getDefaultGroup(),
                                             selectedUsers,
                                             new DeleteUsersChildCallback(parentCallback));
        }
    }

    int getCallbackSize(List<Subject> selectedUsers, List<Subject> selectedGroups) {
        int callbackSize = 0;
        if (selectedUsers != null && !selectedUsers.isEmpty()) {
            callbackSize++;
        }
        if (selectedGroups != null && !selectedGroups.isEmpty()) {
            callbackSize += selectedGroups.size();
        }

        return callbackSize;
    }

    List<String> getCollaboratorIds(List<UpdateMemberResult> userSuccesses, List<Group> successGroups) {
        List<String> collaboratorIds = Lists.newArrayList();
        List<String> userIds = userSuccesses.stream()
                                            .map(UpdateMemberResult::getSubjectId)
                                            .collect(Collectors.toList());
        List<String> groupIds = successGroups.stream()
                                             .map(Group::getId)
                                             .collect(Collectors.toList());
        collaboratorIds.addAll(userIds);
        collaboratorIds.addAll(groupIds);
        return collaboratorIds;
    }

    Map<Boolean, List<UpdateMemberResult>> mapIsSuccessResults(List<UpdateMemberResult> totalResults) {
        return totalResults.stream().collect(Collectors.partitioningBy(UpdateMemberResult::isSuccess));
    }

    Map<Boolean, List<Subject>> mapIsGroup(List<Subject> models) {
        return models.stream()
                     .collect(Collectors.partitioningBy(subject -> Group.GROUP_IDENTIFIER.equals(subject.getSourceId())));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#loadCurrentCollaborators
     * ()
     */
    @Override
    public void loadCurrentCollaborators() {
        view.maskCollaborators(null);
        Group defaultGroup = groupFactory.getDefaultGroup();
        groupServiceFacade.getMembers(defaultGroup, new AsyncCallback<List<Subject>>() {

            @Override
            public void onFailure(Throwable caught) {
                view.unmaskCollaborators();
            }

            @Override
            public void onSuccess(List<Subject> result) {
                view.unmaskCollaborators();
                if (result != null && !result.isEmpty()) {
                    view.addCollaborators(result);
                }
                eventBus.fireEvent(new CollaboratorsLoadedEvent());
            }

        });

    }

    @Override
    public void setCurrentMode(ManageCollaboratorsView.MODE m) {
        view.setMode(m);
    }

    @Override
    public ManageCollaboratorsView.MODE getCurrentMode() {
        return view.getMode();
    }

    @Override
    public List<Subject> getSelectedSubjects() {
        return view.getSelectedSubjects();
    }

    @Override
    public void onAddGroupSelected(AddGroupSelected event) {
        groupDetailsDialog.get(new AsyncCallback<GroupDetailsDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(GroupDetailsDialog result) {
                result.show();
                result.addGroupSavedHandler(new GroupSaved.GroupSavedHandler() {
                    @Override
                    public void onGroupSaved(GroupSaved event) {
                        Group group = event.getGroup();
                        view.addCollaborators(wrapSubjectInList(groupFactory.convertGroupToSubject(group)));
                    }
                });
            }
        });
    }

    @Override
    public void onGroupNameSelected(GroupNameSelected event) {
        Subject subject = event.getSubject();
        groupDetailsDialog.get(new AsyncCallback<GroupDetailsDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(GroupDetailsDialog result) {
                result.show(subject);
                result.addGroupSavedHandler(new GroupSaved.GroupSavedHandler() {
                    @Override
                    public void onGroupSaved(GroupSaved event) {
                        Group group = event.getGroup();
                        view.updateCollabList(groupFactory.convertGroupToSubject(group));
                    }
                });
            }
        });
    }

    AddMemberToGroupCallback getAddMemberToGroupCallback() {
        return new AddMemberToGroupCallback();
    }

    List<AddMemberToGroupCallback> getAddMemberToGroupCallbackList() {
        return Lists.newArrayList();
    }
}
