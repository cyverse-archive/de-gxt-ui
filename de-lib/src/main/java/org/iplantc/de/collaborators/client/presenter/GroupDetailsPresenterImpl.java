package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.DeleteMembersSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.client.views.dialogs.RetainPermissionsDialog;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class GroupDetailsPresenterImpl implements GroupDetailsView.Presenter {

    private GroupDetailsView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private ManageCollaboratorsView.Appearance appearance;
    private HandlerManager handlerManager;
    GroupDetailsView.MODE mode;
    Group originalGroup;

    @Inject AsyncProviderWrapper<RetainPermissionsDialog> permissionsDlgProvider;

    @Inject IplantAnnouncer announcer;
    @Inject UserInfo userInfo;

    @Inject
    public GroupDetailsPresenterImpl(GroupDetailsView view,
                                     GroupServiceFacade serviceFacade,
                                     GroupAutoBeanFactory factory,
                                     ManageCollaboratorsView.Appearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;
        
        view.addAddGroupMemberSelectedHandler(this);
        view.addDeleteMembersSelectedHandler(this);
    }

    @Override
    public void go(HasOneWidget container, Subject subject, GroupDetailsView.MODE mode) {
        this.mode = mode;
        container.setWidget(view);
        Group group = factory.convertSubjectToGroup(subject);

        getGroupMembers(group);
    }

    void getGroupMembers(Group group) {
        if (GroupDetailsView.MODE.EDIT == mode) {
            this.originalGroup = getGroupCopy(group);
            view.mask(appearance.loadingMask());
            serviceFacade.getListMembers(group, new AsyncCallback<List<Subject>>() {
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

    Group getGroupCopy(Group group) {
        Group copy = factory.getGroup().as();
        copy.setName(group.getName());
        copy.setDescription(group.getDescription());
        return copy;
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
        serviceFacade.addList(group, new AsyncCallback<Group>() {
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
        serviceFacade.updateGroup(originalGroup.getName(), group, new AsyncCallback<Group>() {
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
        Subject subject = event.getSubject();

        if (subject != null) {
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
            serviceFacade.addMembers(originalGroup, wrapSubjectInList(subject), new AsyncCallback<List<UpdateMemberResult>>() {
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
            return originalGroup.getName().equals(subject.getSubjectDisplayName());
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
                permissionsDlgProvider.get(new AsyncCallback<RetainPermissionsDialog>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        ErrorHandler.post(throwable);
                    }

                    @Override
                    public void onSuccess(RetainPermissionsDialog dialog) {
                        dialog.show();
                        dialog.addDialogHideHandler(dialogHideEvent -> {
                            Dialog.PredefinedButton button = dialogHideEvent.getHideButton();
                            switch(button) {
                                case YES:
                                    deleteMembers(subjects, group, true);
                                    break;
                                case NO:
                                    deleteMembers(subjects, group, false);
                                    break;
                                default:
                                    dialog.hide();
                                    break;
                            }
                        });
                    }
                });
            }
        }
    }

    void deleteMembers(List<Subject> subjects, Group group, boolean retainPermissions) {
        serviceFacade.deleteMembers(group, subjects, retainPermissions, new AsyncCallback<List<UpdateMemberResult>>() {
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
