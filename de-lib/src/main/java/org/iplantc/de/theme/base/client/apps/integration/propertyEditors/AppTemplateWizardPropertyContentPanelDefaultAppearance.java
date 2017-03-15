package org.iplantc.de.theme.base.client.apps.integration.propertyEditors;

import org.iplantc.de.apps.integration.client.view.AppTemplateWizardPropertyContentPanelAppearance;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.gray.client.panel.GrayHeaderAppearance;

public class AppTemplateWizardPropertyContentPanelDefaultAppearance extends ContentPanelBaseAppearance implements
                                                                                                       AppTemplateWizardPropertyContentPanelAppearance {

    public interface AppTemplateWizardPropertyContentPanelResources extends ContentPanelResources {
        @Source({ "com/sencha/gxt/theme/base/client/panel/ContentPanel.gss",
                  "AppTemplateWizardPropertyContentPanel.gss" })
        @Override
        AppTemplateWizardPropertyContentPanelStyle style();
    }

    public interface AppTemplateWizardPropertyContentPanelStyle extends ContentPanelStyle {

    }

    public AppTemplateWizardPropertyContentPanelDefaultAppearance() {
        super(GWT.<AppTemplateWizardPropertyContentPanelResources> create(AppTemplateWizardPropertyContentPanelResources.class),
                GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }

    public AppTemplateWizardPropertyContentPanelDefaultAppearance(AppTemplateWizardPropertyContentPanelResources resources) {
        super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }

    @Override
    public HeaderDefaultAppearance getHeaderAppearance() {
        return new GrayHeaderAppearance();
    }
}
