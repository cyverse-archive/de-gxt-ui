package org.iplantc.de.apps.integration.client.view.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author aramsey
 */
public interface PropertyEditorAppearance {

    String getPropertyDetailsPanelHeader(String value);

    AppsEditorPanelAppearance panelAppearance();

    String groupNameLabel();

    String groupNameEmptyText();

    String groupDelete();

    String regex(String regex);

    String characterLimit(int limit);

    String intAbove(int above);

    String intBelow(int below);

    String intRange(int above, int below);

    String dblAbove(double above);

    String dblBelow(double below);

    String dblRange(double above, double below);

    String ruleType();

    String valueRegex();

    String valueCharLimit();

    String valueAbove();

    String valueBelow();

    String valueBetween();

    String valueBetweenAnd();

    String validatorDialogHeading();

    String regexLabel();

    String characterLimitLabel();

    String intAboveLabel();

    String intBelowLabel();

    String intRangeLabel();

    String dblAboveLabel();

    String dblBelowLabel();

    String dblRangeLabel();

    String validatorDialogWidth();

    String validatorDialogHeight();

    String textInputValidationRules();

    String validatorRulesLabel();

    SafeHtml createContextualHelpLabel(String label, String toolTip);

    ImageResource helpIcon();

    String argumentValidatorNameColumn();

    String add();

    ImageResource addIcon();

    String edit();

    ImageResource editIcon();

    String delete();

    ImageResource deleteIcon();

    String singleSelectEditToolTip();

    String selectionCreateWidgetEmptyText();

    String singleSelectDisplayColumnHeader();

    String singleSelectNameColumnHeader();

    String singleSelectValueColumnHeader();

    String singleSelectIsDefaultColumnHeader();

    String singleSelectToolTipColumnHeader();

    String checkCascadeLabel();

    String forceSingleSelectLabel();

    String forceSingleSelectToolTip();

    String addGroupToolTip();

    String addArgumentToolTip();

    String cascadeOptionsComboToolTip();

    ImageResource folderAddIcon();

    ImageResource cancelIcon();

    String doubleInputLabel();

    String doubleInputEmptyText();

    String argumentOptionEmptyText();

    String toolTipEmptyText();

    String doubleInputWidgetEmptyEditText();

    String integerInputDefaultLabel();

    String integerInputDefaultValue();

    String integerInputExcludeArgument();

    String excludeWhenEmpty();

    SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip);

    String toolTip();

    String toolTipText();

    String argumentOption();

    String argumentOptionHelp();

    SafeHtml doNotDisplay();

    SafeHtml isRequired();

    String emptyListSelectionText();

    String singleSelectionDefaultValue();

    String singleSelectDefaultItem();

    String singleSelectExcludeArgument();

    String singleSelectionCreateLabel();

    String singleSelectionCreateList();

    String doubleSelectionLabel();

    String textSelectionEmptyText();

    String envVarWidgetEmptyEditText();

    String envVarDefaultLabel();

    String envVarDefaultValue();

    String envVarNameLabel();

    String envVarDefaultName();

    String envVarLabel();

    String envVarEmptyText();

    String envVarNameEmptyText();

    String fileInputExcludeArgument();

    String isImplicit();

    String fileInputIsImplicit();

    String fileInputLabel();

    String fileInputEmptyText();

    String fileInputFileInfoType();

    String fileOutputEmptyText();

    String fileOutputDefaultLabel();

    String fileOutputDefaultValue();

    String fileOutputSourceLabel();

    String fileOutputOutputSource();

    String fileOutputExcludeArgument();

    String doNotPass();

    String doNotPassHelp();

    String fileOutputLabel();

    String fileOutputLabelEmptyText();

    String fileOutputFileInfoTypeLabel();

    String folderInputExcludeArgument();

    String folderInputLabel();

    String folderInputEmptyText();

    String folderInputFileInfoType();

    String folderOutputEmptyText();

    String folderOutputDefaultLabel();

    String folderOutputDefaultValue();

    String folderOutputLabel();

    String folderOutputLabelEmptyText();

    String folderOutputFileInfoTypeLabel();

    String folderOutputDefaultValueHelp();

    String infoLabel();

    String infoLabelHelp();

    String infoEmptyText();
}
