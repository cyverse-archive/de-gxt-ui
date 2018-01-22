package org.iplantc.de.theme.base.client.diskResource.grid.cells;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.diskResource.client.views.grid.cells.DiskResourceDotMenuCell;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.cells.AppDotMenuDefaultAppearance;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;
import org.iplantc.de.theme.base.client.diskResource.toolbar.ToolbarDisplayMessages;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class DiskResourceDotMenuDefaultAppearance implements DiskResourceDotMenuCell.DiskResourceDotMenuAppearance {

    private IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;
    private DiskResourceMessages diskResourceMessages;
    private AppDotMenuDefaultAppearance appDotMenuDefaultAppearance;
    private ToolbarDisplayMessages toolbarDisplayMessages;
    private DiskResourceDotMenuDisplayStrings displayStrings;

    public DiskResourceDotMenuDefaultAppearance() {
        this(GWT.<IplantResources>create(IplantResources.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<DiskResourceMessages>create(DiskResourceMessages.class),
             GWT.<AppDotMenuDefaultAppearance>create(AppDotMenuDefaultAppearance.class),
             GWT.<ToolbarDisplayMessages>create(ToolbarDisplayMessages.class),
             GWT.<DiskResourceDotMenuDisplayStrings>create(DiskResourceDotMenuDisplayStrings.class));
    }

    public DiskResourceDotMenuDefaultAppearance(IplantResources iplantResources,
                                                IplantDisplayStrings iplantDisplayStrings,
                                                DiskResourceMessages diskResourceMessages,
                                                AppDotMenuDefaultAppearance appDotMenuDefaultAppearance,
                                                ToolbarDisplayMessages toolbarDisplayMessages,
                                                DiskResourceDotMenuDisplayStrings displayStrings) {

        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.diskResourceMessages = diskResourceMessages;
        this.appDotMenuDefaultAppearance = appDotMenuDefaultAppearance;
        this.toolbarDisplayMessages = toolbarDisplayMessages;
        this.displayStrings = displayStrings;
    }

    @Override

    public void render(Cell.Context context, DiskResource value, SafeHtmlBuilder sb, String debugId) {
        appDotMenuDefaultAppearance.render(context, sb, debugId);
    }

    @Override
    public ImageResource dotMenuIcon() {
        return appDotMenuDefaultAppearance.dotMenuIcon();
    }

    @Override
    public String favoriteText(DiskResource diskResource) {
        if (diskResource.isFavorite()) {
            return iplantDisplayStrings.remAppFromFav();
        } else {
            return iplantDisplayStrings.addAppToFav();
        }
    }

    @Override
    public ImageResource favoriteIcon(DiskResource diskResource) {
        if (diskResource.isFavorite()) {
            return iplantResources.favIconDelete();
        } else {
            return iplantResources.favIconAdd();
        }
    }

    @Override
    public String commentText() {
        return iplantDisplayStrings.comments();
    }

    @Override
    public ImageResource commentIcon() {
        return iplantResources.userComment();
    }

    @Override
    public String dataLinkText(DiskResource diskResource) {
        if (diskResource instanceof File) {
            return displayStrings.dataLinkFileText();
        } else {
            return toolbarDisplayMessages.shareFolderLocationMenuItem();
        }
    }

    @Override
    public ImageResource dataLinkIcon() {
        return iplantResources.linkIcon();
    }

    @Override
    public String shareText() {
        return diskResourceMessages.shareCollab();
    }

    @Override
    public ImageResource shareIcon() {
        return iplantResources.share();
    }

    @Override
    public String metadataText() {
        return displayStrings.metadataText();
    }

    @Override
    public ImageResource metadataIcon() {
        return iplantResources.metadataIcon();
    }

    @Override
    public String copyPathText() {
        return displayStrings.copyPathText();
    }

    @Override
    public ImageResource copyPathIcon() {
        return iplantResources.copy();
    }
}
