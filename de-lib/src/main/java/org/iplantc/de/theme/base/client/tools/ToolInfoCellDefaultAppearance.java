package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.tools.client.views.cells.ToolInfoCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author sriram
 */
public class ToolInfoCellDefaultAppearance implements ToolInfoCell.ToolInfoCellAppearance {

    interface ToolDetailsRenderer extends XTemplates {
        @XTemplate(source = "ToolInfo.html")
        SafeHtml render();
    }

    interface MyCss extends CssResource {
        String toolInfo();
    }

    interface Resources extends ClientBundle {
        @Source("ToolInfoCell.gss")
        MyCss css();
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<img class='{0}' qtip='{2}' src='{1}'/>")
        SafeHtml cell(String imgClassName, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("<img id='{3}' class='{0}' qtip='{2}' src='{1}'/>")
        SafeHtml debugCell(String imgClassName, SafeUri img, String toolTip, String debugId);
    }

    private Resources resources;
    private Templates templates;
    private ToolDetailsRenderer detailsRenderer;
    private ToolsDisplayStrings displayStrings;
    private IplantResources iplantResources;

    public ToolInfoCellDefaultAppearance() {
        this((Resources)GWT.create(Resources.class),
             (ToolDetailsRenderer)GWT.create(ToolDetailsRenderer.class),
             (Templates)GWT.create(Templates.class),
             (ToolsDisplayStrings)GWT.create(ToolsDisplayStrings.class),
             (IplantResources)GWT.create(IplantResources.class));
    }

    public ToolInfoCellDefaultAppearance(Resources resources,
                                         ToolDetailsRenderer detailsRenderer,
                                         Templates templates,
                                         ToolsDisplayStrings displayStrings,
                                         IplantResources iplantResources) {

        this.resources = resources;
        this.templates = templates;
        this.detailsRenderer = detailsRenderer;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, String debugId) {
        final SafeUri safeUri = iplantResources.info().getSafeUri();
        if (DebugInfo.isDebugIdEnabled()) {
            sb.append(templates.debugCell(resources.css().toolInfo(),
                                          safeUri,
                                          displayStrings.toolInformation(),
                                          debugId));
        } else {
            sb.append(templates.cell(resources.css().toolInfo(),
                                     safeUri,
                                     displayStrings.toolInformation()));
        }
    }

    @Override
    public SafeHtml detailsRenderer() {
        return detailsRenderer.render();
    }


    @Override
    public String detailsDialogWidth() {
        return "500px";
    }

    @Override
    public String detailsDialogHeight() {
        return "300px";
    }

    @Override
    public String attributionLabel() {
        return displayStrings.attributionLabel();
    }

    @Override
    public String descriptionLabel() {
        return displayStrings.descriptionLabel();
    }

    @Override
    public String tabWidth() {
        return "500px";
    }

    @Override
    public String tabHeight() {
        return "300px";
    }

    @Override
    public String toolInformation() {
        return displayStrings.toolInformation();
    }

    @Override
    public String appsUsingTool() {
        return displayStrings.appUsingTool();
    }
}
