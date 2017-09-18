package org.iplantc.de.apps.widgets.client.view.editors;

import org.iplantc.de.apps.widgets.client.gin.factory.ArgumentEditorGinFactory;
import org.iplantc.de.apps.widgets.client.view.AppTemplateForm;
import org.iplantc.de.apps.widgets.client.view.AppTemplateForm.ArgumentEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.DoubleSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FileOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.FolderOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.MultiFileOutputEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceAnnotationEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceGenomeEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ReferenceSequenceEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.SampleArgumentEditor;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.tree.TreeSelectionEditor;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.refGenome.ReferenceGenome;
import org.iplantc.de.client.services.AppBuilderMetadataServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.de.shared.AppsCallback;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import java.util.List;

/**
 * @author jstroot
 */
public class ArgumentEditorFactoryImpl implements AppTemplateForm.ArgumentEditorFactory {
    protected AppTemplateForm.ArgumentEditor subEditor;

    @Inject AppTemplateWizardAppearance appearance;
    @Inject AppBuilderMetadataServiceFacade appMetadataService;

    @Inject AppsWidgetsPropertyPanelLabels appsWidgetsLabels;
    @Inject ReferenceGenomeProperties refGenomeProps;
    @Inject SelectionItemProperties selectionItemProps;
    @Inject ReferenceGenomeProperties referenceGenomeProperties;
    @Inject ArgumentEditorGinFactory argumentEditorGinFactory;

    private EditorChain<Argument, AppTemplateForm.ArgumentEditor> chain;
    private final SimpleContainer con;
    private ListStore<ReferenceGenome> refGenomeListStore;


    @Inject
    public ArgumentEditorFactoryImpl() {
        con = new SimpleContainer();
    }

    @Override
    public Widget asWidget() {
        return con;
    }

    @Override
    public AppTemplateForm.ArgumentEditor createEditorForTraversal() {
        // JDS This should let the editor hierarchy know what the bound paths should be.
        return new SampleArgumentEditor();
    }

    @Override
    public void flush() {

    }

    @Override
    public String getPathElement(AppTemplateForm.ArgumentEditor subEditor) {
        return "";
    }

    @Override
    public ArgumentEditor getSubEditor() {
        return subEditor;
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {
    }

    @Override
    public void setEditorChain(EditorChain<Argument, AppTemplateForm.ArgumentEditor> chain) {
        this.chain = chain;
    }

    @Override
    public void setValue(Argument value) {

        if (subEditor != null) {
            return;
        }
        chain.detach(subEditor);

        switch (value.getType()) {
            case FileInput:
                subEditor = argumentEditorGinFactory.fileInputEditor(appearance);
                break;
            case FolderInput:
                subEditor = argumentEditorGinFactory.folderInputEditor(appearance);
                break;
            case MultiFileSelector:
                subEditor = argumentEditorGinFactory.multiFileInputEditor(appearance);
                break;
            case FileFolderInput:
                subEditor = argumentEditorGinFactory.fileFolderInputEditor(appearance);
                break;
            case EnvironmentVariable:
                subEditor = argumentEditorGinFactory.environmentVariableEditor(appearance);
                break;
            case Flag:
                subEditor = argumentEditorGinFactory.flagEditor(appearance);
                break;
            case Info:
                subEditor = argumentEditorGinFactory.infoEditor(appearance);
                break;
            case MultiLineText:
                subEditor = argumentEditorGinFactory.multiLineTextEditor(appearance);
                break;
            case Integer:
                subEditor = argumentEditorGinFactory.integerInputEditor(appearance);
                break;
            case Double:
                subEditor = argumentEditorGinFactory.doubleInputEditor(appearance);
                break;
            case Text:
                subEditor = argumentEditorGinFactory.textInputEditor(appearance);
                break;
            case TextSelection:
                subEditor = argumentEditorGinFactory.textSelectionEditor(appearance);
                break;
            case IntegerSelection:
                subEditor = argumentEditorGinFactory.integerSelectionEditor(appearance);
                break;
            case DoubleSelection:
                subEditor = new DoubleSelectionEditor(appearance);
                break;
            case TreeSelection:
                subEditor = new TreeSelectionEditor(appearance, selectionItemProps);
                break;
            case FileOutput:
                subEditor = new FileOutputEditor(appearance, appsWidgetsLabels);
                break;
            case FolderOutput:
                subEditor = new FolderOutputEditor(appearance, appsWidgetsLabels);
                break;
            case MultiFileOutput:
                subEditor = new MultiFileOutputEditor(appearance, appsWidgetsLabels);
                break;
            case ReferenceGenome:
                subEditor = new ReferenceGenomeEditor(appearance, getReferenceGenomeStore(), refGenomeProps);
                break;
            case ReferenceSequence:
                subEditor = new ReferenceSequenceEditor(appearance, getReferenceGenomeStore(), refGenomeProps);
                break;
            case ReferenceAnnotation:
                subEditor = new ReferenceAnnotationEditor(appearance, getReferenceGenomeStore(), refGenomeProps);
                break;
            default:
                throw new IllegalStateException("Argument type must be known");
        }
        con.add(subEditor);
        Widget parent = con.getParent();
        if ((parent != null) && (parent instanceof ResizeContainer)) {
            ((ResizeContainer)parent).forceLayout();
        }
        subEditor.disableValidations();
        chain.attach(value, subEditor);
        subEditor.enableValidations();
    }

    private ListStore<ReferenceGenome> getReferenceGenomeStore() {
        if (refGenomeListStore == null) {
            refGenomeListStore = new ListStore<>(referenceGenomeProperties.id());

            appMetadataService.getReferenceGenomes(new AppsCallback<List<ReferenceGenome>>() {

                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(List<ReferenceGenome> result) {
                    if (refGenomeListStore.getAll().isEmpty()) {
                        refGenomeListStore.addAll(result);
                        refGenomeListStore.addSortInfo(new Store.StoreSortInfo<>(referenceGenomeProperties.nameValue(), SortDir.ASC));
                    }
                }
            });
        }
        return refGenomeListStore;
    }
}
