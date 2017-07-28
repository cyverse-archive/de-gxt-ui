package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

public class RetainPermissionsDialog extends ConfirmMessageBox {

    private ManageCollaboratorsView.Appearance appearance;

    @Inject
    public RetainPermissionsDialog(ManageCollaboratorsView.Appearance appearance) {
        super(appearance.retainPermissionsHeader(), appearance.retainPermissionsMessage());
        this.appearance = appearance;
        setWidth(appearance.retainPermissionsWidth());

        setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
        getButton(PredefinedButton.YES).setText(appearance.retainPermissionsBtn());
        getButton(PredefinedButton.NO).setText(appearance.removePermissionsBtn());

        ensureDebugId(CollaboratorsModule.Ids.RETAIN_PERMS_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.YES).ensureDebugId(baseID + CollaboratorsModule.Ids.RETAIN_PERMS_BTN);
        getButton(PredefinedButton.NO).ensureDebugId(baseID + CollaboratorsModule.Ids.REMOVE_PERMS_BTN);
        getButton(PredefinedButton.CANCEL).ensureDebugId(baseID + CollaboratorsModule.Ids.CANCEL_BTN);
    }
}
