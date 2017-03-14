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

    interface DCTemplate extends XTemplates {
        @XTemplate(source = "DCSearchResult.html")
        SafeHtml render(Tool c);
    }

    private DCTemplate dcTemplate;
    private IplantDisplayStrings iplantDisplayStrings;

    public ToolSearchFieldDefaultAppearance() {
        this((DCTemplate)GWT.create(DCTemplate.class),
             (IplantDisplayStrings)GWT.create(IplantDisplayStrings.class));
    }

    public ToolSearchFieldDefaultAppearance(DCTemplate dcTemplate,
                                            IplantDisplayStrings iplantDisplayStrings) {

        this.dcTemplate = dcTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public SafeHtml render(Tool tool) {
        return dcTemplate.render(tool);
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
