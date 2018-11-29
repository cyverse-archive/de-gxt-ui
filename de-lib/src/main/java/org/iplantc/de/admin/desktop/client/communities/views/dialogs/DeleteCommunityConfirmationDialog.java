package org.iplantc.de.admin.desktop.client.communities.views.dialogs;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.groups.Group;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

/**
 * @author aramsey
 *
 * A dialog that is presented to admins to confirm they want to delete a community
 */
public class DeleteCommunityConfirmationDialog extends ConfirmMessageBox {

    private AdminCommunitiesView.Appearance appearance;

    @Inject
    public DeleteCommunityConfirmationDialog(AdminCommunitiesView.Appearance appearance) {
        super(appearance.confirmDeleteCommunityTitle(), "");
        this.appearance = appearance;
    }

    public void show(Group selectedCommunity) {
        setMessage(appearance.confirmDeleteCommunityMessage(selectedCommunity.getName()));
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use 'show(Group)' instead.");
    }
}
