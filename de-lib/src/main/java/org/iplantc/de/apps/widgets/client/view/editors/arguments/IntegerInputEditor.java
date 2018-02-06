package org.iplantc.de.apps.widgets.client.view.editors.arguments;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToIntegerConverter;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.commons.client.widgets.IPlantSideErrorHandler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;

public class IntegerInputEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<Integer> editorAdapter;
    private final SpinnerField<Integer> spinnerField;

    @Inject
    public IntegerInputEditor(@Assisted AppTemplateWizardAppearance appearance) {
        super(appearance);
        spinnerField = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        spinnerField.setErrorSupport(new IPlantSideErrorHandler(spinnerField));
        spinnerField.setMinValue(Integer.MIN_VALUE);
        spinnerField.setEmptyText(appearance.integerInputWidgetEmptyText());
        editorAdapter = new ArgumentEditorConverter<Integer>(spinnerField, new SplittableToIntegerConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        spinnerField.setId(baseID + AppsModule.Ids.APP_LAUNCH_INTEGER_SPINNER);
    }
}
