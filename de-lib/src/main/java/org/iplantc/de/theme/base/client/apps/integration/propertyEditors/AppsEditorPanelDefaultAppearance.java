package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppsEditorPanelAppearance;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.gray.client.panel.GrayHeaderAppearance;

public class AppsEditorPanelDefaultAppearance extends ContentPanelBaseAppearance implements
                                                                                 AppsEditorPanelAppearance {

    public interface AppsEditorPanelResources extends ContentPanelResources {
        @Source({ "com/sencha/gxt/theme/base/client/panel/ContentPanel.gss", "AppsEditorPanel.gss" })
        @Override
        AppsEditorPanelStyle style();
    }

    public interface AppsEditorPanelStyle extends ContentPanelStyle {

    }

    public AppsEditorPanelDefaultAppearance() {
        super(GWT.<AppsEditorPanelResources> create(AppsEditorPanelResources.class),
                GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }

    public AppsEditorPanelDefaultAppearance(AppsEditorPanelResources resources) {
        super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }

    @Override
    public HeaderDefaultAppearance getHeaderAppearance() {
        return new GrayHeaderAppearance();
    }
}
