package org.iplantc.de.fileViewers.client.callbacks;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.widgets.ContextualHelpPopup;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.QuickTip;


public class ShareAnonymousCallback extends DataCallback<String> {

    public interface ShareAnonymousCallbackAppearance {

        String copyPasteInstructions();

        String ensemblUrl();

        SafeHtml notificationWithContextHelp();

        String sendToEnsemblMenuItem();

        String sendToEnsemblUrlHelp();

    }

    private final IsMaskable container;
    private final File file;
    private final JsonUtil jsonUtil;
    private final ShareAnonymousCallbackAppearance appearance =
            GWT.create(ShareAnonymousCallbackAppearance.class);

    public ShareAnonymousCallback(final File file, final IsMaskable container) {
        this.container = container;
        this.file = file;
        this.jsonUtil = JsonUtil.getInstance();
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        if (container != null) {
            container.unmask();
        }
        ErrorHandler.post("Unable to retrieve URL's for Ensembl.", caught);
    }

    @Override
    public void onSuccess(String result) {
        if (container != null) {
            container.unmask();
        }
        JSONObject obj = jsonUtil.getObject(result);
        JSONObject paths = jsonUtil.getObject(obj, "paths");
        showShareLink(jsonUtil.getString(paths, file.getPath()));
    }


    private void showShareLink(String linkId) {
        // Open dialog window with text selected.
        IPlantDialog dlg = new IPlantDialog(true);
        dlg.setHeading(appearance.sendToEnsemblMenuItem());
        attachHelp(dlg);

        dlg.setHideOnButtonClick(true);
        dlg.setResizable(false);
        dlg.setSize("535", "175");

        FieldLabel fl = new FieldLabel();
        fl.setHTML(SafeHtmlUtils.fromTrustedString(appearance.ensemblUrl()));
        TextField textBox = new TextField();
        textBox.setWidth(500);
        textBox.setReadOnly(true);
        textBox.setValue(linkId);
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

        notification.setWidth(500);
        container.add(notification);
        dlg.setWidget(container);
        dlg.setFocusWidget(textBox);
        dlg.show();
        textBox.selectAll();
    }

    private void attachHelp(final IPlantDialog dlg) {
        final ContextualHelpPopup popup = new ContextualHelpPopup();
        popup.setWidth(450);
        popup.add(new HTML(appearance.sendToEnsemblUrlHelp()));
        dlg.getHelpToolButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                popup.showAt(dlg.getHelpToolButton().getAbsoluteLeft(),
                             dlg.getHelpToolButton().getAbsoluteTop() + 15);
            }
        });
    }

}
