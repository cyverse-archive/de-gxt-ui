package org.iplantc.de.diskResource.client.views.widgets;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.diskResource.client.events.RequestDiskResourceFavoriteEvent;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected;
import org.iplantc.de.diskResource.client.views.grid.cells.DiskResourceDotMenuCell;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * A menu containing the Favorite, Data Link, Share, Metadata, and Comment buttons as menu items
 * @author aramsey
 */
public class DiskResourceDotMenu extends Menu {

    private DiskResourceDotMenuCell.DiskResourceDotMenuAppearance appearance;
    private MenuItem favoriteBtn;
    private MenuItem dataLinkBtn;
    private MenuItem shareBtn;
    private MenuItem metadataBtn;
    private MenuItem commentBtn;

    public DiskResourceDotMenu(DiskResourceDotMenuCell.DiskResourceDotMenuAppearance appearance,
                               DiskResource diskResource,
                               HasHandlers hasHandlers) {
        super();
        this.appearance = appearance;

        addMenuItems(diskResource);
        checkButtonStatus(diskResource);
        addHandlers(hasHandlers, diskResource);
    }

    void addHandlers(HasHandlers hasHandlers, DiskResource diskResource) {
        if (hasHandlers != null) {
            favoriteBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new RequestDiskResourceFavoriteEvent(diskResource)));
            dataLinkBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ShareByDataLinkSelected(diskResource)));
            shareBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ManageSharingSelected(diskResource)));
            metadataBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ManageMetadataSelected(diskResource)));
            commentBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ManageCommentsSelected(diskResource)));
        }
    }

    void addMenuItems(DiskResource diskResource) {
        favoriteBtn = new MenuItem(appearance.favoriteText(diskResource), appearance.favoriteIcon(diskResource));
        dataLinkBtn = new MenuItem(appearance.dataLinkText(diskResource), appearance.dataLinkIcon());
        shareBtn = new MenuItem(appearance.shareText(), appearance.shareIcon());
        metadataBtn = new MenuItem(appearance.metadataText(), appearance.metadataIcon());
        commentBtn = new MenuItem(appearance.commentText(), appearance.commentIcon());

        add(favoriteBtn);
        add(dataLinkBtn);
        add(shareBtn);
        add(metadataBtn);
        add(commentBtn);
    }

    void checkButtonStatus(DiskResource diskResource) {
        shareBtn.setEnabled(isOwner(diskResource));
    }

    boolean isOwner(DiskResource diskResource) {
        return PermissionValue.own.equals(diskResource.getPermission());
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        favoriteBtn.setId(baseID + DiskResourceModule.Ids.ACTION_CELL_FAVORITE);
        dataLinkBtn.setId(baseID + DiskResourceModule.Ids.ACTION_CELL_DATA_LINK_ADD);
        shareBtn.setId(baseID + DiskResourceModule.Ids.ACTION_CELL_SHARE);
        metadataBtn.setId(baseID + DiskResourceModule.Ids.ACTION_CELL_METADATA);
        commentBtn.setId(baseID + DiskResourceModule.Ids.ACTION_CELL_COMMENTS);
    }
}
