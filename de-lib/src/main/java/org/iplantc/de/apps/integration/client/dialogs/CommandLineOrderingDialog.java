package org.iplantc.de.apps.integration.client.dialogs;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.apps.integration.client.view.CommandLineOrderingView;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import java.util.List;

/**
 * @author aramsey
 */
public class CommandLineOrderingDialog extends IPlantDialog {

    private AppTemplateWizardAppearance appearance;
    private CommandLineOrderingView view;
    
    @Inject
    public CommandLineOrderingDialog(AppTemplateWizardAppearance appearance,
                                     CommandLineOrderingView view) {
        this.appearance = appearance;
        this.view = view;

        setPredefinedButtons(PredefinedButton.OK);
        setHeading(appearance.commandLineOrder());
        setModal(true);
        setOkButtonText(appearance.done());
        setAutoHide(false);
        setSize(appearance.commandLineDialogWidth(), appearance.commandLineDialogHeight());
        add(view);
    }

    public void show(List<Argument> argumentList) {
        view.add(argumentList);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported. Use show(List<Argument>) method instead.");
    }
}
