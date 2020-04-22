package org.iplantc.de.apps.widgets.client.view.dialogs;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

public class ViceLimitDialog extends IPlantDialog {

    private AppLaunchView.AppLaunchViewAppearance appearance;

    @Inject
    public ViceLimitDialog(AppLaunchView.AppLaunchViewAppearance appearance) {
        this.appearance = appearance;
        setModal(true);
        setPixelSize(350, 200);
        setHideOnButtonClick(true);
        setHeading(appearance.viceLimitTitle());
        setPredefinedButtons(PredefinedButton.OK);
        getOkButton().setText(appearance.closeButton());
    }

    public void show(String errorMsg) {
        super.show();

        HTML html = new HTML();
        html.setHTML(appearance.viceLimitBody(errorMsg));
        add(html);

        ensureDebugId(AppsModule.Ids.VICE_LIMIT_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + AppsModule.Ids.CLOSE_BTN);
    }
}
