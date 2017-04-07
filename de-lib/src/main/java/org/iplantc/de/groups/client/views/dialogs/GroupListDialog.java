package org.iplantc.de.groups.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.groups.shared.GroupsModule;

import javax.inject.Inject;

/**
 * @author aramsey
 */
public class GroupListDialog extends IPlantDialog {

    private final GroupView.GroupPresenter presenter;
    private final GroupView.GroupViewAppearance appearance;

    @Inject
    public GroupListDialog(final GroupView.GroupPresenter presenter,
                           GroupView.GroupViewAppearance appearance) {

        this.presenter = presenter;
        this.appearance = appearance;

        ensureDebugId(GroupsModule.Ids.GROUP_DIALOG);
        setHeading(appearance.groupDialogHeader());
        setSize(appearance.groupDialogWidth(), appearance.groupDialogHeight());
        setMinWidth(Integer.parseInt(appearance.groupDialogWidth()));

        presenter.go(this);
    }


    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID + GroupsModule.Ids.GROUP_VIEW);
    }
}
