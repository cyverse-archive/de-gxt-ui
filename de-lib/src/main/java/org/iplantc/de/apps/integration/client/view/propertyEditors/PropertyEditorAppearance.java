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
}