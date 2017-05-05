package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
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

    @Inject IplantAnnouncer announcer;

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
    }

    @Override
    public void go(HasOneWidget container, Group group, GroupDetailsView.MODE mode) {
        this.mode = mode;
        container.setWidget(view);

        getGroupMembers(group);
    }

    void getGroupMembers(Group group) {
        if (GroupDetailsView.MODE.EDIT == mode) {
            serviceFacade.getMembers(group, new AsyncCallback<List<Collaborator>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(List<Collaborator> result) {
                    view.addMembers(result);
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
    public void clearHandlers() {
        view.clearHandlers();
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
                List<Collaborator> subjects = view.getCollaborators();
                updateGroupMembers(group, subjects);
            }
        });
    }

    void updateGroup(Group group) {
        serviceFacade.updateGroup(group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Group result) {
                ensureHandlers().fireEvent(new GroupSaved(result));
                List<Collaborator> subjects = view.getCollaborators();
                updateGroupMembers(result, subjects);
            }
        });
    }

    void updateGroupMembers(Group group, List<Collaborator> subjects) {
        if (subjects != null && !subjects.isEmpty()) {
            serviceFacade.updateMembers(group, subjects, new AsyncCallback<List<UpdateMemberResult>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                    view.unmask();
                }

                @Override
                public void onSuccess(List<UpdateMemberResult> result) {
                    List<UpdateMemberResult> failures = result.stream()
                                                             .filter(item -> !item.isSuccess())
                                                             .collect(Collectors.toList());
                    if (failures == null || !failures.isEmpty()) {
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
        Collaborator subject = event.getSubject();

        if (group != null && !Strings.isNullOrEmpty(group.getName()) && subject != null) {
            serviceFacade.addMember(group, subject, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(Void result) {
                    view.addMembers(Lists.newArrayList(subject));
                }
            });
        }
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
