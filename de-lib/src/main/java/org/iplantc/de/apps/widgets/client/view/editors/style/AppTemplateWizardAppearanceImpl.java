package org.iplantc.de.apps.widgets.client.view.editors.style;

import org.iplantc.de.apps.widgets.client.view.util.IPlantSimpleHtmlSanitizer;
import org.iplantc.de.client.models.HasLabel;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.uiapps.integration.AppIntegrationMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

import java.util.List;

/**
 * @author jstroot
 * 
 */
public class AppTemplateWizardAppearanceImpl implements AppTemplateWizardAppearance {

    private final AppsWidgetsContextualHelpMessages help;
    private IplantErrorStrings errorStrings;
    private IplantDisplayStrings iplantDisplayStrings;
    private AppIntegrationMessages appIntegrationMessages;
    private final AppsWidgetsPropertyPanelLabels labels;
    private final Resources res;
    private final AppTemplateWizardTemplates templates;

    public AppTemplateWizardAppearanceImpl() {
        this(GWT.<Resources> create(Resources.class),
             GWT.<AppTemplateWizardTemplates> create(AppTemplateWizardTemplates.class),
             GWT.<AppsWidgetsPropertyPanelLabels> create(AppsWidgetsPropertyPanelLabels.class),
             GWT.<AppsWidgetsContextualHelpMessages> create(AppsWidgetsContextualHelpMessages.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<AppIntegrationMessages> create(AppIntegrationMessages.class));
    }

    public AppTemplateWizardAppearanceImpl(Resources resources,
                                           AppTemplateWizardTemplates templates,
                                           AppsWidgetsPropertyPanelLabels labels,
                                           AppsWidgetsContextualHelpMessages helpMessages,
                                           IplantErrorStrings errorStrings,
                                           IplantDisplayStrings iplantDisplayStrings,
                                           AppIntegrationMessages appIntegrationMessages) {

        this.res = resources;
        this.templates = templates;
        this.labels = labels;
        this.help = helpMessages;
        this.errorStrings = errorStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appIntegrationMessages = appIntegrationMessages;
    }

    @Override
    public SafeHtml createContentPanelHeaderLabel(SafeHtml label, boolean required) {
        if (required) {
            return templates.contentPanelHeaderRequired(label);
        }
        return templates.contentPanelHeader(label);
    }

    @Override
    public SafeHtml createContextualHelpLabel(String labelToolTipText, String propertyToolTip) {
        return templates.fieldLabelImgFloatRight(SafeHtmlUtils.fromString(labelToolTipText), res.help().getSafeUri(), propertyToolTip);
    }

    @Override
    public SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip) {
        return templates.fieldLabelImg(SafeHtmlUtils.fromString(label), res.help().getSafeUri(), toolTip);
    }

    @Override
    public int getAppNameCharLimit() {
        return 255;
    }

    @Override
    public IconButton getArgListDeleteButton() {
        IconButton argDeleteBtn = new IconButton(new IconConfig(res.css().delete(), res.css().deleteHover()));
        argDeleteBtn.addStyleName(res.css().deleteBtn());
        return argDeleteBtn;
    }

    @Override
    public int getAutoExpandOnHoverDelay() {
        return 500;
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

    @Override
    public AppsWidgetsContextualHelpMessages getContextHelpMessages() {
        return help;
    }

    @Override
    public int getDefaultArgListHeight() {
        return 200;
    }

    @Override
    public int getDefaultTreeSelectionHeight() {
        return 200;
    }

    @Override
    public ImageElement getErrorIconImg() {
        ImageElement errIconImg = Document.get().createImageElement();
        errIconImg.setSrc(res.exclamation().getSafeUri().asString());
        errIconImg.getStyle().setFloat(Float.LEFT);
        return errIconImg;
    }

    @Override
    public ImageElement getErrorIconImgWithErrQTip(List<EditorError> errors) {
        ImageElement errIconImg = getErrorIconImg();
        String errorString = "";
        for (EditorError err : errors) {
            if (err instanceof HasLabel) {
                errorString += ((HasLabel)err).getLabel() + ": ";
            }
            errorString += err.getMessage();
            if (errors.indexOf(err) != errors.size() - 1) {
                errorString += "<br>";
            }
        }
        errIconImg.setAttribute("qtip", errorString);
        return errIconImg;
    }

    @Override
    public AppsWidgetsPropertyPanelLabels getPropertyPanelLabels() {
        return labels;
    }

    @Override
    public Style getStyle() {
        res.css().ensureInjected();
        return res.css();
    }

    @Override
    public AppTemplateWizardTemplates getTemplates() {
        return templates;
    }

    @Override
    public SafeHtml getRequiredFieldLabel() {
        return templates.fieldLabelRequired();
    }

    @Override
    public SafeHtml getContextualHelpLabel(SafeHtml label, String contextualHelp) {
        return templates.fieldLabelImgFloatRight(label, res.info().getSafeUri(), contextualHelp);
    }

    @Override
    public SafeHtml sanitizeHtml(String html) {
        return IPlantSimpleHtmlSanitizer.sanitizeHtml(html);
    }

    @Override
    public SafeHtml cannotDeleteLastArgumentGroup() {
        return errorStrings.cannotDeleteLastArgumentGroup();
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
    public String warning() {
        return iplantDisplayStrings.warning();
    }

    @Override
    public String appContainsErrorsPromptToContinue() {
        return errorStrings.appContainsErrorsPromptToContinue();
    }

    @Override
    public ImageResource getErrorIcon() {
        return MessageBox.ICONS.error();
    }

    @Override
    public ImageResource getQuestionIcon() {
        return MessageBox.ICONS.question();
    }

    @Override
    public String previewJSON() {
        return appIntegrationMessages.previewJSON();
    }

    @Override
    public String appContainsErrorsUnableToSave() {
        return errorStrings.appContainsErrorsUnableToSave();
    }

    @Override
    public String saveSuccessful() {
        return appIntegrationMessages.saveSuccessful();
    }

    @Override
    public String unableToSave() {
        return errorStrings.unableToSave();
    }

    @Override
    public String commandLineOrder() {
        return appIntegrationMessages.commandLineOrder();
    }

    @Override
    public String done() {
        return iplantDisplayStrings.done();
    }

    @Override
    public String commandLineDialogWidth() {
        return "640";
    }

    @Override
    public String commandLineDialogHeight() {
        return "480";
    }

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
    public SafeHtml createChkBoxContextualHelpLabel(String labelToolTipText, String propertyToolTip) {
        return templates.fieldLabelImgFloatRightChkBox(SafeHtmlUtils.fromString(labelToolTipText),
                                                 res.help().getSafeUri(),
                                                 propertyToolTip);
    }

    @Override
    public SafeHtml createChkBoxContextualHelpLabelNoFloat(String label, String toolTip) {
        return templates.fieldLabelImgChkBox(SafeHtmlUtils.fromString(label),
                                             res.help().getSafeUri(),
                                             toolTip);
    }

    @Override
    public SafeHtml getChkBoxContextualHelpLabel(SafeHtml label, String contextualHelp) {
        return templates.fieldLabelImgFloatRightChkBox(label, res.info().getSafeUri(), contextualHelp);
    }

}
