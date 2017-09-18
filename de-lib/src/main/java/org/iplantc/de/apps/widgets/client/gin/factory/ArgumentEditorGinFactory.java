package org.iplantc.de.apps.widgets.client.gin.factory;

import org.iplantc.de.apps.widgets.client.view.editors.FileFolderInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.DoubleInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.DoubleSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.EnvironmentVariableEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FileInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FileOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FlagEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FolderInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FolderOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.InfoEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.IntegerInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.IntegerSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.MultiFileInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.MultiFileOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.MultiLineTextEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceAnnotationEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceGenomeEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceSequenceEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.TextInputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.TextSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.tree.TreeSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

/**
 * @author jstroot
 */
public interface ArgumentEditorGinFactory {
    FileInputEditor fileInputEditor(AppTemplateWizardAppearance appearance);
    FolderInputEditor folderInputEditor(AppTemplateWizardAppearance appearance);
    MultiFileInputEditor multiFileInputEditor(AppTemplateWizardAppearance appearance);
    FileFolderInputEditor fileFolderInputEditor(AppTemplateWizardAppearance appearance);
    EnvironmentVariableEditor environmentVariableEditor(AppTemplateWizardAppearance appearance);
    FlagEditor flagEditor(AppTemplateWizardAppearance appearance);
    InfoEditor infoEditor(AppTemplateWizardAppearance appearance);
    MultiLineTextEditor multiLineTextEditor(AppTemplateWizardAppearance appearance);
    IntegerInputEditor integerInputEditor(AppTemplateWizardAppearance appearance);
    DoubleInputEditor doubleInputEditor(AppTemplateWizardAppearance appearance);
    TextInputEditor textInputEditor(AppTemplateWizardAppearance appearance);
    TextSelectionEditor textSelectionEditor(AppTemplateWizardAppearance appearance);
    IntegerSelectionEditor integerSelectionEditor(AppTemplateWizardAppearance appearance);
    DoubleSelectionEditor doubleSelectionEditor(AppTemplateWizardAppearance appearance);
    TreeSelectionEditor treeSelectionEditor(AppTemplateWizardAppearance appearance);
    FileOutputEditor fileOutputEditor(AppTemplateWizardAppearance appearance);
    FolderOutputEditor folderOutputEditor(AppTemplateWizardAppearance appearance);
    MultiFileOutputEditor multiFileOutputEditor(AppTemplateWizardAppearance appearance);
    ReferenceGenomeEditor referenceGenomeEditor(AppTemplateWizardAppearance appearance);
    ReferenceSequenceEditor referenceSequenceEditor(AppTemplateWizardAppearance appearance);
    ReferenceAnnotationEditor referenceAnnotationEditor(AppTemplateWizardAppearance appearance);
}
