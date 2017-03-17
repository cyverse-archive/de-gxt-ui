package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;
import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
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
    private AppsWidgetsDisplayMessages appsWidgetsDisplayMessages;

    public PropertyEditorDefaultAppearance() {
        this ((AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class),
              (AppsEditorPanelAppearance)GWT.create(AppsEditorPanelAppearance.class),
              (ArgumentValidatorMessages)GWT.create(ArgumentValidatorMessages.class),
              (AppsWidgetsContextualHelpMessages)GWT.create(AppsWidgetsContextualHelpMessages.class),
              (Templates)GWT.create(Templates.class),
              (IplantResources)GWT.create(IplantResources.class),
              (PropertyEditorDisplayStrings)GWT.create(PropertyEditorDisplayStrings.class),
              (IplantDisplayStrings)GWT.create(IplantDisplayStrings.class),
              (AppsWidgetsDisplayMessages)GWT.create(AppsWidgetsDisplayMessages.class));
    }

    public PropertyEditorDefaultAppearance(AppsWidgetsPropertyPanelLabels propertyPanelLabels,
                                           AppsEditorPanelAppearance panelAppearance,
                                           ArgumentValidatorMessages argumentValidatorMessages,
                                           AppsWidgetsContextualHelpMessages contextualHelpMessages,
                                           Templates templates,
                                           IplantResources iplantResources,
                                           PropertyEditorDisplayStrings displayStrings,
                                           IplantDisplayStrings iplantDisplayStrings,
                                           AppsWidgetsDisplayMessages appsWidgetsDisplayMessages) {
        this.propertyPanelLabels = propertyPanelLabels;
        this.panelAppearance = panelAppearance;
        this.argumentValidatorMessages = argumentValidatorMessages;
        this.contextualHelpMessages = contextualHelpMessages;
        this.templates = templates;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appsWidgetsDisplayMessages = appsWidgetsDisplayMessages;
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

    @Override
    public String singleSelectEditToolTip() {
        return propertyPanelLabels.singleSelectEditToolTip();
    }

    @Override
    public String selectionCreateWidgetEmptyText() {
        return propertyPanelLabels.selectionCreateWidgetEmptyText();
    }

    @Override
    public String singleSelectDisplayColumnHeader() {
        return propertyPanelLabels.singleSelectDisplayColumnHeader();
    }

    @Override
    public String singleSelectNameColumnHeader() {
        return propertyPanelLabels.singleSelectNameColumnHeader();
    }

    @Override
    public String singleSelectValueColumnHeader() {
        return propertyPanelLabels.singleSelectValueColumnHeader();
    }

    @Override
    public String singleSelectIsDefaultColumnHeader() {
        return propertyPanelLabels.singleSelectIsDefaultColumnHeader();
    }

    @Override
    public String singleSelectToolTipColumnHeader() {
        return propertyPanelLabels.singleSelectToolTipColumnHeader();
    }

    @Override
    public String checkCascadeLabel() {
        return appsWidgetsDisplayMessages.checkCascadeLabel();
    }

    @Override
    public String forceSingleSelectLabel() {
        return appsWidgetsDisplayMessages.forceSingleSelectLabel();
    }

    @Override
    public String forceSingleSelectToolTip() {
        return appsWidgetsDisplayMessages.forceSingleSelectToolTip();
    }

    @Override
    public String addGroupToolTip() {
        return appsWidgetsDisplayMessages.addGroupToolTip();
    }

    @Override
    public String addArgumentToolTip() {
        return appsWidgetsDisplayMessages.addArgumentToolTip();
    }

    @Override
    public String cascadeOptionsComboToolTip() {
        return appsWidgetsDisplayMessages.cascadeOptionsComboToolTip();
    }

    @Override
    public ImageResource folderAddIcon() {
        return iplantResources.folderAdd();
    }

    @Override
    public ImageResource cancelIcon() {
        return iplantResources.cancel();
    }

    @Override
    public String doubleInputLabel() {
        return propertyPanelLabels.doubleInputLabel();
    }

    @Override
    public String doubleInputEmptyText() {
        return propertyPanelLabels.doubleInputEmptyText();
    }

    @Override
    public String argumentOptionEmptyText() {
        return propertyPanelLabels.argumentOptionEmptyText();
    }

    @Override
    public String toolTipEmptyText() {
        return propertyPanelLabels.toolTipEmptyText();
    }

    @Override
    public String doubleInputWidgetEmptyEditText() {
        return propertyPanelLabels.doubleInputWidgetEmptyEditText();
    }

    @Override
    public String integerInputDefaultLabel() {
        return propertyPanelLabels.integerInputDefaultLabel();
    }

    @Override
    public String integerInputDefaultValue() {
        return contextualHelpMessages.integerInputDefaultValue();
    }

    @Override
    public String integerInputExcludeArgument() {
        return contextualHelpMessages.integerInputExcludeArgument();
    }

    @Override
    public String excludeWhenEmpty() {
        return propertyPanelLabels.excludeWhenEmpty();
    }

    @Override
    public SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip) {
        return templates.fieldLabelImg(SafeHtmlUtils.fromString(label),
                                       helpIcon().getSafeUri(),
                                       toolTip);
    }

    @Override
    public String toolTip() {
        return contextualHelpMessages.toolTip();
    }

    @Override
    public String toolTipText() {
        return propertyPanelLabels.toolTipText();
    }

    @Override
    public String argumentOption() {
        return propertyPanelLabels.argumentOption();
    }

    @Override
    public String argumentOptionHelp() {
        return contextualHelpMessages.argumentOption();
    }

    @Override
    public SafeHtml doNotDisplay() {
        return propertyPanelLabels.doNotDisplay();
    }

    @Override
    public SafeHtml isRequired() {
        return propertyPanelLabels.isRequired();
    }

    @Override
    public String emptyListSelectionText() {
        return appsWidgetsDisplayMessages.emptyListSelectionText();
    }

    @Override
    public String singleSelectionDefaultValue() {
        return propertyPanelLabels.singleSelectionDefaultValue();
    }

    @Override
    public String singleSelectDefaultItem() {
        return contextualHelpMessages.singleSelectDefaultItem();
    }

    @Override
    public String singleSelectExcludeArgument() {
        return contextualHelpMessages.singleSelectExcludeArgument();
    }

    @Override
    public String singleSelectionCreateLabel() {
        return propertyPanelLabels.singleSelectionCreateLabel();
    }

    @Override
    public String singleSelectionCreateList() {
        return contextualHelpMessages.singleSelectionCreateList();
    }

    @Override
    public String doubleSelectionLabel() {
        return propertyPanelLabels.doubleSelectionLabel();
    }

    @Override
    public String textSelectionEmptyText() {
        return propertyPanelLabels.textSelectionEmptyText();
    }

    @Override
    public String envVarWidgetEmptyEditText() {
        return propertyPanelLabels.envVarWidgetEmptyEditText();
    }

    @Override
    public String envVarDefaultLabel() {
        return propertyPanelLabels.envVarDefaultLabel();
    }

    @Override
    public String envVarDefaultValue() {
        return contextualHelpMessages.envVarDefaultValue();
    }

    @Override
    public String envVarNameLabel() {
        return propertyPanelLabels.envVarNameLabel();
    }

    @Override
    public String envVarDefaultName() {
        return contextualHelpMessages.envVarDefaultName();
    }

    @Override
    public String envVarLabel() {
        return propertyPanelLabels.envVarLabel();
    }

    @Override
    public String envVarEmptyText() {
        return propertyPanelLabels.envVarEmptyText();
    }

    @Override
    public String envVarNameEmptyText() {
        return propertyPanelLabels.envVarNameEmptyText();
    }

    @Override
    public String fileInputExcludeArgument() {
        return contextualHelpMessages.fileInputExcludeArgument();
    }

    @Override
    public String isImplicit() {
        return propertyPanelLabels.isImplicit();
    }

    @Override
    public String fileInputIsImplicit() {
        return contextualHelpMessages.fileInputIsImplicit();
    }

    @Override
    public String fileInputLabel() {
        return propertyPanelLabels.fileInputLabel();
    }

    @Override
    public String fileInputEmptyText() {
        return propertyPanelLabels.fileInputEmptyText();
    }

    @Override
    public String fileInputFileInfoType() {
        return propertyPanelLabels.fileInputFileInfoType();
    }

    @Override
    public String fileOutputEmptyText() {
        return propertyPanelLabels.fileOutputEmptyText();
    }

    @Override
    public String fileOutputDefaultLabel() {
        return propertyPanelLabels.fileOutputDefaultLabel();
    }

    @Override
    public String fileOutputDefaultValue() {
        return contextualHelpMessages.fileOutputDefaultValue();
    }

    @Override
    public String fileOutputSourceLabel() {
        return propertyPanelLabels.fileOutputSourceLabel();
    }

    @Override
    public String fileOutputOutputSource() {
        return contextualHelpMessages.fileOutputOutputSource();
    }

    @Override
    public String fileOutputExcludeArgument() {
        return contextualHelpMessages.fileOutputExcludeArgument();
    }

    @Override
    public String doNotPass() {
        return propertyPanelLabels.doNotPass();
    }

    @Override
    public String doNotPassHelp() {
        return contextualHelpMessages.doNotPass();
    }

    @Override
    public String fileOutputLabel() {
        return propertyPanelLabels.fileOutputLabel();
    }

    @Override
    public String fileOutputLabelEmptyText() {
        return propertyPanelLabels.fileOutputLabelEmptyText();
    }

    @Override
    public String fileOutputFileInfoTypeLabel() {
        return propertyPanelLabels.fileOutputFileInfoTypeLabel();
    }

    @Override
    public String folderInputExcludeArgument() {
        return contextualHelpMessages.folderInputExcludeArgument();
    }

    @Override
    public String folderInputLabel() {
        return propertyPanelLabels.folderInputLabel();
    }

    @Override
    public String folderInputEmptyText() {
        return propertyPanelLabels.folderInputEmptyText();
    }

    @Override
    public String folderInputFileInfoType() {
        return propertyPanelLabels.folderInputFileInfoType();
    }

    @Override
    public String folderOutputEmptyText() {
        return propertyPanelLabels.folderOutputEmptyText();
    }

    @Override
    public String folderOutputDefaultLabel() {
        return propertyPanelLabels.folderOutputDefaultLabel();
    }

    @Override
    public String folderOutputDefaultValue() {
        return contextualHelpMessages.folderOutputDefaultValue();
    }

    @Override
    public String folderOutputLabel() {
        return propertyPanelLabels.folderOutputLabel();
    }

    @Override
    public String folderOutputLabelEmptyText() {
        return propertyPanelLabels.folderOutputLabelEmptyText();
    }

    @Override
    public String folderOutputFileInfoTypeLabel() {
        return propertyPanelLabels.folderOutputFileInfoTypeLabel();
    }

    @Override
    public String folderOutputDefaultValueHelp() {
        return contextualHelpMessages.folderOutputDefaultValue();
    }

    @Override
    public String infoLabel() {
        return propertyPanelLabels.infoLabel();
    }

    @Override
    public String infoLabelHelp() {
        return contextualHelpMessages.infoLabelHelp();
    }

    @Override
    public String infoEmptyText() {
        return propertyPanelLabels.infoEmptyText();
    }

    @Override
    public String integerInputWidgetEmptyEditText() {
        return propertyPanelLabels.integerInputWidgetEmptyEditText();
    }

    @Override
    public String integerInputLabel() {
        return propertyPanelLabels.integerInputLabel();
    }

    @Override
    public String integerInputEmptyText() {
        return propertyPanelLabels.integerInputEmptyText();
    }

    @Override
    public String integerSelectionLabel() {
        return propertyPanelLabels.integerSelectionLabel();
    }

    @Override
    public String repeatOptionFlag() {
        return propertyPanelLabels.repeatOptionFlag();
    }

    @Override
    public String fileInputRepeatOptionFlag() {
        return contextualHelpMessages.fileInputRepeatOptionFlag();
    }

    @Override
    public String multiFileInputLabel() {
        return propertyPanelLabels.multiFileInputLabel();
    }

    @Override
    public String multiFileInputEmptyText() {
        return propertyPanelLabels.multiFileInputEmptyText();
    }

    @Override
    public String multiFileInputFileInfoType() {
        return propertyPanelLabels.multiFileInputFileInfoType();
    }

    @Override
    public String multiFileOutputDefaultLabel() {
        return propertyPanelLabels.multiFileOutputDefaultLabel();
    }

    @Override
    public String multiFileOutputDefaultValue() {
        return contextualHelpMessages.multiFileOutputDefaultValue();
    }

    @Override
    public String multiFileOutputLabel() {
        return propertyPanelLabels.multiFileOutputLabel();
    }

    @Override
    public String multiFileOutputLabelEmptyText() {
        return propertyPanelLabels.multiFileOutputLabelEmptyText();
    }

    @Override
    public String multiFileOutputFileInfoTypeLabel() {
        return propertyPanelLabels.multiFileOutputFileInfoTypeLabel();
    }

    @Override
    public String multiFileOutputEmptyText() {
        return propertyPanelLabels.multiFileOutputEmptyText();
    }

    @Override
    public String textInputWidgetEmptyEditText() {
        return propertyPanelLabels.textInputWidgetEmptyEditText();
    }

    @Override
    public String textInputDefaultLabel() {
        return propertyPanelLabels.textInputDefaultLabel();
    }

    @Override
    public String textInputDefaultText() {
        return contextualHelpMessages.textInputDefaultText();
    }

    @Override
    public String textInputExcludeArgument() {
        return contextualHelpMessages.textInputExcludeArgument();
    }

    @Override
    public String multiLineTextLabel() {
        return propertyPanelLabels.multiLineTextLabel();
    }

    @Override
    public String textInputEmptyText() {
        return propertyPanelLabels.textInputEmptyText();
    }
}
