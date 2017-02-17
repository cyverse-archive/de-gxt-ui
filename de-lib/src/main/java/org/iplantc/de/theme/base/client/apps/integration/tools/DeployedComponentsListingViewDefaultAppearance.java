package org.iplantc.de.theme.base.client.apps.integration.tools;

import org.iplantc.de.apps.integration.client.view.tools.DeployedComponentsListingView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class DeployedComponentsListingViewDefaultAppearance implements DeployedComponentsListingView.DeployedComponentsListingViewAppearance {

    interface DCDetailsRenderer extends XTemplates {
        @XTemplate(source = "DCDetails.html")
        SafeHtml render();
    }

    private IplantDisplayStrings iplantDisplayStrings;
    private DCDetailsRenderer dcDetailsRenderer;
    private DeployedComponentsDisplayStrings displayStrings;
    private IplantResources iplantResources;

    public DeployedComponentsListingViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<DCDetailsRenderer>create(DCDetailsRenderer.class),
             GWT.<DeployedComponentsDisplayStrings>create(DeployedComponentsDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    public DeployedComponentsListingViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
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
}
