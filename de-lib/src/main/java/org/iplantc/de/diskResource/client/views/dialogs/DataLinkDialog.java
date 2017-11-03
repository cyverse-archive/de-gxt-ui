package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

/**
 * A dialog that simply shows the user a public link to a disk resource for the purpose of copying the URL
 */
public class DataLinkDialog extends ClipboardCopyEnabledDialog {

    private DataLinkView.Appearance appearance;

    @Inject
    public DataLinkDialog(DataLinkView.Appearance appearance) {
        this.appearance = appearance;

        setHeading(appearance.dataLinkTitle());
        setHideOnButtonClick(true);
        setResizable(false);
        setPredefinedButtons(Dialog.PredefinedButton.OK);
        setSize(appearance.copyDataLinkDlgWidth(), appearance.copyDataLinkDlgHeight());
    }


    public void show(String url) {
        textBox.setValue(url);
        setFocusWidget(textBox);

        ensureDebugId(DiskResourceModule.Ids.DATA_LINK_DLG);
        textBox.getElement()
               .setId(DiskResourceModule.Ids.DATA_LINK_DLG + DiskResourceModule.Ids.DATA_LINK_URL);

        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported, use show(String). ");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
        textBox.ensureDebugId(baseID + DiskResourceModule.Ids.DATA_LINK_URL);
    }
}
