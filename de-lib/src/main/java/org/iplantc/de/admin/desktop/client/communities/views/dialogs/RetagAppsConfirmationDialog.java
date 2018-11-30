package org.iplantc.de.admin.desktop.client.communities.views.dialogs;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.groups.Group;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

/**
 * @author aramsey
 *
 * A dialog that is presented to admins to confirm they want to continue renaming a community and thus
 * retag any apps currently tagged with the old community name
 */
public class RetagAppsConfirmationDialog extends ConfirmMessageBox {

    private AdminCommunitiesView.Appearance appearance;

    @Inject
    public RetagAppsConfirmationDialog(AdminCommunitiesView.Appearance appearance) {
        super(appearance.retagAppsConfirmationTitle(), "");
        this.appearance = appearance;
    }

    public void show(String oldCommunityName) {
        setMessage(appearance.retagAppsCommunityMessage(oldCommunityName));
        super.show();

        ensureDebugId(Belphegor.CommunityIds.RETAG_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use 'show(String)' instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.YES).ensureDebugId(baseID + Belphegor.CommunityIds.YES_BTN);
        getButton(PredefinedButton.NO).ensureDebugId(baseID + Belphegor.CommunityIds.NO_BTN);
    }
}
