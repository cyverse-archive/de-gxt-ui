package org.iplantc.de.commons.client.views.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;

/**
 * @author jstroot
 */
public class IPlantPromptDialog extends IPlantDialog {

    private static IPlantPromptDialogUiBinder uiBinder = GWT.create(IPlantPromptDialogUiBinder.class);

    interface IPlantPromptDialogUiBinder extends UiBinder<Widget, IPlantPromptDialog> {
    }

    @UiField FieldLabel fieldLabel;
    @UiField TextField textField;

    private final TextButton okButton;

    public IPlantPromptDialog() {
        add(uiBinder.createAndBindUi(this));
        okButton = getButton(PredefinedButton.OK);
        okButton.setEnabled(false);
        Scheduler.get().scheduleFinally(() -> {
            setFocusWidget(textField);
            textField.selectAll();
        });
    }

    public IPlantPromptDialog(final String fieldLabelText,
                              final int maxLength,
                              final String initialText,
                              final Validator<String> validator) {
        this();
        setFieldLabelText(fieldLabelText);
        setMaxLength(maxLength);
        setInitialText(initialText);
        addValidator(validator);
    }

    protected void setFieldLabelText(String label) {
        fieldLabel.setText(label);
    }

    protected void setMaxLength(int maxLength) {
        if (maxLength > 0) {
            addValidator(new MaxLengthValidator(maxLength));
        }
    }

    protected void setInitialText(String initialText) {
        textField.setText(initialText);
        textField.selectAll();
    }

    @UiHandler("textField")
    void onKeyUp(KeyUpEvent event) {
        okButton.setEnabled(textField.isCurrentValid());
    }

    @UiHandler("textField")
    void onKeyDown(KeyDownEvent event) {
        if (textField.isCurrentValid() && (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
            onButtonPressed(okButton);
        }
    }

    public void addValidator(Validator<String> validator) {
        if (validator == null) {
            return;
        }
        textField.addValidator(validator);
    }

    public String getFieldText() {
        return textField.getCurrentValue();
    }
}
