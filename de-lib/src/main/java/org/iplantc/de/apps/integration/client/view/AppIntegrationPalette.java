package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.commons.client.widgets.ContextualHelpPopup;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * This is a ui component which contains draggable images of the different supported argument types in
 * the App Integration view.
 * 
 * @author jstroot
 * 
 */
public class AppIntegrationPalette extends Composite {

    interface AppIntegrationPaletteUiBinder extends UiBinder<Widget, AppIntegrationPalette> {}

    @UiField ContentPanel fileFolderPanel, textNumericalPanel, listPanel, outputPanel, referenceGenomePanel;

    @UiField ToolButton fileFolderCategoryHelpBtn,
        listsCategoryHelpBtn,
        textNumericalInputCategoryHelpBtn,
        outputCategoryHelpBtn,
        referenceGenomeCategoryHelpBtn;

    @UiField Image flag,
        environmentVariable,
        multiFileSelector,
        fileInput,
        group,
        integerInput,
        treeSelection,
        singleSelect,
        multiLineText,
        text;

    // Expose group drag source for special case handling in AppsIntegrationViewImpl
    AppIntegrationGroupDND grpDragSource;

    @UiField Image info,
        folderInput,
        integerSelection,
        doubleSelection,
        doubleInput,
        fileOutput,
        folderOutput,
        multiFileOutput,
        referenceGenome,
        referenceSequence,
        referenceAnnotation;

    @UiField(provided = true) AppsEditorView.AppsEditorViewAppearance appearance;
    private boolean onlyLabelEditMode;
    private final AppIntegrationPaletteUiBinder uiBinder = GWT.create(AppIntegrationPaletteUiBinder.class);

    @Inject
    public AppIntegrationPalette(final AppsEditorView.AppsEditorViewAppearance appearance) {
        this.appearance = appearance;
        initWidget(uiBinder.createAndBindUi(this));

        setUpDND();
    }

    void setUpDND() {
        grpDragSource = new AppIntegrationGroupDND(this, group);

        // Add dragSource objects to each button
        new AppIntegrationItemDND(this, environmentVariable, ArgumentType.EnvironmentVariable);
        new AppIntegrationItemDND(this, fileInput, ArgumentType.FileInput);
        new AppIntegrationItemDND(this, flag, ArgumentType.Flag);
        new AppIntegrationItemDND(this, integerInput, ArgumentType.Integer);
        new AppIntegrationItemDND(this, multiFileSelector, ArgumentType.MultiFileSelector);
        new AppIntegrationItemDND(this, multiLineText, ArgumentType.MultiLineText);
        new AppIntegrationItemDND(this, text, ArgumentType.Text);
        new AppIntegrationItemDND(this, singleSelect, ArgumentType.TextSelection);
        new AppIntegrationItemDND(this, treeSelection, ArgumentType.TreeSelection);
        new AppIntegrationItemDND(this, info, ArgumentType.Info);
        new AppIntegrationItemDND(this, folderInput, ArgumentType.FolderInput);
        new AppIntegrationItemDND(this, integerSelection, ArgumentType.IntegerSelection);
        new AppIntegrationItemDND(this, doubleSelection, ArgumentType.DoubleSelection);
        new AppIntegrationItemDND(this, doubleInput, ArgumentType.Double);
        new AppIntegrationItemDND(this, fileOutput, ArgumentType.FileOutput);
        new AppIntegrationItemDND(this, folderOutput, ArgumentType.FolderOutput);
        new AppIntegrationItemDND(this, multiFileOutput, ArgumentType.MultiFileOutput);
        new AppIntegrationItemDND(this, referenceGenome, ArgumentType.ReferenceGenome);
        new AppIntegrationItemDND(this, referenceAnnotation, ArgumentType.ReferenceAnnotation);
        new AppIntegrationItemDND(this, referenceSequence, ArgumentType.ReferenceSequence);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        fileFolderPanel.ensureDebugId(baseID + Ids.FILE_FOLDER_PANEL);
        fileFolderCategoryHelpBtn.ensureDebugId(baseID + Ids.FILE_FOLDER_PANEL + Ids.HELP_BTN);
        getCollapseBtn(fileFolderPanel).ensureDebugId(baseID + Ids.FILE_FOLDER_PANEL + Ids.COLLAPSE_BTN);

        textNumericalPanel.ensureDebugId(baseID + Ids.TEXT_NUMERICAL_PANEL);
        textNumericalInputCategoryHelpBtn.ensureDebugId(baseID + Ids.TEXT_NUMERICAL_PANEL + Ids.HELP_BTN);
        getCollapseBtn(textNumericalPanel).ensureDebugId(baseID + Ids.TEXT_NUMERICAL_PANEL + Ids.COLLAPSE_BTN);

        listPanel.ensureDebugId(baseID + Ids.LIST_PANEL);
        listsCategoryHelpBtn.ensureDebugId(baseID + Ids.LIST_PANEL + Ids.HELP_BTN);
        getCollapseBtn(listPanel).ensureDebugId(baseID + Ids.LIST_PANEL + Ids.COLLAPSE_BTN);

        outputPanel.ensureDebugId(baseID + Ids.OUTPUT_PANEL);
        outputCategoryHelpBtn.ensureDebugId(baseID + Ids.OUTPUT_PANEL + Ids.HELP_BTN);
        getCollapseBtn(outputPanel).ensureDebugId(baseID + Ids.OUTPUT_PANEL + Ids.COLLAPSE_BTN);

        referenceGenomePanel.ensureDebugId(baseID + Ids.REFERENCE_GENOME_PANEL);
        referenceGenomeCategoryHelpBtn.ensureDebugId(baseID + Ids.REFERENCE_GENOME_PANEL + Ids.HELP_BTN);
        getCollapseBtn(referenceGenomePanel).ensureDebugId(baseID + Ids.REFERENCE_GENOME_PANEL + Ids.COLLAPSE_BTN);

        group.ensureDebugId(baseID + Ids.GROUP);
        environmentVariable.ensureDebugId(baseID + Ids.ENV_VARIABLE);
        fileInput.ensureDebugId(baseID + Ids.FILE_INPUT);
        flag.ensureDebugId(baseID + Ids.FLAG);
        integerInput.ensureDebugId(baseID + Ids.INTEGER_INPUT);
        multiFileSelector.ensureDebugId(baseID + Ids.MULTI_FILE_SELECTOR);
        multiLineText.ensureDebugId(baseID + Ids.MULTI_LINE_TEXT);
        text.ensureDebugId(baseID + Ids.TEXT);
        singleSelect.ensureDebugId(baseID + Ids.SINGLE_SELECT);
        treeSelection.ensureDebugId(baseID + Ids.TREE_SELECTION);
        info.ensureDebugId(baseID + Ids.INFO);
        folderInput.ensureDebugId(baseID + Ids.FOLDER_INPUT);
        integerSelection.ensureDebugId(baseID + Ids.INTEGER_SELECTION);
        doubleSelection.ensureDebugId(baseID + Ids.DOUBLE_SELECTION);
        doubleInput.ensureDebugId(baseID + Ids.DOUBLE_INPUT);
        fileOutput.ensureDebugId(baseID + Ids.FILE_OUTPUT);
        folderOutput.ensureDebugId(baseID + Ids.FOLDER_OUTPUT);
        multiFileOutput.ensureDebugId(baseID + Ids.MULTI_FILE_OUTPUT);
        referenceGenome.ensureDebugId(baseID + Ids.REFERENCE_GENOME);
        referenceSequence.ensureDebugId(baseID + Ids.REFERENCE_SEQUENCE);
        referenceAnnotation.ensureDebugId(baseID + Ids.REFERENCE_ANNOTATION);
    }

