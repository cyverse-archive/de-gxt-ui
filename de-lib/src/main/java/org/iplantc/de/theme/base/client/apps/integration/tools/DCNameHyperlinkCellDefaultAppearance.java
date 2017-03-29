package org.iplantc.de.theme.base.client.apps.integration.tools;

import org.iplantc.de.apps.integration.client.view.deployedComponents.cells.DCNameHyperlinkCell;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author aramsey
 */
public class DCNameHyperlinkCellDefaultAppearance implements DCNameHyperlinkCell.DCNameHyperlinkCellAppearance {

    interface MyCss extends CssResource {
        String DCName();
    }

    interface Resources extends ClientBundle {
        @Source("DCNameHyperlinkCell.gss")
        MyCss css();
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml cell(String textClassName, SafeHtml name, String textToolTip, String elementName);
    }

    private Resources resources;
    private Templates templates;

    public DCNameHyperlinkCellDefaultAppearance() {
        this((Resources)GWT.create(Resources.class),
             (Templates)GWT.create(Templates.class));
    }

    public DCNameHyperlinkCellDefaultAppearance(Resources resources, Templates templates) {

        this.resources = resources;
        this.templates = templates;
        this.resources.css().ensureInjected();
    }

    @Override
    public SafeHtml render(Tool tool) {
        SafeHtml safeHtmlToolName = SafeHtmlUtils.fromTrustedString(tool.getName());
        return templates.cell(resources.css().DCName(), safeHtmlToolName, "Click to view info",
                              ELEMENT_NAME);
    }
}
