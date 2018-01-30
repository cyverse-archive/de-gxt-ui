package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Dialog;

/**
 * A dialog that simply shows the user a public link to a disk resource for the purpose of copying the URL
 */
public class DataLinkDialog extends ClipboardCopyEnabledDialog {

    private DataLinkView.Appearance appearance;

    @Inject
    public DataLinkDialog(DataLinkView.Appearance appearance, @Assisted boolean copyMultiLine) {
        super(copyMultiLine);
        this.appearance = appearance;

        setHeading(appearance.dataLinkTitle());
        setHideOnButtonClick(true);
        setResizable(false);
        setPredefinedButtons(Dialog.PredefinedButton.OK);
        if (copyMultiLine) {
            setSize(appearance.copyDataLinkDlgWidth(), appearance.copyDataLinkDlgMultiLineHeight());
        } else {
            setSize(appearance.copyDataLinkDlgWidth(), appearance.copyDataLinkDlgHeight());
        }
    }


    public void show(String url) {
        setCopyText(url);
        setFocusWidget(textBox);
        ensureDebugId(DiskResourceModule.Ids.DATA_LINK_DLG);
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
        setTextBoxId(baseID + DiskResourceModule.Ids.DATA_LINK_URL
                      + DiskResourceModule.Ids.DATA_LINk_URL_INPUT);
    }
}
