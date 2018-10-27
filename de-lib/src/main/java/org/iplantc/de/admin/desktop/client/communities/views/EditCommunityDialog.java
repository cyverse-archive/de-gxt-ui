package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.apps.client.ManageCommunitiesView;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * A dialog that can be used to create or edit a community
 */
public class EditCommunityDialog extends IPlantDialog {

    private AdminCommunitiesView.Appearance appearance;
    private ManageCommunitiesView.Presenter presenter;

    @Inject
    public EditCommunityDialog(AdminCommunitiesView.Appearance appearance,
                               ManageCommunitiesView.Presenter presenter) {
        this.appearance = appearance;
        this.presenter = presenter;
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
    }

    @Override
    protected void onButtonPressed(TextButton button) {
        if (button == getButton(PredefinedButton.OK)) {
            if (presenter.isViewValid()) {
                super.onButtonPressed(button);
            }
        } else {
            super.onButtonPressed(button);
        }
    }

    public void show(Group community, ManageCommunitiesView.MODE mode) {
        presenter.go(this, mode);
        presenter.editCommunity(community);
        if (community != null) {
            setHeading(appearance.editCommunity());
        } else {
            setHeading(appearance.addCommunity());
        }
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "This method is not supported. Use 'show(Group)' instead.");
    }

    public Group getUpdatedCommunity() {
        return presenter.getUpdatedCommunity();
    }
}
