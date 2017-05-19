package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.apps.integration.client.view.dialogs.ToolListingDialog;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.shared.GWT;

/**
 * @author aramsey
 */
public class ToolListingViewDefaultAppearance implements ToolListingDialog.ToolsListingViewAppearance {


    private IplantDisplayStrings iplantDisplayStrings;
    private ToolsDisplayStrings displayStrings;
    private IplantResources iplantResources;

    public ToolListingViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<ToolsDisplayStrings>create(ToolsDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    public ToolListingViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                            ToolsDisplayStrings displayStrings,
                                            IplantResources iplantResources) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
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
    public int dcListingDialogWidth() {
        return 600;
    }

    @Override
    public int dcListingDialogHeight() {
        return 500;
    }

    @Override
    public String dcListingDialogHeading() {
        return displayStrings.toolListingDialogHeading();
    }
}
