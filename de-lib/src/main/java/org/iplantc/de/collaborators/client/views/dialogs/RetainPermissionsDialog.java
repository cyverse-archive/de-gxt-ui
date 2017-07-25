package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.collaborators.client.ManageCollaboratorsView;

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
    }
}
