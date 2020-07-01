package org.iplantc.de.apps.widgets.client.view.dialogs;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
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
        setPredefinedButtons(PredefinedButton.OK);
        getOkButton().setText(appearance.closeButton());
    }

    public void show(String serviceError, String errorMsg) {
        super.show();
        HTML html = new HTML();
        if (ServiceErrorCode.ERR_FORBIDDEN.toString().equals(serviceError)) {
            // VICE access denied intentionally - bad user
            setHeading(appearance.viceDeniedTitle());
            html.setHTML(appearance.viceDeniedBody());
        } else if (ServiceErrorCode.ERR_PERMISSION_NEEDED.toString().equals(serviceError)) {
            // VICE approval required
            setHeading(appearance.viceApprovalTitle());
            html.setHTML(appearance.viceApprovalBody());
        } else {
            // VICE limit reached
            setHeading(appearance.viceLimitTitle());
            html.setHTML(appearance.viceLimitBody(errorMsg));
        }

        add(html);
        ensureDebugId(AppsModule.Ids.VICE_LIMIT_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + AppsModule.Ids.CLOSE_BTN);
    }
}
