package org.iplantc.de.fileViewers.client.callbacks;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.widgets.ContextualHelpPopup;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

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
        // Open dialog window with text selected.
        IPlantDialog dlg = new IPlantDialog(true);
        dlg.setHeading(appearance.sendToEnsemblMenuItem());
        attachHelp(dlg);

        dlg.setHideOnButtonClick(true);
        dlg.setResizable(false);
        dlg.setSize(appearance.ensemblUrlDialogWidth(), appearance.ensemblUrlDialogHeight());

        FieldLabel fl = new FieldLabel();
        fl.setHTML(SafeHtmlUtils.fromTrustedString(appearance.ensemblUrl()));
        TextArea textBox = new TextArea();
        textBox.setPixelSize(appearance.ensemblUrlTextAreaWidth(), appearance.ensemblUrlTextAreaHeight());
        textBox.setReadOnly(true);
        textBox.setValue(Joiner.on('\n').join(linkIds));
        fl.setWidget(textBox);
        fl.setLabelAlign(LabelAlign.TOP);

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(fl);
        container.add(new Label(appearance.copyPasteInstructions()));

        // Use a fl to get html
        FieldLabel notification = new FieldLabel();
        notification.setLabelSeparator("");
        notification.setLabelAlign(LabelAlign.TOP);
        notification.setHTML(appearance.notificationWithContextHelp());
        new QuickTip(notification);

        notification.setWidth(appearance.notificationWithContextWidth());
        container.add(notification);
        dlg.setWidget(container);
        dlg.setFocusWidget(textBox);
        dlg.show();
        textBox.selectAll();
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
