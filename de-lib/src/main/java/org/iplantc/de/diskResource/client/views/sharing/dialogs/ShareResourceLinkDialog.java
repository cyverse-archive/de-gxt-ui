package org.iplantc.de.diskResource.client.views.sharing.dialogs;

import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.inject.Inject;

/**
 * @author jstroot
 */
public class ShareResourceLinkDialog extends ClipboardCopyEnabledDialog {


    @Inject
    ShareResourceLinkDialog(final GridView.Presenter.Appearance appearance) {
        setPredefinedButtons(PredefinedButton.OK);
        setHideOnButtonClick(true);
        setResizable(false);
        setSize(appearance.shareLinkDialogWidth(), appearance.shareLinkDialogHeight());

        HorizontalPanel panel = new HorizontalPanel();
    }

    public void show(final String link) {
        textBox.setValue(link);
        textBox.selectAll();
        ensureDebugId(DiskResourceModule.Ids.SHARE_LINK_DLG);
        textBox.getElement()
               .setId(DiskResourceModule.Ids.SHARE_LINK_DLG + DiskResourceModule.Ids.LINK_TEXT);

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
    }

}
