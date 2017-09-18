package org.iplantc.de.theme.base.client.apps.widgets;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.apps.widgets.client.view.util.IPlantSimpleHtmlSanitizer;
import org.iplantc.de.client.models.HasLabel;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.de.resources.client.uiapps.widgets.argumentTypes.EnvironmentVariableLabels;
import org.iplantc.de.resources.client.uiapps.widgets.argumentTypes.TextInputLabels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

import java.util.List;

/**
 * @author jstroot
 * 
 */
public class AppTemplateWizardDefaultAppearance implements AppTemplateWizardAppearance {

    interface AppTemplateWizardTemplates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<p style='text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'>{0}</p>")
        SafeHtml contentPanelHeader(SafeHtml label);

        @SafeHtmlTemplates.Template("<p style='text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'><span style='color: red;'>*&nbsp</span>{0}</p>")
        SafeHtml contentPanelHeaderRequired(SafeHtml label);

        @SafeHtmlTemplates.Template("{0}&nbsp;<img src='{1}' qtip='{2}' ></img>")
        SafeHtml fieldLabelImg(SafeHtml label, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("{0}<img style='float: right;' src='{1}' qtip='{2}'></img>")
        SafeHtml fieldLabelImgFloatRight(SafeHtml label, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("{0}&nbsp;<img src='{1}' title='{2}'></img>")
        SafeHtml fieldLabelImgChkBox(SafeHtml label, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("{0}<img style='float: right;' src='{1}' title='{2}'></img>")
        SafeHtml fieldLabelImgFloatRightChkBox(SafeHtml label, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("<span style='color: red;'>*&nbsp</span>")
        SafeHtml fieldLabelRequired();

    }

    interface Style extends CssResource {

        String appHeaderSelect();

        String argument();

        String argumentSelect();

        String delete();

        String deleteBtn();

        String deleteHover();

        String emptyGroupBgText();

        String grab();

        String grabbing();

    }

    interface Resources extends IplantResources {
        @Source("AppTemplateWizard.gss")
        Style css();

        // KLUDGE Duplicated resource in apps theme as well.
        @Source("delete_rating.png")
        ImageResource deleteRating();

        // KLUDGE Duplicated resource in apps theme as well.
        @Source("delete_rating_hover.png")
        ImageResource deleteRatingHover();
    }

    private final AppsWidgetsContextualHelpMessages help;
    private final AppsWidgetsPropertyPanelLabels labels;
    private final Resources res;
    private final AppTemplateWizardTemplates templates;
    private EnvironmentVariableLabels environmentVariableLabels;
    private TextInputLabels textInputLabels;
    private IplantDisplayStrings iplantDisplayStrings;
    private Style style;

    public AppTemplateWizardDefaultAppearance() {
        this(GWT.<Resources>create(Resources.class),
             GWT.<AppTemplateWizardTemplates>create(AppTemplateWizardTemplates.class),
             GWT.<AppsWidgetsPropertyPanelLabels>create(AppsWidgetsPropertyPanelLabels.class),
             GWT.<AppsWidgetsContextualHelpMessages>create(AppsWidgetsContextualHelpMessages.class),
             GWT.<EnvironmentVariableLabels>create(EnvironmentVariableLabels.class),
             GWT.<TextInputLabels>create(TextInputLabels.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class));
    }

    public AppTemplateWizardDefaultAppearance(Resources res,
                                              AppTemplateWizardTemplates templates,
                                              AppsWidgetsPropertyPanelLabels labels,
                                              AppsWidgetsContextualHelpMessages help,
                                              EnvironmentVariableLabels environmentVariableLabels,
                                              TextInputLabels textInputLabels,
                                              IplantDisplayStrings iplantDisplayStrings) {
        this.help = help;
        this.labels = labels;
        this.res = res;
        this.templates = templates;
        this.environmentVariableLabels = environmentVariableLabels;
        this.textInputLabels = textInputLabels;
        this.iplantDisplayStrings = iplantDisplayStrings;

        this.style = res.css();
        style.ensureInjected();
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
    public String appHeaderSelectClassName() {
        return style.appHeaderSelect();
    }

    @Override
    public String argumentClassName() {
        return style.argument();
    }

    @Override
    public String argumentSelectClassName() {
        return style.argumentSelect();
    }

    @Override
    public String deleteClassName() {
        return style.delete();
    }

    @Override
    public String deleteBtnClassName() {
        return style.deleteBtn();
    }

    @Override
    public String deleteHoverClassName() {
        return style.deleteHover();
    }

    @Override
    public String emptyGroupBgTextClassName() {
        return style.emptyGroupBgText();
    }

    @Override
    public String grabClassName() {
        return style.grab();
    }

    @Override
    public String grabbingClassName() {
        return style.grabbing();
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
    public String multiFilePrompt() {
        return help.multiFilePrompt();
    }

    @Override
    public String treeSelectorFilterEmptyText() {
        return iplantDisplayStrings.treeSelectorFilterEmptyText();
    }

    @Override
    public int treeFilterWidth() {
        return 250;
    }

    @Override
    public String envVarWidgetEmptyText() {
        return environmentVariableLabels.envVarWidgetEmptyText();
    }

    @Override
    public String textInputWidgetEmptyText() {
        return textInputLabels.textInputWidgetEmptyText();
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
