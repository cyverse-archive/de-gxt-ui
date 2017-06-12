package org.iplantc.de.theme.base.client.apps.integration.tools;

import org.iplantc.de.apps.integration.client.view.tools.ToolSearchField;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class ToolSearchFieldDefaultAppearance implements ToolSearchField.ToolSearchFieldAppearance {

    interface ToolTemplate extends XTemplates {
        @XTemplate(source = "ToolSearchResult.html")
        SafeHtml render(Tool c);
    }

    private ToolTemplate toolTemplate;
    private IplantDisplayStrings iplantDisplayStrings;

    public ToolSearchFieldDefaultAppearance() {
        this((ToolTemplate)GWT.create(ToolTemplate.class),
             (IplantDisplayStrings)GWT.create(IplantDisplayStrings.class));
    }

    public ToolSearchFieldDefaultAppearance(ToolTemplate toolTemplate,
                                            IplantDisplayStrings iplantDisplayStrings) {

        this.toolTemplate = toolTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public SafeHtml render(Tool tool) {
        return toolTemplate.render(tool);
    }

    @Override
    public String searchEmptyText() {
        return iplantDisplayStrings.searchEmptyText();
    }

    @Override
    public String toolLabel(Tool c) {
        return c.getName() +  " " + c.getVersion();
    }
}
