package org.iplantc.de.teams.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface EditTeamView extends IsWidget,
                                      IsMaskable,
                                      UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers {

    String SEARCH_MEMBERS_TAG = "members";
    String SEARCH_NON_MEMBERS_TAG = "nonMembers";

    interface Presenter {

        void go(HasOneWidget widget, Group group);

        void setViewDebugId(String debugId);
    }

    void edit(Group group);

    void addNonMembers(List<Privilege> privilegeList);

    void addMembers(List<Privilege> privilegeList);
}
