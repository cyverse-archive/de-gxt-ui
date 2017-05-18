package org.iplantc.de.apps.integration.client.gin;

import org.iplantc.de.apps.integration.client.presenter.AppsEditorPresenterImpl;
import org.iplantc.de.apps.integration.client.view.AppEditorToolbar;
import org.iplantc.de.apps.integration.client.view.AppEditorToolbarImpl;
import org.iplantc.de.apps.integration.client.view.AppIntegrationPalette;
import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.apps.integration.client.view.AppsEditorViewImpl;
import org.iplantc.de.apps.integration.client.view.CommandLineOrderingView;
import org.iplantc.de.apps.integration.client.view.deployedComponents.cells.DCNameHyperlinkCell;
import org.iplantc.de.apps.integration.client.view.dialogs.CommandLineOrderingDialog;
import org.iplantc.de.apps.integration.client.view.propertyEditors.ArgumentGroupPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.DecimalInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.DecimalSelectionPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.EnvVarPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.FileInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.FileOutputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.FlagArgumentPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.FolderInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.FolderOutputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.InfoPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.IntegerInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.IntegerSelectionPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.MultiFileInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.MultiFileOutputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.MultiLineTextInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.ReferenceAnnotationPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.ReferenceGenomePropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.ReferenceSequencePropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.TextInputPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.TextSelectionPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.TreeSelectionPropertyEditor;
import org.iplantc.de.apps.integration.client.view.propertyEditors.widgets.AddValidatorDialog;
import org.iplantc.de.apps.integration.client.view.propertyEditors.widgets.ArgumentValidatorEditor;
import org.iplantc.de.apps.integration.client.view.tools.ToolSearchField;
import org.iplantc.de.apps.integration.client.view.widgets.AppTemplateContentPanel;
import org.iplantc.de.resources.client.IplantContextualHelpAccessStyle;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.uiapps.integration.AppIntegrationErrorMessages;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;

/**
 * @author jstroot
 * 
 */
public class AppEditorGinModule extends AbstractGinModule {

    @Provides
    public IplantContextualHelpAccessStyle createContextualHelpAccessStyle() {
    	return IplantResources.RESOURCES.getContxtualHelpStyle();
    }

    @Override
    protected void configure() {
        bind(AppsEditorView.class).to(AppsEditorViewImpl.class);
        bind(AppEditorToolbar.class).to(AppEditorToolbarImpl.class);
        bind(AppsEditorView.Presenter.class).to(AppsEditorPresenterImpl.class);
        bind(AppIntegrationPalette.class);

        // Bind the appearance for the ArgumentGroupEditors
        bind(AppIntegrationErrorMessages.class).to(IplantErrorStrings.class);

        // Bind all the property editors
        bind(FileInputPropertyEditor.class);
        bind(FolderInputPropertyEditor.class);
        bind(MultiFileInputPropertyEditor.class);
        bind(TextInputPropertyEditor.class);
        bind(EnvVarPropertyEditor.class);
        bind(MultiLineTextInputPropertyEditor.class);
        bind(DecimalInputPropertyEditor.class);
        bind(IntegerInputPropertyEditor.class);
        bind(FlagArgumentPropertyEditor.class);
        bind(TextSelectionPropertyEditor.class);
        bind(IntegerSelectionPropertyEditor.class);
        bind(DecimalSelectionPropertyEditor.class);
        bind(TreeSelectionPropertyEditor.class);
        bind(InfoPropertyEditor.class);
        bind(FileOutputPropertyEditor.class);
        bind(FolderOutputPropertyEditor.class);
        bind(MultiFileOutputPropertyEditor.class);
        bind(ReferenceAnnotationPropertyEditor.class);
        bind(ReferenceGenomePropertyEditor.class);
        bind(ReferenceSequencePropertyEditor.class);
        bind(ArgumentGroupPropertyEditor.class);
        bind(ArgumentValidatorEditor.class);
        bind(DCNameHyperlinkCell.class);
        bind(ToolSearchField.class);
        bind(AppTemplateContentPanel.class);
        bind(AddValidatorDialog.class);
        bind(CommandLineOrderingView.class);
        bind(CommandLineOrderingDialog.class);


   }
}
