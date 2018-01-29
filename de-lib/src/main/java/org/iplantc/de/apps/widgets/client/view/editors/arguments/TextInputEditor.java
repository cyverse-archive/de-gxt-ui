package org.iplantc.de.apps.widgets.client.view.editors.arguments;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.commons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author jstroot
 */
public class TextInputEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextField textField;
    IplantValidationConstants validationConstants;

    @Inject
    public TextInputEditor(@Assisted AppTemplateWizardAppearance appearance,
                           IplantValidationConstants validationConstants) {
        super(appearance);

        textField = new TextField();
        textField.setEmptyText(appearance.textInputWidgetEmptyText());
        textField.addValidator(new CmdLineArgCharacterValidator(validationConstants.restrictedCmdLineChars()));
        editorAdapter = new ArgumentEditorConverter<>(textField, new SplittableToStringConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public void disableValidations() {
        super.disableValidations();

        textField.getValidators().clear();
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        textField.setId(baseID + AppsModule.Ids.APP_LAUNCH_TEXT_INPUT);
    }
}
