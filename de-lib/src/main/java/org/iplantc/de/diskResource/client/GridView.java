package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.diskResource.client.events.DiskResourceNameSelectedEvent.DiskResourceNameSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.DiskResourceNameSelectedEvent.HasDiskResourceNameSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.DiskResourcePathSelectedEvent.HasDiskResourcePathSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent.HasDiskResourceSelectionChangedEventHandlers;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent.FolderSelectionEventHandler;
import org.iplantc.de.diskResource.client.events.RequestDiskResourceFavoriteEvent.RequestDiskResourceFavoriteEventHandler;
import org.iplantc.de.diskResource.client.events.TemplateDownloadEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.de.diskResource.client.events.selection.BulkMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.CopyMetadataSelected.CopyMetadataSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.CopyPathSelected;
import org.iplantc.de.diskResource.client.events.selection.DNDDiskResourcesCompleted;
import org.iplantc.de.diskResource.client.events.selection.DownloadTemplateSelectedEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected.EditInfoTypeSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected.ManageCommentsSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected.ManageMetadataSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected.ManageSharingSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.Md5ValueClicked.Md5ValueClickedHandler;
import org.iplantc.de.diskResource.client.events.selection.SetInfoTypeSelected.SetInfoTypeSelectedHandler;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataSelected.SaveMetadataSelectedEventHandler;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected.ShareByDataLinkSelectedEventHandler;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.views.grid.DiskResourceColumnModel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.data.shared.event.StoreUpdateEvent.HasStoreUpdateHandlers;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.container.HasLayout;
import com.sencha.gxt.widget.core.client.grid.LiveGridCheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;
import java.util.Map;

/**
 * Created by jstroot on 1/26/15.
 * @author jstroot
 */
public interface GridView extends IsWidget,
                                  HasLayout,
                                  HasDiskResourcePathSelectedEventHandlers,
                                  FolderSelectionEventHandler,
                                  HasDiskResourceNameSelectedEventHandlers,
                                  HasDiskResourceSelectionChangedEventHandlers,
                                  SubmitDiskResourceQueryEventHandler {
    interface Appearance {

        String actionsColumnLabel();

        int dotMenuColumnWidth();

        String createdDateColumnLabel();

        int createdDateColumnWidth();

        String dataDragDropStatusText(int totalSelectionCount);

        String lastModifiedColumnLabel();

        int lastModifiedColumnWidth();

        int liveGridViewRowHeight();

        int liveToolItemWidth();

        String nameColumnLabel();

        int nameColumnWidth();

        String pathColumnLabel();

        int pathColumnWidth();

        String pathFieldLabel();

        int pathFieldLabelWidth();

        String pathFieldEmptyText();

        String permissionErrorMessage();

        int selectionStatusItemWidth();

        void setPagingToolBarStyle(ToolBar pagingToolBar);

        String sizeColumnLabel();

        int sizeColumnWidth();

        String gridViewEmptyText();

        String infoTypeDialogWidth();

        String infoTypeDialogHeight();

        String infoTypeDialogHeader();

        String infoTypeEmptyText();

        String selectionCountStatus(int selectionCount);
    }

    interface FolderContentsRpcProxy extends DataProxy<FolderContentsLoadConfig, PagingLoadResult<DiskResource>> {
        void setHasSafeHtml(HasSafeHtml centerHeader);
    }

    interface Presenter extends DiskResourceNameSelectedEventHandler,
                                ManageSharingSelectedEventHandler,
                                ManageMetadataSelectedEventHandler,
                                CopyMetadataSelectedEventHandler,
                                SaveMetadataSelectedEventHandler,
                                DownloadTemplateSelectedEvent.DownloadTemplateSelectedEventHandler,
                                ShareByDataLinkSelectedEventHandler,
                                RequestDiskResourceFavoriteEventHandler,
                                ManageCommentsSelectedEventHandler,
                                FolderSelectionEventHandler,
                                SubmitDiskResourceQueryEventHandler,
                                HasStoreUpdateHandlers<DiskResource>,
                                EditInfoTypeSelectedEventHandler, SetInfoTypeSelectedHandler,
                                Md5ValueClickedHandler,
                                TemplateDownloadEvent.TemplateDownloadEventHandler,
                                FetchDetailsCompleted.HasFetchDetailsCompletedHandlers,
                                DNDDiskResourcesCompleted.HasDNDDiskResourcesCompletedHandlers,
                                BulkMetadataSelected.BulkMetadataSelectedHandler,
                                CopyPathSelected.CopyPathSelectedEventHandler {

        interface Appearance {

            String comments();

            String dataLinkTitle();

            String createDataLinksError();

            String favoritesError(String message);

            String markFavoriteError();

            String metadata();

            String metadataDialogHeight();

            String metadataDialogWidth();

            String metadataFormInvalid();

            String metadataHelp();

            String removeFavoriteError();

            String retrieveStatFailed();

            String searchDataResultsHeader(String searchText, int total, double executionTime_ms);

            String searchFailure();

            String shareLinkDialogHeight();

            String shareLinkDialogMultiLineHeight();

            int shareLinkDialogTextBoxWidth();

            String shareLinkDialogWidth();

            String shareFailure();

            String shareByLinkFailure();

            String metadataOverwriteWarning(String drName);

            String metadataManageFailure();

            String commentsManageFailure();

            String copyMetadata(String path);

            String copyMetadataSuccess();

            String copyMetadataFailure();

            String md5Checksum();

            String checksum();

            String metadataSaveError();

            String error();

            String copyMetadataPrompt();

            SafeHtml fileExistsError(String fileName);

            String fileSaveError(String fileName);

            String listingFailure();

            String metadataSaved();

            String copyMetadataNoResources();

            String loadingMask();

            String saving();

            String copyMetadataDlgWidth();

            String copyMetadataDlgHeight();

            int md5MaxLength();

            String bulkMetadataError();

            String bulkMetadataSuccess();

            String copyPath();
        }

        void deSelectDiskResources();

        void doMoveDiskResources(Folder targetFolder, List<DiskResource> resources);

        Element findGridRow(Element eventTargetElement);

        int findGridRowIndex(Element targetRow);

        List<DiskResource> getAllDiskResources();

        List<DiskResource> getSelectedDiskResources();

        Folder getSelectedUploadFolder();

        GridView getView();

        boolean isSelectAllChecked();

        void setFilePreviewEnabled(boolean filePreviewEnabled);

        void setSelectedDiskResourcesById(List<? extends HasId> diskResourcesToSelect);

        void unRegisterHandler(EventHandler handler);

    }

    HandlerRegistration addBeforeLoadHandler(BeforeLoadEvent.BeforeLoadHandler<FolderContentsLoadConfig> handler);

    Element findGridRow(Element eventTargetElement);

    int findGridRowIndex(Element targetRow);

    DiskResourceColumnModel getColumnModel();

    PagingLoader<FolderContentsLoadConfig,PagingLoadResult<DiskResource>> getGridLoader();

    LiveGridCheckBoxSelectionModel getSelectionModel();

    void setSingleSelect();

    void setColumnPreferences(Map<String, String> preferences);
}

