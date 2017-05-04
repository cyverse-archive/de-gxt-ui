package org.iplantc.de.collaborators.client.presenter;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author aramsey
 */
public class GroupDetailsPresenterImpl implements GroupDetailsView.Presenter {

    private GroupDetailsView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private GroupView.GroupViewAppearance appearance;
    private HandlerManager handlerManager;
    private GroupDetailsView.MODE mode;

    @Inject
    public GroupDetailsPresenterImpl(GroupDetailsView view,
                                     GroupServiceFacade serviceFacade,
                                     GroupAutoBeanFactory factory,
                                     GroupView.GroupViewAppearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.appearance = appearance;
    }

    @Override
    public void go(HasOneWidget container, Group group, GroupDetailsView.MODE mode) {
        this.mode = mode;
        container.setWidget(view);

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
        Group group = view.getGroup();
        if (group == null) {
            return;
        }
        if (GroupDetailsView.MODE.ADD == mode) {
            addGroup(group);
        }
    }

    void addGroup(Group group) {
        serviceFacade.addGroup(group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(Group result) {
                ensureHandlers().fireEvent(new GroupSaved(getGroupList(result)));
            }
        });
    }

    @Override
    public void onAddGroupMemberSelected(AddGroupMemberSelected event) {
        Group group = event.getGroup();
        Collaborator subject = event.getSubject();

        serviceFacade.addMembers(group, subject, new AsyncCallback<Void>() {
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

    List<Group> getGroupList(Group result) {
        return Lists.newArrayList(result);
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
