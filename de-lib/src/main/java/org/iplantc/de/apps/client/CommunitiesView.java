package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

/**
 * @author aramsey
 */
public interface CommunitiesView extends IsWidget,
                                         IsMaskable,
                                         CommunitySelectionChangedEvent.HasCommunitySelectionChangedEventHandlers {
    interface Appearance {

        void setTreeIcons(TreeStyle style);

        String communities();

        String loadingMask();
    }


    interface Presenter extends CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler,
                                CommunitySelectionChangedEvent.HasCommunitySelectionChangedEventHandlers {

        Group getSelectedCommunity();

        CommunitiesView getView();

        void go(HasId selectedCommunity,
                DETabPanel deTabPanel);

        void setViewDebugId(String baseID);
    }

    interface AppCategoryHierarchyProvider {
        List<String> getGroupHierarchy(AppCategory appCategory);
    }

    Tree<Group, String> getTree();
}
