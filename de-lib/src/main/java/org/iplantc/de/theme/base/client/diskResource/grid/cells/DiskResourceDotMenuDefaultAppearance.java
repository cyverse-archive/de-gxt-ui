package org.iplantc.de.theme.base.client.diskResource.grid.cells;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.diskResource.client.views.grid.cells.DiskResourceDotMenuCell;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;
import org.iplantc.de.theme.base.client.diskResource.toolbar.ToolbarDisplayMessages;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class DiskResourceDotMenuDefaultAppearance implements DiskResourceDotMenuCell.DiskResourceDotMenuAppearance {

    interface MyCss extends CssResource {
        @ClassName("dot_menu")
        String dotMenu();
    }

    interface Resources extends ClientBundle {
        @Source("DotMenu.gss")
        MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img class='{0}' src='{1}' id='{2}'/>")
        SafeHtml cell(String imgClassName, SafeUri img, String debugId);
    }

    private Templates templates;
    private Resources resources;
    private IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;
    private DiskResourceMessages diskResourceMessages;
    private ToolbarDisplayMessages toolbarDisplayMessages;
    private DiskResourceDotMenuDisplayStrings displayStrings;

    public DiskResourceDotMenuDefaultAppearance() {
        this(GWT.<IplantResources>create(IplantResources.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<DiskResourceMessages>create(DiskResourceMessages.class),
             GWT.<ToolbarDisplayMessages>create(ToolbarDisplayMessages.class),
             GWT.<DiskResourceDotMenuDisplayStrings>create(DiskResourceDotMenuDisplayStrings.class),
             GWT.create(Templates.class),
             GWT.create(Resources.class));
    }

    public DiskResourceDotMenuDefaultAppearance(IplantResources iplantResources,
                                                IplantDisplayStrings iplantDisplayStrings,
                                                DiskResourceMessages diskResourceMessages,
                                                ToolbarDisplayMessages toolbarDisplayMessages,
                                                DiskResourceDotMenuDisplayStrings displayStrings,
                                                Templates templates,
                                                Resources resources) {

        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.diskResourceMessages = diskResourceMessages;
        this.toolbarDisplayMessages = toolbarDisplayMessages;
        this.displayStrings = displayStrings;
        this.templates = templates;
        this.resources = resources;
        resources.css().ensureInjected();
    }

    @Override

    public void render(Cell.Context context, DiskResource value, SafeHtmlBuilder sb, String debugId) {
        String className = resources.css().dotMenu();
        SafeUri uri = dotMenuIcon().getSafeUri();

        sb.append(templates.cell(className, uri, debugId));
    }

    @Override
    public ImageResource dotMenuIcon() {
        return iplantResources.dotMenu();
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
