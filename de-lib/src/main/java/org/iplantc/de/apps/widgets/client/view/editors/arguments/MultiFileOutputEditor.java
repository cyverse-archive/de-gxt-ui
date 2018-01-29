package org.iplantc.de.apps.widgets.client.view.editors.arguments;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.commons.client.validators.DiskResourceUnixGlobValidator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.form.TextField;

public class MultiFileOutputEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextField textField;

    @Inject
    public MultiFileOutputEditor(@Assisted AppTemplateWizardAppearance appearance) {
        super(appearance);
        textField = new TextField();
        textField.setEmptyText(appearance.multiFileOutputEmptyText());
        textField.addValidator(new DiskResourceUnixGlobValidator());
        editorAdapter = new ArgumentEditorConverter<String>(textField, new SplittableToStringConverter());

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

        textField.setId(baseID + AppsModule.Ids.APP_LAUNCH_MULTI_FILE_OUTPUT);
    }
}
