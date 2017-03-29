package org.iplantc.de.theme.base.client.apps.integration;

import org.iplantc.de.apps.integration.client.view.widgets.AppTemplateContentPanel;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

/**
 * @author aramsey
 */
public class ArgumentGroupContentPanelDefaultAppearance extends ContentPanelBaseAppearance implements
                                                                                           AppTemplateContentPanel.ArgumentGroupContentPanelAppearance {

    public interface ArgumentGroupContentPanelAppearanceResources extends ContentPanelResources {
        @Source({ "com/sencha/gxt/theme/base/client/panel/ContentPanel.gss",
                  "ArgumentGroupContentPanel.gss" })
        @Override
        ArgumentGroupContentPanelAppearanceStyle style();
    }

    public interface SelectableHeaderResources extends HeaderDefaultAppearance.HeaderResources {

        @Override
        @Source({ "com/sencha/gxt/theme/base/client/widget/Header.gss", "SelectableHeader.gss" })
        SelectableHeaderStyle style();
    }

    public interface SelectableHeaderStyle extends HeaderDefaultAppearance.HeaderStyle {
        String headerSelect();
    }

    public interface ArgumentGroupContentPanelAppearanceStyle extends ContentPanelStyle {

    }

    public final class SelectableHeaderAppearance extends HeaderDefaultAppearance {

        public SelectableHeaderAppearance() {
            super(GWT.create(SelectableHeaderResources.class),
                  GWT.<Template> create(Template.class));
        }

    }

    public ArgumentGroupContentPanelDefaultAppearance() {
        super(GWT.<ArgumentGroupContentPanelAppearanceResources> create(ArgumentGroupContentPanelAppearanceResources.class),
              GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }

    public ArgumentGroupContentPanelDefaultAppearance(ArgumentGroupContentPanelAppearanceResources resources) {
        super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    }


    @Override
    public HeaderDefaultAppearance getHeaderAppearance() {
        return new SelectableHeaderAppearance();
    }
}
