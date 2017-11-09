package org.iplantc.de.theme.base.client.diskResource.grid.presenter;

import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;
import org.iplantc.de.theme.base.client.diskResource.grid.GridViewDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author jstroot
 */
public class GridViewPresenterDefaultAppearance implements GridView.Presenter.Appearance {
    private final IplantDisplayStrings iplantDisplayStrings;
    private final IplantErrorStrings iplantErrorStrings;
    private final GridViewDisplayStrings displayStrings;
    private final DiskResourceMessages diskResourceMessages;

    public GridViewPresenterDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<GridViewDisplayStrings> create(GridViewDisplayStrings.class),
             GWT.<DiskResourceMessages> create(DiskResourceMessages.class));
    }

    GridViewPresenterDefaultAppearance(final IplantDisplayStrings iplantDisplayStrings,
                                       final IplantErrorStrings iplantErrorStrings,
                                       final GridViewDisplayStrings displayStrings,
                                       final DiskResourceMessages diskResourceMessages) {
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantErrorStrings = iplantErrorStrings;
        this.displayStrings = displayStrings;
        this.diskResourceMessages = diskResourceMessages;
    }

    @Override
    public String comments() {
        return iplantDisplayStrings.comments();
    }

    @Override
    public String dataLinkTitle() {
        return displayStrings.dataLinkTitle();
    }

    @Override
    public String createDataLinksError() {
        return diskResourceMessages.createDataLinksError();
    }

    @Override
    public String favoritesError(String message) {
        return displayStrings.favoritesError(message);
    }

    @Override
    public String markFavoriteError() {
        return displayStrings.markFavoriteError();
    }

    @Override
    public String metadata() {
        return displayStrings.metadata();
    }

    @Override
    public String metadataDialogHeight() {
        return "600";
    }

    @Override
    public String metadataDialogWidth() {
        return "800";
    }

    @Override
    public String metadataFormInvalid() {
        return displayStrings.metadataFormInvalid();
    }

    @Override
    public String metadataHelp() {
        return displayStrings.metadataHelp();
    }

    @Override
    public String removeFavoriteError() {
        return displayStrings.removeFavoriteError();
    }

    @Override
    public String retrieveStatFailed() {
        return displayStrings.retrieveStatError();
    }

    @Override
    public String searchDataResultsHeader(String searchText, int total, double executionTime_ms) {
        return displayStrings.searchDataResultsHeader(searchText, total, executionTime_ms);
    }

    @Override
    public String searchFailure() {
        return displayStrings.searchFailure();
    }

    @Override
    public String shareLinkDialogHeight() {
        return "130";
    }

    @Override
    public int shareLinkDialogTextBoxWidth() {
        return 410;
    }

    @Override
    public String shareLinkDialogWidth() {
        return "535";
    }

    @Override
    public String shareFailure() {
        return displayStrings.shareFailure();
    }

    @Override
    public String shareByLinkFailure() {
        return displayStrings.shareByLinkFailure();
    }

    @Override
    public String metadataOverwriteWarning(String drName) {
        return displayStrings.metadataOverwriteWarning(drName);
    }

    @Override
    public String metadataManageFailure() {
        return displayStrings.metadataManageFailure();
    }

    @Override
    public String commentsManageFailure() {
        return displayStrings.commentsManageFailure();
    }

    @Override
    public String copyMetadata(String path) {
        return displayStrings.copyMetadata(path);
    }

    @Override
    public String copyMetadataSuccess() {
        return displayStrings.copyMetadataSuccess();
    }

    @Override
    public String copyMetadataFailure() {
        return displayStrings.copyMetadataFailure();
    }

    @Override
    public String md5Checksum() {
        return displayStrings.md5Checksum();
    }

    @Override
    public String checksum() {
        return displayStrings.checksum();
    }

    @Override
    public String metadataSaveError() {
        return displayStrings.metadataSaveError();
    }

    public String error() {
        return iplantDisplayStrings.error();
    }

    public String copyMetadataPrompt() {
        return displayStrings.copyMetadataPrompt();
    }

    @Override
    public SafeHtml fileExistsError(String fileName) {
        return iplantErrorStrings.fileExists(fileName);
    }

    @Override
    public String fileSaveError(String fileName) {
        return displayStrings.fileSaveError(fileName);
    }

    @Override
    public String listingFailure() {
        return displayStrings.listingFailure();
    }

    @Override
    public String metadataSaved() {
        return displayStrings.metadataSaved();
    }

    @Override
    public String copyMetadataNoResources() {
        return displayStrings.copyMetadataNoResources();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String saving() {
        return displayStrings.saving();
    }

    @Override
    public String copyMetadataDlgWidth() {
        return "400px";
    }

    @Override
    public String copyMetadataDlgHeight() {
        return "350px";
    }

    @Override
    public int md5MaxLength() {
        return 125;
    }

    @Override
    public String bulkMetadataSuccess() {
        return diskResourceMessages.bulkMetadataSuccess();
    }

    @Override
    public String bulkMetadataError() {
        return diskResourceMessages.bulkMetadataError();
    }

    @Override
    public String copyPath() {
        return displayStrings.copyPath();
    }

}
