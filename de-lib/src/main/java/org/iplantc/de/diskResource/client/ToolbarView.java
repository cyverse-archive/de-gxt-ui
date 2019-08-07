package org.iplantc.de.diskResource.client;

import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent.FolderSelectionEventHandler;
import org.iplantc.de.diskResource.client.events.selection.AutomatePathListSelected;
import org.iplantc.de.diskResource.client.events.selection.BulkMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.CopyMetadataSelected.HasCopyMetadataSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiFolderStructureSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiSraFolderStructureSubmitted;
import org.iplantc.de.diskResource.client.events.selection.CreateNewDelimitedFileSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFileSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderConfirmed;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNewPathListSelected;
import org.iplantc.de.diskResource.client.events.selection.CreatePublicLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.DeleteDiskResourcesSelected.HasDeleteDiskResourcesSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.DownloadTemplateSelectedEvent;
import org.iplantc.de.diskResource.client.events.selection.EditFileSelected;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected.HasEditInfoTypeSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.EmptyTrashSelected.HasEmptyTrashSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.ImportFromCogeBtnSelected;
import org.iplantc.de.diskResource.client.events.selection.ImportFromUrlSelected.HasImportFromUrlSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected.HasManageCommentsSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected.HasManageMetadataSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected.HasManageSharingSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.MoveDiskResourcesSelected.HasMoveDiskResourcesSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.NewMultiInputPathListFileSelected;
import org.iplantc.de.diskResource.client.events.selection.OpenNewWindowAtLocationSelected;
import org.iplantc.de.diskResource.client.events.selection.OpenNewWindowSelected;
import org.iplantc.de.diskResource.client.events.selection.OpenTrashFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.RefreshFolderSelected.HasRefreshFolderSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.RenameDiskResourceSelected.HasRenameDiskResourceSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.RequestDOISelected;
import org.iplantc.de.diskResource.client.events.selection.RestoreDiskResourcesSelected.HasRestoreDiskResourceSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SendToCogeSelected.HasSendToCogeSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SendToEnsemblSelected.HasSendToEnsemblSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected.HasShareByDataLinkSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.SimpleDownloadSelected.HasSimpleDownloadSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SimpleUploadSelected.HasSimpleUploadSelectedHandlers;
import org.iplantc.de.diskResource.client.views.search.DiskResourceSearchField;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author jstroot
 */
