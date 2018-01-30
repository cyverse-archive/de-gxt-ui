package org.iplantc.de.commons.client.views.dialogs;

import org.iplantc.de.client.util.CopyToClipboardUtil;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

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

        String copyTextBoxWidth();

        String copyTextBoxHeight();

        String copyTextAreaWidth();

        String copyTextAreaHeight();

        String errorCopying();
    }

    protected TextBoxBase textBox;
    protected TextButton copyBtn;

    private String footerText;
    private String promptText;

    Appearance appearance = GWT.create(Appearance.class);

    public ClipboardCopyEnabledDialog(boolean copyMultiLine) {
        if (!copyMultiLine) {
            textBox = new TextBox();
            textBox.setReadOnly(true);
            textBox.setWidth(appearance.copyTextBoxWidth());
            textBox.setHeight(appearance.copyTextBoxHeight());
        } else {
            textBox = new TextArea();
            textBox.setReadOnly(true);
            textBox.setWidth(appearance.copyTextAreaWidth());
            textBox.setHeight(appearance.copyTextAreaHeight());
        }
        textBox.setReadOnly(true);
    }

    public void setPromptText(String text) {
        this.promptText = text;
    }

    public void setFooterText(String text) {
        this.footerText = text;
    }

    public void setCopyText(String text) {
        textBox.setValue(text);
    }

    public void setTextBoxId(String id) {
        textBox.getElement().setId(id);
    }

    @Override
    public void show() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);
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
        if (!Strings.isNullOrEmpty(promptText)) {
            container.add(new HTML(promptText));
        }
        container.add(panel);
        container.add(new Label(appearance.copyPasteInstructions()));
        if (!Strings.isNullOrEmpty(footerText)) {
            container.add(new Label(footerText));
        }
        setWidget(container);
        setFocusWidget(textBox);
        super.show();
    }


}
