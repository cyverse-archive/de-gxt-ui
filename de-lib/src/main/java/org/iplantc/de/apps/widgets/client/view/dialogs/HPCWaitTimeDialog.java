package org.iplantc.de.apps.widgets.client.view.dialogs;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

public class HPCWaitTimeDialog extends IPlantDialog {
    
    @Inject
    public HPCWaitTimeDialog(AppLaunchView.AppLaunchViewAppearance appearance) {
        setHideOnButtonClick(true);
        setHeading(appearance.waitTimes());
        HTML htm = new HTML();
        htm.setHTML(appearance.hpcAppWaitTimes());
        add(htm);
        setPredefinedButtons(Dialog.PredefinedButton.OK);
    }

    @Override
    public void show() {
        super.show();

        ensureDebugId(AppsModule.Ids.HPC_WAIT_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + AppsModule.Ids.OK_BTN);
    }
}
