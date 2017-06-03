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
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.gin.ManageCollaboratorsViewFactory;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.collaborators.client.views.dialogs.GroupDetailsDialog;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.resources.client.messages.I18N;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sriram
 * 
 */
public class ManageCollaboratorsPresenter implements ManageCollaboratorsView.Presenter,
                                                     RemoveCollaboratorSelected.RemoveCollaboratorSelectedHandler,
                                                     DeleteGroupSelected.DeleteGroupSelectedHandler,
                                                     AddGroupSelected.AddGroupSelectedHandler,
                                                     GroupNameSelected.GroupNameSelectedHandler,
                                                     UserSearchResultSelected.UserSearchResultSelectedEventHandler {

    @Inject CollaboratorsUtil collaboratorsUtil;
    @Inject EventBus eventBus;
    @Inject IplantAnnouncer announcer;
    @Inject UserInfo userInfo;
    private ManageCollaboratorsViewFactory factory;
    private GroupAutoBeanFactory groupFactory;
    private GroupServiceFacade groupServiceFacade;
    private CollaboratorsServiceFacade collabServiceFacade;
    private GroupView.GroupViewAppearance groupAppearance;
    ManageCollaboratorsView view;
    HandlerRegistration addCollabHandlerRegistration;

    @Inject AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialog;

    @Inject
    public ManageCollaboratorsPresenter(ManageCollaboratorsViewFactory factory,
                                        GroupAutoBeanFactory groupFactory,
                                        GroupServiceFacade groupServiceFacade,
                                        CollaboratorsServiceFacade collabServiceFacade,
                                        GroupView.GroupViewAppearance groupAppearance) {
        this.factory = factory;
        this.groupFactory = groupFactory;
        this.groupServiceFacade = groupServiceFacade;
        this.collabServiceFacade = collabServiceFacade;
        this.groupAppearance = groupAppearance;
    }

    void addEventHandlers() {
        view.addDeleteGroupSelectedHandler(this);
        view.addAddGroupSelectedHandler(this);
        view.addGroupNameSelectedHandler(this);
        view.addUserSearchResultSelectedEventHandler(this);
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
        view.addRemoveCollaboratorSelectedHandler(this);
        loadCurrentCollaborators();
//        updateListView();
        addEventHandlers();
        container.setWidget(view.asWidget());
    }

    @Override
    public void onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
        Subject subject = userSearchResultSelected.getSubject();
        if (!userInfo.getUsername()
                     .equals(subject.getId())) {
            if (!collaboratorsUtil.isCurrentCollaborator(subject, view.getCollaborators())) {
                addAsCollaborators(Arrays.asList(subject));
            }
        } else {
            announcer.schedule(new ErrorAnnouncementConfig(I18N.DISPLAY.collaboratorSelfAdd()));
        }
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
                List<UpdateMemberResult> failures = result.stream()
                                                          .filter(item -> !item.isSuccess())
                                                          .collect(Collectors.toList());
                if (failures != null && !failures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.unableToAddMembers(failures)));
                } else {
                    // remove added models from search results
                    view.addCollaborators(models);
                    String names = getCollaboratorNames(models);

                    announcer.schedule(new SuccessAnnouncementConfig(I18N.DISPLAY.collaboratorAddConfirm(
                            names)));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.addCollabErrorMsg(), caught);
            }
        });

    }

    String getCollaboratorNames(List<Subject> subjects) {
        Stream<Subject> stream = subjects.stream();

        Stream<String> stringStream = stream.map(Subject::getSubjectDisplayName);
        List<String> names = stringStream.collect(Collectors.toList());
        return Joiner.on(", ").join(names);
    }

    @Override
    public void updateListView() {
        view.maskCollabLists(groupAppearance.loadingMask());
        groupServiceFacade.getGroups(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCollabLists();
            }

            @Override
            public void onSuccess(List<Group> result) {
                List<Group> filteredGroups = result.stream()
                                            .filter(group -> !Group.DEFAULT_GROUP.equals(group.getName()))
                                            .collect(Collectors.toList());
                view.addCollabLists(filteredGroups);
                view.unmaskCollabLists();
            }
        });
    }

    @Override
    public void onRemoveCollaboratorSelected(RemoveCollaboratorSelected event) {
        List<Subject> models = event.getSubjects();
        groupServiceFacade.deleteMembers(groupFactory.getDefaultGroup(), models, new AsyncCallback<List<UpdateMemberResult>>() {

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<UpdateMemberResult> failures = result.stream()
                                                          .filter(item -> !item.isSuccess())
                                                          .collect(Collectors.toList());
                if (failures != null && !failures.isEmpty()) {
                    announcer.schedule(new ErrorAnnouncementConfig(groupAppearance.memberDeleteFail(failures)));
                }
                view.removeCollaborators(models);
                String names = getCollaboratorNames(models);
                announcer.schedule(new SuccessAnnouncementConfig(I18N.DISPLAY.collaboratorRemoveConfirm(names)));

            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

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
                view.loadData(result);
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
    public void onDeleteGroupSelected(DeleteGroupSelected event) {
        Group group = event.getGroup();
        if (group == null) {
            return;
        }
        ConfirmMessageBox deleteAlert = new ConfirmMessageBox(groupAppearance.deleteGroupConfirmHeading(group),
                                                              groupAppearance.deleteGroupConfirm(group));
        deleteAlert.show();
        deleteAlert.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    deleteGroup(group);
                }
            }
        });
    }

    void deleteGroup(Group group) {
        view.maskCollabLists(groupAppearance.loadingMask());
        groupServiceFacade.deleteGroup(group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCollabLists();
            }

            @Override
            public void onSuccess(Group result) {
                view.removeCollabList(group);
                announcer.schedule(new SuccessAnnouncementConfig(groupAppearance.groupDeleteSuccess(result)));
                view.unmaskCollabLists();
            }
        });
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
                        view.addCollabLists(Lists.newArrayList(group));
                    }
                });
            }
        });
    }

    @Override
    public void onGroupNameSelected(GroupNameSelected event) {
        Group group = event.getGroup();
        groupDetailsDialog.get(new AsyncCallback<GroupDetailsDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(GroupDetailsDialog result) {
                result.show(group);
                result.addGroupSavedHandler(new GroupSaved.GroupSavedHandler() {
                    @Override
                    public void onGroupSaved(GroupSaved event) {
                        Group group = event.getGroup();
                        view.updateCollabList(group);
                    }
                });
            }
        });
    }
}
