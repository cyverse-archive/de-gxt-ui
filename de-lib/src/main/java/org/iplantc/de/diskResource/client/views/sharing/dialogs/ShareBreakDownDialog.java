package org.iplantc.de.diskResource.client.views.sharing.dialogs;

import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.commons.client.views.sharing.SharingAppearance;
import org.iplantc.de.diskResource.client.views.sharing.ShareBreakDownView;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;

/**
 * @author sriram, jstroot
 * 
 */
public class ShareBreakDownDialog extends Dialog {

    private ShareBreakDownView view;

    @Inject
    ShareBreakDownDialog(ShareBreakDownView view,
                         final SharingAppearance appearance) {
        this.view = view;
        setWidget(view);
        setPixelSize(appearance.shareBreakDownDlgWidth(), appearance.shareBreakDownDlgHeight());
        setHideOnButtonClick(true);
        setModal(true);
        setHeading(appearance.whoHasAccess());
    }

    public void show(List<Sharing> shares) {
        view.loadGrid(shares);

        super.show();
    }

    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class.  Use show(List<Sharing>) instead.");
    }
}
