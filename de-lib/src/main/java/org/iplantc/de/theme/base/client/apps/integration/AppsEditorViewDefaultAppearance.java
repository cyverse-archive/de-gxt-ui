package org.iplantc.de.theme.base.client.apps.integration;

import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.uiapps.integration.AppIntegrationMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.IconButton;

/**
 * @author aramsey
 */
public class AppsEditorViewDefaultAppearance implements AppsEditorView.AppsEditorViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantErrorStrings errorStrings;
    private AppIntegrationMessages appIntegrationMessages;
    private AppTemplateWizardAppearance.Resources style;
    private AppsWidgetsContextualHelpMessages contextualHelpMessages;
    private IplantResources iplantResources;
    private AppsWidgetsPropertyPanelLabels propertyPanelLabels;

    public AppsEditorViewDefaultAppearance() {
        this((IplantDisplayStrings)GWT.create(IplantDisplayStrings.class),
             (IplantErrorStrings)GWT.create(IplantErrorStrings.class),
             (AppIntegrationMessages)GWT.create(AppIntegrationMessages.class),
             (AppTemplateWizardAppearance.Resources)GWT.create(AppTemplateWizardAppearance.Resources.class),
             (AppsWidgetsContextualHelpMessages)GWT.create(AppsWidgetsContextualHelpMessages.class),
             (IplantResources)GWT.create(IplantResources.class),
             (AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class));
    }

    public AppsEditorViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                           IplantErrorStrings errorStrings,
                                           AppIntegrationMessages appIntegrationMessages,
                                           AppTemplateWizardAppearance.Resources style,
                                           AppsWidgetsContextualHelpMessages contextualHelpMessages,
                                           IplantResources iplantResources,
                                           AppsWidgetsPropertyPanelLabels propertyPanelLabels) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.errorStrings = errorStrings;
        this.appIntegrationMessages = appIntegrationMessages;
        this.style = style;
        this.contextualHelpMessages = contextualHelpMessages;
        this.iplantResources = iplantResources;
        this.propertyPanelLabels = propertyPanelLabels;

        this.style.css().ensureInjected();
    }

    @Override
    public SafeHtml cannotDeleteLastArgumentGroup() {
        return errorStrings.cannotDeleteLastArgumentGroup();
    }

    @Override
    public String appHeaderSelect() {
        return style.css().appHeaderSelect();
    }

    @Override
    public IconButton getArgListDeleteButton() {
        IconButton argDeleteBtn =
                new IconButton(new IconButton.IconConfig(style.css().delete(), style.css().deleteHover()));
        argDeleteBtn.addStyleName(style.css().deleteBtn());
        return argDeleteBtn;
    }

    @Override
    public String argumentSelect() {
        return style.css().argumentSelect();
    }

    @Override
    public String warning() {
        return iplantDisplayStrings.warning();
    }

    @Override
    public String appContainsErrorsPromptToContinue() {
        return errorStrings.appContainsErrorsPromptToContinue();
    }

    @Override
    public String save() {
        return iplantDisplayStrings.save();
    }

    @Override
    public String unsavedChanges() {
        return iplantDisplayStrings.unsavedChanges();
    }

    @Override
    public String done() {
        return iplantDisplayStrings.done();
    }

    @Override
    public String appContainsErrorsUnableToSave() {
        return errorStrings.appContainsErrorsUnableToSave();
    }

    @Override
    public String unableToSave() {
        return errorStrings.unableToSave();
    }

    @Override
    public ImageResource questionIcon() {
        return MessageBox.ICONS.question();
    }

    @Override
    public ImageResource errorIcon() {
        return MessageBox.ICONS.error();
    }

    @Override
    public String commandLineOrder() {
        return appIntegrationMessages.commandLineOrder();
    }

    @Override
    public SafeHtml argumentLabel() {
        return appIntegrationMessages.argumentLabel();
    }

    @Override
    public SafeHtml orderLabel() {
        return appIntegrationMessages.orderLabel();
    }

    @Override
    public String previewJSON() {
        return appIntegrationMessages.previewJSON();
    }

    @Override
    public String saveSuccessful() {
        return appIntegrationMessages.saveSuccessful();
    }

    @Override
    public String contextualHelp() {
        return iplantResources.getContxtualHelpStyle().contextualHelp();
    }

    @Override
    public SafeHtml appCategorySection() {
        return contextualHelpMessages.appCategorySection();
    }

    @Override
    public String detailsPanelDefaultText() {
        return propertyPanelLabels.detailsPanelDefaultText();
    }

    @Override
    public String detailsPanelHeader(String s) {
        return propertyPanelLabels.detailsPanelHeader(s);
    }

    @Override
    public String paletteHeader() {
        return appIntegrationMessages.paletteHeader();
    }

    @Override
    public String cmdLinePreviewHeader() {
        return appIntegrationMessages.cmdLinePreviewHeader();
    }

    @Override
    public SafeHtml appCategoryFileInput() {
        return contextualHelpMessages.appCategoryFileInput();
    }

    @Override
    public SafeHtml appCategoryLists() {
        return contextualHelpMessages.appCategoryLists();
    }

    @Override
    public SafeHtml appCategoryTextInput() {
        return contextualHelpMessages.appCategoryTextInput();
    }

    @Override
    public SafeHtml appCategoryOutput() {
        return contextualHelpMessages.appCategoryOutput();
    }

    @Override
    public SafeHtml appCategoryReferenceGenome() {
        return contextualHelpMessages.appCategoryReferenceGenome();
    }

    @Override
    public ImageResource inputSection() {
        return iplantResources.inputSection();
    }

    @Override
    public ImageResource inputFileMulti() {
        return iplantResources.inputFileMulti();
    }

    @Override
    public ImageResource inputFile() {
        return iplantResources.inputFile();
    }

    @Override
    public ImageResource inputFolder() {
        return iplantResources.inputFolder();
    }

    @Override
    public ImageResource generalInfoText() {
        return iplantResources.generalInfoText();
    }

    @Override
    public ImageResource inputTextSingle() {
        return iplantResources.inputTextSingle();
    }

    @Override
    public ImageResource inputTextMulti() {
        return iplantResources.inputTextMulti();
    }

    @Override
    public ImageResource inputCheckBox() {
        return iplantResources.inputCheckBox();
    }

    @Override
    public ImageResource inputEnvVar() {
        return iplantResources.inputEnvVar();
    }

    @Override
    public ImageResource inputNumberInteger() {
        return iplantResources.inputNumberInteger();
    }

    @Override
    public ImageResource inputNumberDouble() {
        return iplantResources.inputNumberDouble();
    }

    @Override
    public ImageResource inputSelectSingle() {
        return iplantResources.inputSelectSingle();
    }

    @Override
    public ImageResource inputSelectInteger() {
        return iplantResources.inputSelectInteger();
    }

    @Override
    public ImageResource inputSelectDouble() {
        return iplantResources.inputSelectDouble();
    }

    @Override
    public ImageResource inputSelectGrouped() {
        return iplantResources.inputSelectGrouped();
    }

    @Override
    public ImageResource outputFileName() {
        return iplantResources.outputFileName();
    }

    @Override
    public ImageResource outputFolderName() {
        return iplantResources.outputFolderName();
    }

    @Override
    public ImageResource outputMultiFile() {
        return iplantResources.outputMultiFile();
    }

    @Override
    public ImageResource referenceGenome() {
        return iplantResources.referenceGenome();
    }

    @Override
    public ImageResource referenceSequence() {
        return iplantResources.referenceSequence();
    }

    @Override
    public ImageResource referenceAnnotation() {
        return iplantResources.referenceAnnotation();
    }

    @Override
    public String fileFolderCategoryTitle() {
        return appIntegrationMessages.fileFolderCategoryTitle();
    }

    @Override
    public String textNumericalInputCategoryTitle() {
        return appIntegrationMessages.textNumericalInputCategoryTitle();
    }

    @Override
    public String listsCategoryTitle() {
        return appIntegrationMessages.listsCategoryTitle();
    }

    @Override
    public String outputCategoryTitle() {
        return appIntegrationMessages.outputCategoryTitle();
    }

    @Override
    public String referenceGenomeCategoryTitle() {
        return appIntegrationMessages.referenceGenomeCategoryTitle();
    }

    @Override
    public String grab() {
        return style.css().grab();
    }

    @Override
    public String commandLineDialogWidth() {
        return "640";
    }

    @Override
    public String commandLineDialogHeight() {
        return "480";
    }

    @Override
    public int argumentNameColumnWidth() {
        return 140;
    }

    @Override
    public SafeHtml argumentNameColumnLabel() {
        return appIntegrationMessages.argumentLabel();
    }

    @Override
    public int argumentOrderColumnWidth() {
        return 30;
    }

    @Override
    public SafeHtml argumentOrderColumnLabel() {
        return appIntegrationMessages.orderLabel();
    }

    @Override
    public int getAutoScrollDelay() {
        return 200;
    }

    @Override
    public int getAutoScrollRegionHeight() {
        return 5;
    }

    @Override
    public int getAutoScrollRepeatDelay() {
        return 50;
    }
}