    private Widget getCollapseBtn(ContentPanel panel) {
        int lastWidget = panel.getHeader().getToolCount() - 1;
        return panel.getHeader().getTool(lastWidget);
    }

    public boolean getOnlyLabelEditMode() {
        return onlyLabelEditMode;
    }

    public void setOnlyLabelEditMode(boolean onlyLabelEditMode) {
        this.onlyLabelEditMode = onlyLabelEditMode;
    }

    @UiFactory
    ToolButton createToolButton() {
        return new ToolButton(appearance.contextualHelp());
    }

    @UiHandler({"fileFolderCategoryHelpBtn", "listsCategoryHelpBtn", "textNumericalInputCategoryHelpBtn", "outputCategoryHelpBtn", "referenceGenomeCategoryHelpBtn"})
    void onSelect(SelectEvent event) {
        if (!(event.getSource() instanceof ToolButton)) {
            return;
        }
        ToolButton btn = (ToolButton)event.getSource();
        ContextualHelpPopup popup = new ContextualHelpPopup();
        popup.setWidth(450);
        popup.add(new HTML(getCategoryContextHelp(btn)));
        popup.showAt(btn.getAbsoluteLeft(), btn.getAbsoluteTop() + 15);
    }



    private SafeHtml getCategoryContextHelp(ToolButton btn) {
        SafeHtml ret = null;
        if (btn == fileFolderCategoryHelpBtn) {
            ret = appearance.appCategoryFileInput();
        } else if (btn == listsCategoryHelpBtn) {
            ret = appearance.appCategoryLists();
        } else if (btn == textNumericalInputCategoryHelpBtn) {
            ret = appearance.appCategoryTextInput();
        } else if (btn == outputCategoryHelpBtn) {
            ret = appearance.appCategoryOutput();
        } else if (btn == referenceGenomeCategoryHelpBtn) {
            ret = appearance.appCategoryReferenceGenome();
        }
        return ret;
    }

}
