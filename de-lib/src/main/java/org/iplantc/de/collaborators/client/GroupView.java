package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupView extends IsWidget {

    interface GroupViewAppearance {

        String addGroup();

        ImageResource addIcon();

        String deleteGroup();

        ImageResource deleteIcon();

        int nameColumnWidth();

        String nameColumnLabel();

        int descriptionColumnWidth();

        String descriptionColumnLabel();

        String groupDialogHeader();

        String groupDialogWidth();

        String groupDialogHeight();
    }

    interface GroupPresenter {

        void go(HasOneWidget container);

        void setViewDebugId(String baseId);
    }

    void addCollabLists(List<Group> result);
}
