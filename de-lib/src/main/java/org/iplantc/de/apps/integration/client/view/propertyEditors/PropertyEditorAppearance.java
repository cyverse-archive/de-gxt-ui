package org.iplantc.de.apps.integration.client.view.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;

/**
 * @author aramsey
 */
public interface PropertyEditorAppearance {

    String getPropertyDetailsPanelHeader(String value);

    AppsEditorPanelAppearance panelAppearance();

    String groupNameLabel();

    String groupNameEmptyText();

    String groupDelete();
}
