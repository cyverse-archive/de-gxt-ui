package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;
import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.de.resources.client.uiapps.widgets.ArgumentValidatorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * @author aramsey
 */
public class PropertyEditorDefaultAppearance implements PropertyEditorAppearance {

    interface Templates extends SafeHtmlTemplates {
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


    private AppsWidgetsPropertyPanelLabels propertyPanelLabels;
    private AppsEditorPanelAppearance panelAppearance;
    private ArgumentValidatorMessages argumentValidatorMessages;
    private AppsWidgetsContextualHelpMessages contextualHelpMessages;
    private Templates templates;
    private IplantResources iplantResources;
    private PropertyEditorDisplayStrings displayStrings;
    private IplantDisplayStrings iplantDisplayStrings;

    public PropertyEditorDefaultAppearance() {
        this ((AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class),
              (AppsEditorPanelAppearance)GWT.create(AppsEditorPanelAppearance.class),
              (ArgumentValidatorMessages)GWT.create(ArgumentValidatorMessages.class),
              (AppsWidgetsContextualHelpMessages)GWT.create(AppsWidgetsContextualHelpMessages.class),
              (Templates)GWT.create(Templates.class),
              (IplantResources)GWT.create(IplantResources.class),
              (PropertyEditorDisplayStrings)GWT.create(PropertyEditorDisplayStrings.class),
              (IplantDisplayStrings)GWT.create(IplantDisplayStrings.class));
    }

    public PropertyEditorDefaultAppearance(AppsWidgetsPropertyPanelLabels propertyPanelLabels,
                                           AppsEditorPanelAppearance panelAppearance,
                                           ArgumentValidatorMessages argumentValidatorMessages,
                                           AppsWidgetsContextualHelpMessages contextualHelpMessages,
                                           Templates templates,
                                           IplantResources iplantResources,
                                           PropertyEditorDisplayStrings displayStrings,
                                           IplantDisplayStrings iplantDisplayStrings) {
        this.propertyPanelLabels = propertyPanelLabels;
        this.panelAppearance = panelAppearance;
        this.argumentValidatorMessages = argumentValidatorMessages;
        this.contextualHelpMessages = contextualHelpMessages;
        this.templates = templates;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public String getPropertyDetailsPanelHeader(String value) {
        return propertyPanelLabels.detailsPanelHeader(value);
    }

    @Override
    public AppsEditorPanelAppearance panelAppearance() {
        return panelAppearance;
    }

    @Override
    public String groupNameLabel() {
        return propertyPanelLabels.groupNameLabel();
    }

    @Override
    public String groupNameEmptyText() {
        return propertyPanelLabels.groupNameEmptyText();
    }

    @Override
    public String groupDelete() {
        return propertyPanelLabels.groupDelete();
    }

    @Override
    public String regex(String regex) {
        return argumentValidatorMessages.regex(regex);
    }

    @Override
    public String characterLimit(int limit) {
        return argumentValidatorMessages.characterLimit(limit);
    }

    @Override
    public String intAbove(int above) {
        return argumentValidatorMessages.intAbove(above);
    }

    @Override
    public String intBelow(int below) {
        return argumentValidatorMessages.intBelow(below);
    }

    @Override
    public String intRange(int above, int below) {
        return argumentValidatorMessages.intRange(above, below);
    }

    @Override
    public String dblAbove(double above) {
        return argumentValidatorMessages.dblAbove(above);
    }

    @Override
    public String dblBelow(double below) {
        return argumentValidatorMessages.dblBelow(below);
    }

    @Override
    public String dblRange(double above, double below) {
        return argumentValidatorMessages.dblRange(above, below);
    }

    @Override
    public String ruleType() {
        return argumentValidatorMessages.ruleType();
    }

    @Override
    public String valueRegex() {
        return argumentValidatorMessages.valueRegex();
    }

    @Override
    public String valueCharLimit() {
        return argumentValidatorMessages.valueCharLimit();
    }

    @Override
    public String valueAbove() {
        return argumentValidatorMessages.valueAbove();
    }

    @Override
    public String valueBelow() {
        return argumentValidatorMessages.valueBelow();
    }

    @Override
    public String valueBetween() {
        return argumentValidatorMessages.valueBetween();
    }

    @Override
    public String valueBetweenAnd() {
        return argumentValidatorMessages.valueBetweenAnd();
    }

    @Override
    public String validatorDialogHeading() {
        return argumentValidatorMessages.validatorDialogHeading();
    }

    @Override
    public String regexLabel() {
        return argumentValidatorMessages.regexLabel();
    }

    @Override
    public String characterLimitLabel() {
        return argumentValidatorMessages.characterLimitLabel();
    }

    @Override
    public String intAboveLabel() {
        return argumentValidatorMessages.intAboveLabel();
    }

    @Override
    public String intBelowLabel() {
        return argumentValidatorMessages.intBelowLabel();
    }

    @Override
    public String intRangeLabel() {
        return argumentValidatorMessages.intRangeLabel();
    }

    @Override
    public String dblAboveLabel() {
        return argumentValidatorMessages.dblAboveLabel();
    }

    @Override
    public String dblBelowLabel() {
        return argumentValidatorMessages.dblBelowLabel();
    }

    @Override
    public String dblRangeLabel() {
        return argumentValidatorMessages.dblRangeLabel();
    }

    @Override
    public String validatorDialogWidth() {
        return "400";
    }

    @Override
    public String validatorDialogHeight() {
        return "250";
    }

    @Override
    public String textInputValidationRules() {
        return contextualHelpMessages.textInputValidationRules();
    }

    @Override
    public String validatorRulesLabel() {
        return propertyPanelLabels.validatorRulesLabel();
    }

    @Override
    public SafeHtml createContextualHelpLabel(String label, String toolTip) {
        return templates.fieldLabelImgFloatRight(SafeHtmlUtils.fromString(label), helpIcon().getSafeUri(), toolTip);
    }

    @Override
    public ImageResource helpIcon() {
        return iplantResources.help();
    }

    @Override
    public String argumentValidatorNameColumn() {
        return displayStrings.argumentValidatorNameColumn();
    }

    @Override
    public String add() {
        return iplantDisplayStrings.add();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public String edit() {
        return iplantDisplayStrings.edit();
    }

    @Override
    public ImageResource editIcon() {
        return iplantResources.edit();
    }

    @Override
    public String delete() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }
}