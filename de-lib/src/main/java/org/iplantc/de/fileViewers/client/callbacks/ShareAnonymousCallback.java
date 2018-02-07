package org.iplantc.de.fileViewers.client.callbacks;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.dialogs.ClipboardCopyEnabledDialog;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.widgets.ContextualHelpPopup;
import org.iplantc.de.fileViewers.share.FileViewerModule;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;

import java.util.List;
import java.util.stream.Collectors;


public class ShareAnonymousCallback extends DataCallback<List<String>> {

    public interface ShareAnonymousCallbackAppearance {

        String ensemblUrl();

        SafeHtml notificationWithContextHelp();

        int notificationWithContextWidth();

        String sendToEnsemblMenuItem();

        String sendToEnsemblUrlHelp();

        int sendToEnsemblUrlHelpPopupWidth();

        String ensemblUrlDialogWidth();

        String ensemblUrlDialogHeight();

        int ensemblUrlTextAreaWidth();

        int ensemblUrlTextAreaHeight();

        String copyPasteInstructions();
    }

    private final IsMaskable container;
    private final DiskResourceUtil diskResourceUtil;
    private final ShareAnonymousCallbackAppearance appearance =
            GWT.create(ShareAnonymousCallbackAppearance.class);

    public ShareAnonymousCallback(final IsMaskable container, final DiskResourceUtil diskResourceUtil) {
        this.container = container;
        this.diskResourceUtil = diskResourceUtil;
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        if (container != null) {
            container.unmask();
        }
        ErrorHandler.post("Unable to retrieve URL's for Ensembl.", caught);
    }

    @Override
    public void onSuccess(List<String> urls) {
        if (container != null) {
            container.unmask();
        }

        showShareLink(urls.stream()
                          .filter(path -> !diskResourceUtil.isGenomeIndexFile(path))
                          .collect(Collectors.toList()));
    }


    private void showShareLink(List<String> linkIds) {
        ClipboardCopyEnabledDialog dlg = new ClipboardCopyEnabledDialog(true, true);
        attachHelp(dlg);
        dlg.setHeading(appearance.sendToEnsemblMenuItem());
        dlg.setPromptText(appearance.ensemblUrl());
        dlg.setFooterText(appearance.notificationWithContextHelp().asString());
        dlg.setSize(appearance.ensemblUrlDialogWidth(), appearance.ensemblUrlDialogHeight());
        dlg.setCopyText(Joiner.on('\n').join(linkIds));
        dlg.setTextBoxId(FileViewerModule.Ids.FILE_VIEWER_VIEW + FileViewerModule.Ids.SHARE_ANONYMOUS);
        dlg.show();
    }

    private void attachHelp(final IPlantDialog dlg) {
        final ContextualHelpPopup popup = new ContextualHelpPopup();
        popup.setWidth(appearance.sendToEnsemblUrlHelpPopupWidth());
        popup.add(new HTML(appearance.sendToEnsemblUrlHelp()));
        dlg.getHelpToolButton()
           .addSelectHandler(event -> popup.showAt(dlg.getHelpToolButton().getAbsoluteLeft(),
                                                   dlg.getHelpToolButton().getAbsoluteTop() + 15));
    }

}
