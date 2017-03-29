package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;
import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class PropertyEditorDefaultAppearance implements PropertyEditorAppearance {

    private AppsWidgetsPropertyPanelLabels propertyPanelLabels;
    private AppsEditorPanelAppearance panelAppearance;

    public PropertyEditorDefaultAppearance() {
        this ((AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class),
              (AppsEditorPanelAppearance)GWT.create(AppsEditorPanelAppearance.class));
    }

    public PropertyEditorDefaultAppearance(AppsWidgetsPropertyPanelLabels propertyPanelLabels,
                                           AppsEditorPanelAppearance panelAppearance) {
        this.propertyPanelLabels = propertyPanelLabels;
        this.panelAppearance = panelAppearance;
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
}
