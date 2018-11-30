package org.iplantc.de.admin.desktop.client.communities.views.dialogs;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.ManageCommunitiesView;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;

import java.util.List;

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
        setHideOnButtonClick(ManageCommunitiesView.MODE.CREATE == mode);
        if (community != null) {
            setHeading(appearance.editCommunity());
        } else {
            setHeading(appearance.addCommunity());
        }
        super.show();

        ensureDebugId(Belphegor.CommunityIds.EDIT_COMMUNITY_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "This method is not supported. Use 'show(Group)' instead.");
    }

    public Group getUpdatedCommunity() {
        return presenter.getUpdatedCommunity();
    }

    public List<Subject> getSelectedAdmins() {
        return presenter.getCommunityAdmins();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);

        getButton(PredefinedButton.OK).ensureDebugId(baseID + Belphegor.CommunityIds.OK_BTN);
    }
}
