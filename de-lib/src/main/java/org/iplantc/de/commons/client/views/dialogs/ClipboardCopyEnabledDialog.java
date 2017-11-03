package org.iplantc.de.commons.client.views.dialogs;

import org.iplantc.de.client.util.CopyToClipboardUtil;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * A dialog that provides ability to copy text to clipboard from a textbox
 *
 * Created by sriram on 11/3/17.
 */
public class ClipboardCopyEnabledDialog extends IPlantDialog {


    public interface Appearance {
        String copyPasteInstructions();

        String copyToClipboard();

        String textboxWidth();

        String textboxHeight();

        String errorCopying();
    }

    protected TextBox textBox;
    protected TextButton copyBtn;

    Appearance appearance = GWT.create(Appearance.class);

    public ClipboardCopyEnabledDialog() {
        textBox = new TextBox();
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);

        textBox = new TextBox();
        textBox.setReadOnly(true);
        textBox.setWidth(appearance.textboxWidth());
        textBox.setHeight(appearance.textboxHeight());

        panel.add(textBox);

        if (CopyToClipboardUtil.isSupported()) {
            copyBtn = new TextButton(appearance.copyToClipboard());
            panel.add(copyBtn);
            copyBtn.addSelectHandler(event -> {
                if (!CopyToClipboardUtil.copyToClipboard(textBox.getElement().getId())) {
                    IplantAnnouncer.getInstance()
                                   .schedule(new ErrorAnnouncementConfig(appearance.errorCopying()
                                                                         + appearance.copyPasteInstructions()));
                }

            });
        }

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(panel);
        container.add(new Label(appearance.copyPasteInstructions()));

        setWidget(container);
        setFocusWidget(textBox);
    }

}