public interface ToolbarView extends IsWidget,
                                     HasManageCommentsSelectedEventHandlers,
                                     HasManageMetadataSelectedEventHandlers,
                                     HasCopyMetadataSelectedEventHandlers,
                                     HasManageSharingSelectedEventHandlers,
                                     HasShareByDataLinkSelectedEventHandlers,
                                     HasSendToEnsemblSelectedHandlers,
                                     HasSendToCogeSelectedHandlers,
                                     HasDeleteDiskResourcesSelectedEventHandlers,
                                     HasEditInfoTypeSelectedEventHandlers,
                                     HasEmptyTrashSelectedHandlers,
                                     HasMoveDiskResourcesSelectedHandlers,
                                     HasRefreshFolderSelectedHandlers,
                                     HasRenameDiskResourceSelectedHandlers,
                                     HasRestoreDiskResourceSelectedHandlers,
                                     HasSimpleUploadSelectedHandlers,
                                     HasSimpleDownloadSelectedHandlers,
                                     HasImportFromUrlSelectedHandlers,
                                     DownloadTemplateSelectedEvent.HasDownloadTemplateSelectedEventHandlers,
                                     FolderSelectionEventHandler,
                                     DiskResourceSelectionChangedEventHandler,
                                     OpenTrashFolderSelected.HasOpenTrashFolderSelectedHandlers,
                                     BulkMetadataSelected.HasBulkMetadataSelectedHandlers,
                                     AutomatePathListSelected.HasAutomatePathListSelectedHandlers,
                                     NewMultiInputPathListFileSelected.HasNewMultiInputPathListSelectedHandlers,
                                     EditFileSelected.HasEditFileSelectedHandlers,
                                     CreatePublicLinkSelected.HasCreatePublicLinkSelectedHandlers,
                                     CreateNewFolderSelected.HasCreateNewFolderSelectedHandlers,
                                     CreateNcbiFolderStructureSelected.HasCreateNcbiFolderStructureSelectedHandlers,
                                     CreateNewFileSelected.HasCreateNewFileSelectedHandlers,
                                     CreateNewPathListSelected.HasCreateNewPathListSelectedHandlers,
                                     CreateNewDelimitedFileSelected.HasCreateNewDelimitedFileSelectedHandlers,
                                     OpenNewWindowAtLocationSelected.HasOpenNewWindowAtLocationSelectedHandlers,
                                     OpenNewWindowSelected.HasOpenNewWindowSelectedHandlers,
                                     ImportFromCogeBtnSelected.HasImportFromCogeBtnSelectedHandlers,
                                     RequestDOISelected.HasRequestDOISelectedHandlers {
    interface Appearance {

        SafeHtml bulkDownloadInfoBoxHeading();

        SafeHtml bulkDownloadInfoBoxMsg();

        SafeHtml bulkUploadInfoBoxHeading();

        SafeHtml bulkUploadInfoBoxMsg();

        String newPathListMenuText();

        ImageResource newPathListMenuIcon();

        ImageResource trashIcon();

        String moveToTrashMenuItem();

        ImageResource newMdFileIcon();

        ImageResource newShellFileIcon();

        ImageResource newPythonFileIcon();

        ImageResource newPerlFileIcon();

        ImageResource newRFileIcon();

        ImageResource newDelimitedFileIcon();

        String newTabularDataFileMenuItem();

        ImageResource newPlainTexFileIcon();

        String newPlainTextFileMenuItem();

        ImageResource newFileMenuIcon();

        String newFileMenu();

        String duplicateMenuItem();

        ImageResource newFolderIcon();

        String newFolderMenuItem();

        String newDataWindowAtLocMenuItem();

        ImageResource addIcon();

        String newWindow();

        ImageResource importDataIcon();

        String importFromUrlMenuItem();

        String bulkUploadFromDesktop();

        String simpleUploadFromDesktop();

        String uploadMenu();

        String editMenu();

        String renameMenuItem();

        ImageResource fileRenameIcon();

        String editFileMenuItem();

        String editCommentsMenuItem();

        ImageResource userCommentIcon();

        String editInfoTypeMenuItem();

        ImageResource infoIcon();

        String metadataMenuItem();

        String editViewMetadataMenuItem();

        String copyMetadataMenuItem();

        ImageResource metadataIcon();

        String moveMenuItem();

        ImageResource editIcon();

        String downloadMenu();

        String simpleDownloadMenuItem();

        ImageResource downloadIcon();

        String bulkDownloadMenuItem();

        String shareMenu();

        String shareWithCollaboratorsMenuItem();

        ImageResource shareWithCollaboratorsIcon();

        String createPublicLinkMenuItem();

        ImageResource linkAddIcon();

        String shareFolderLocationMenuItem();

        ImageResource shareFolderLocationIcon();

        String sendToCogeMenuItem();

        ImageResource sendToCogeIcon();

        String sendToEnsemblMenuItem();

        ImageResource sendToEnsemblIcon();

        ImageResource sendNcbiSraIcon();

        String refresh();

        String trashMenu();

        String openTrashMenuItem();

        ImageResource openTrashIcon();

        String restore();

        String emptyTrashMenuItem();

        ImageResource emptyTrashIcon();

        String deleteMenuItem();

        ImageResource deleteIcon();

        ImageResource refreshIcon();

        String newRFileMenuItem();

        String newPerlFileMenuItem();

        String newPythonFileMenuItem();

        String newShellFileMenuItem();

        String newMdFileMenuItem();

        String saveMetadataMenuItem();
        
        String sendToNcbiSraItem();

        String importFromCoge();

        String applyBulkMetadata();

        String selectMetadata();

        String requestDOI();

        String doiLinkMsg();

        String needDOI();

        String downloadTemplateMenuItem();

        String doiUserAgreement();

        String automateHTPathListMenuItem();

        String newMultiInputPathListText();

        String automatePathListMenuItem();

        String automateMultiInputPathListMenuItem();

        String doiDlgWidth();

        String doiDlgHeight();
    }

    interface Presenter extends CreateNewFolderConfirmed.HasCreateNewFolderConfirmedHandlers,
                                CreateNcbiSraFolderStructureSubmitted.HasCreateNcbiSraFolderStructureSubmittedHandlers {

        interface Appearance {

            String emptyTrash();

            String emptyTrashWarning();

            String cogeSearchError();

            String cogeImportGenomeError();

            String cogeImportGenomeSucess();

            String importFromCoge();

            String templatesError();

            String applyBulkMetadata();

            String overWiteMetadata();

            String doiRequestFail();

            String doiRequestSuccess();
        }

        ToolbarView getView();
    }

    DiskResourceSearchField getSearchField();

    void maskSendToCoGe();

    void maskSendToEnsembl();

    void unmaskSendToCoGe();

    void unmaskSendToEnsembl();

}
