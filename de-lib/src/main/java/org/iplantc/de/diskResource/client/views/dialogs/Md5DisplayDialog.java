package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;

public class Md5DisplayDialog extends IPlantPromptDialog {

    @Inject
    public Md5DisplayDialog(GridView.Presenter.Appearance appearance) {
        setHeading(appearance.md5Checksum());
        setFieldLabelText(appearance.checksum());
        setMaxLength(appearance.md5MaxLength());
        setPredefinedButtons(PredefinedButton.OK);
    }

    public void show(String md5) {
        setInitialText(md5);

        super.show();

        ensureDebugId(DiskResourceModule.Ids.MD5_DIALOG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. "
                                                + "Use show(String md5) instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);

    }
}
