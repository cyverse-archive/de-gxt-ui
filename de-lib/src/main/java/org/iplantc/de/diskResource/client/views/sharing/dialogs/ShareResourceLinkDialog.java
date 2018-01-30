package org.iplantc.de.diskResource.client.views.sharing.dialogs;

import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @author jstroot
 */
public class ShareResourceLinkDialog extends ClipboardCopyEnabledDialog {


    @Inject
    ShareResourceLinkDialog(final GridView.Presenter.Appearance appearance,
                            @Assisted boolean copyMultiLine) {
        super(copyMultiLine);
        setPredefinedButtons(PredefinedButton.OK);
        setHideOnButtonClick(true);
        setResizable(false);
        if (copyMultiLine) {
            setSize(appearance.shareLinkDialogWidth(), appearance.shareLinkDialogMultiLineHeight());
        } else {
            setSize(appearance.shareLinkDialogWidth(), appearance.shareLinkDialogHeight());
        }
    }

    public void show(final String link) {
        setCopyText(link);
        textBox.selectAll();
        ensureDebugId(DiskResourceModule.Ids.SHARE_LINK_DLG);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. " +
                                                    "Use show(List<DiskResource>) instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getButton(PredefinedButton.OK).asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
        textBox.ensureDebugId(baseID + DiskResourceModule.Ids.LINK_TEXT);
        setTextBoxId(baseID + DiskResourceModule.Ids.LINK_TEXT_INPUT);


    }

}
