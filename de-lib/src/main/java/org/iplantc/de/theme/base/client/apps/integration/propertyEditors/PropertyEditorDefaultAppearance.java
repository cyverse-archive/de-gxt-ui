package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;
import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.de.resources.client.uiapps.widgets.ArgumentValidatorMessages;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class PropertyEditorDefaultAppearance implements PropertyEditorAppearance {

    private AppsWidgetsPropertyPanelLabels propertyPanelLabels;
    private AppsEditorPanelAppearance panelAppearance;
    private ArgumentValidatorMessages argumentValidatorMessages;

    public PropertyEditorDefaultAppearance() {
        this ((AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class),
              (AppsEditorPanelAppearance)GWT.create(AppsEditorPanelAppearance.class),
              (ArgumentValidatorMessages)GWT.create(ArgumentValidatorMessages.class));
    }

    public PropertyEditorDefaultAppearance(AppsWidgetsPropertyPanelLabels propertyPanelLabels,
                                           AppsEditorPanelAppearance panelAppearance,
                                           ArgumentValidatorMessages argumentValidatorMessages) {
        this.propertyPanelLabels = propertyPanelLabels;
        this.panelAppearance = panelAppearance;
        this.argumentValidatorMessages = argumentValidatorMessages;
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
}
