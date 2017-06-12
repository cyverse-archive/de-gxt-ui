package org.iplantc.de.theme.base.client.apps.integration.tools;

import org.iplantc.de.apps.integration.client.view.dialogs.ToolListingDialog;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class ToolListingViewDefaultAppearance implements ToolListingDialog.ToolsListingViewAppearance {

    interface DCDetailsRenderer extends XTemplates {
        @XTemplate(source = "DCDetails.html")
        SafeHtml render();
    }

    private IplantDisplayStrings iplantDisplayStrings;
    private DCDetailsRenderer dcDetailsRenderer;
    private DeployedComponentsDisplayStrings displayStrings;
    private IplantResources iplantResources;

    public ToolListingViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<DCDetailsRenderer>create(DCDetailsRenderer.class),
             GWT.<DeployedComponentsDisplayStrings>create(DeployedComponentsDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    public ToolListingViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                            DCDetailsRenderer dcDetailsRenderer,
                                            DeployedComponentsDisplayStrings displayStrings,
                                            IplantResources iplantResources) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.dcDetailsRenderer = dcDetailsRenderer;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
    }

    @Override
    public String nameColumnHeader() {
        return iplantDisplayStrings.name();
    }

    @Override
    public String versionColumnHeader() {
        return displayStrings.toolVersion();
    }

    @Override
    public String pathColumnHeader() {
        return iplantDisplayStrings.path();
    }

    @Override
    public String attributionLabel() {
        return iplantDisplayStrings.attribution();
    }

    @Override
    public String descriptionLabel() {
        return iplantDisplayStrings.description();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String searchEmptyText() {
        return iplantDisplayStrings.searchEmptyText();
    }

    @Override
    public SafeHtml detailsRenderer() {
        return dcDetailsRenderer.render();
    }

    @Override
    public String newToolReq() {
        return iplantDisplayStrings.newToolReq();
    }

    @Override
    public ImageResource add() {
        return iplantResources.add();
    }

    @Override
    public String infoDialogWidth() {
        return "500px";
    }

    @Override
    public String infoDialogHeight() {
        return "300px";
    }

    @Override
    public int nameColumnWidth() {
        return 100;
    }

    @Override
    public int versionColumnWidth() {
        return 100;
    }

    @Override
    public int pathColumnWidth() {
        return 100;
    }

    @Override
    public int dcListingDialogWidth() {
        return 600;
    }

    @Override
    public int dcListingDialogHeight() {
        return 500;
    }

    @Override
    public String dcListingDialogHeading() {
        return displayStrings.dcListingDialogHeading();
    }
}
