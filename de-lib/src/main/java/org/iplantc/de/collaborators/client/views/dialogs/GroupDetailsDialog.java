package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.views.GroupDetailsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

/**
 * @author aramsey
 */
public class GroupDetailsDialog extends IPlantDialog {

    GroupDetailsView view;
    GroupView.GroupViewAppearance appearance;

    @Inject
    public GroupDetailsDialog(GroupDetailsView view,
                              GroupView.GroupViewAppearance appearance) {
        this.view = view;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.groupDetailsWidth(), appearance.groupDetailsHeight());
        setOnEsc(false);

        add(view);
    }

    public void show(Group group) {
        view.edit(group);
        setHeading(appearance.groupDetailsHeading(group));
        super.show();

        ensureDebugId(CollaboratorsModule.Ids.GROUP_DETAILS_DLG);
    }
}
