package org.iplantc.de.apps.integration.client.dialogs;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

/**
 * @author aramsey
 */
public class CommandLineOrderingDialog extends IPlantDialog {

    private AppTemplateWizardAppearance appearance;

    @Inject
    public CommandLineOrderingDialog(AppTemplateWizardAppearance appearance) {
        this.appearance = appearance;

        setPredefinedButtons(PredefinedButton.OK);
        setHeading(appearance.commandLineOrder());
        setModal(true);
        setOkButtonText(appearance.done());
        setAutoHide(false);
        setSize(appearance.commandLineDialogWidth(), appearance.commandLineDialogHeight());
    }
}
