package org.iplantc.de.apps.widgets.client.view.editors.arguments;

import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.commons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * @author jstroot
 */
public class MultiLineTextEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextArea textArea;
    IplantValidationConstants validationConstants;

    @Inject
    public MultiLineTextEditor(@Assisted AppTemplateWizardAppearance appearance,
                               IplantValidationConstants validationConstants) {
        super(appearance);
        textArea = new TextArea();
        textArea.setEmptyText(appearance.textInputWidgetEmptyText());
        textArea.addValidator(new CmdLineArgCharacterValidator(validationConstants.restrictedCmdLineArgCharsExclNewline()));
        editorAdapter = new ArgumentEditorConverter<>(textArea, new SplittableToStringConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

    @Override
    public void disableValidations() {
        super.disableValidations();

        textArea.getValidators().clear();
    }
}
