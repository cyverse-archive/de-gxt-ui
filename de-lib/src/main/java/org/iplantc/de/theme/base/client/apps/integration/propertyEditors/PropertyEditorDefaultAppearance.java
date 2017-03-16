package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class PropertyEditorDefaultAppearance implements PropertyEditorAppearance {

    private AppsWidgetsPropertyPanelLabels propertyPanelLabels;

    public PropertyEditorDefaultAppearance() {
        this ((AppsWidgetsPropertyPanelLabels)GWT.create(AppsWidgetsPropertyPanelLabels.class));
    }

    public PropertyEditorDefaultAppearance(AppsWidgetsPropertyPanelLabels propertyPanelLabels) {
        this.propertyPanelLabels = propertyPanelLabels;
    }

    @Override
    public String getPropertyDetailsPanelHeader(String value) {
        return propertyPanelLabels.detailsPanelHeader(value);
    }
}
