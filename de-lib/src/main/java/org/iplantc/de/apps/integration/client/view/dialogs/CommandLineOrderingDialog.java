package org.iplantc.de.apps.integration.client.view.dialogs;

import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

/**
 * @author aramsey
 */
public class CommandLineOrderingDialog extends IPlantDialog {

    private AppsEditorView.AppsEditorViewAppearance appearance;

    @Inject
    public CommandLineOrderingDialog(AppsEditorView.AppsEditorViewAppearance appearance) {
        this.appearance = appearance;

        setPredefinedButtons(PredefinedButton.OK);
        setHeading(appearance.commandLineOrder());
        setModal(true);
        setOkButtonText(appearance.done());
        setAutoHide(false);
        setSize(appearance.commandLineDialogWidth(), appearance.commandLineDialogHeight());
    }
}
