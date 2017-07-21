package org.iplantc.de.teams.client.presenter;

import static org.iplantc.de.teams.client.EditTeamView.SEARCH_MEMBERS_TAG;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class EditTeamPresenterImpl implements EditTeamView.Presenter,
                                              UserSearchResultSelected.UserSearchResultSelectedEventHandler {

    private EditTeamView view;
    private GroupServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private TeamsView.TeamsViewAppearance appearance;

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

        view.edit(group);
    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId);
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
