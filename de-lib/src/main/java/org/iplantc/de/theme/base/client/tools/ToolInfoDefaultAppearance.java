package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.tools.client.views.manage.ToolInfoView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author sriram
 */
public class ToolInfoDefaultAppearance implements ToolInfoView.ToolInfoAppearance {

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

    private Resources resources;
    private ToolDetailsRenderer detailsRenderer;
    private ToolsDisplayStrings displayStrings;

    public ToolInfoDefaultAppearance() {
        this((Resources)GWT.create(Resources.class),
             (ToolDetailsRenderer)GWT.create(ToolDetailsRenderer.class),
             (ToolsDisplayStrings)GWT.create(ToolsDisplayStrings.class));
    }

    public ToolInfoDefaultAppearance(Resources resources,
                                     ToolDetailsRenderer detailsRenderer,
                                     ToolsDisplayStrings displayStrings) {

        this.resources = resources;
        this.detailsRenderer = detailsRenderer;
        this.displayStrings = displayStrings;
        this.resources.css().ensureInjected();
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

    @Override
    public String restrictions() {
        return displayStrings.restrictions();
    }

    @Override
    public String memLimit() {
        return displayStrings.memLimit();
    }

    @Override
    public String pidsLimit() {
        return displayStrings.pidsLimit();
    }

    @Override
    public String timeLimit() {
        return displayStrings.timeLimit();
    }

    @Override
    public String networkingMode() {
        return displayStrings.networkingMode();
    }

    @Override
    public String bridge() {
        return displayStrings.bridge();
    }

    @Override
    public String enabled() {
        return displayStrings.enabled();
    }

    @Override
    public String disabled() {
        return displayStrings.disabled();
    }

    @Override
    public String notApplicable() {
        return displayStrings.notApplicable();
    }
}
