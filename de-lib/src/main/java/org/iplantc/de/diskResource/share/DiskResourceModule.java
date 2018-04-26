package org.iplantc.de.diskResource.share;

import jsinterop.annotations.JsType;

/**
 * @author jstroot
 */

@JsType
public interface DiskResourceModule {

    @JsType
    interface Ids {

        String DISK_RESOURCE_VIEW = ".diskResourceView";


        String MENU_BAR = ".menuBar";
        String GRID = ".grid";
        String NAVIGATION = ".navigation";
        String FILE_MENU = ".fileMenu";
        String UPLOAD_MENU = ".uploadMenu";
        String EDIT_MENU = ".editMenu";
        String DOWNLOAD_MENU = ".downloadMenu";
        String REFRESH_BUTTON = ".refreshButton";
        String SHARE_MENU = ".shareMenu";
        String TRASH_MENU = ".trashMenu";
        String SEARCH_FIELD = ".searchField";
        String MENU_ITEM_SIMPLE_UPLOAD = ".simpleUpload";
        String MENU_ITEM_BULK_UPLOAD = ".bulkUpload";
        String MENU_ITEM_IMPORT_FROM_URL = ".importFromUrl";
        String MENU_ITEM_NEW_WINDOW = ".newWindow";
        String MENU_ITEM_NEW_WINDOW_AT_LOC = ".newWindowLoc";
        String MENU_ITEM_NEW_FOLDER = ".newFolder";
        String MENU_ITEM_NEW_PLAIN_TEXT = ".newPlainText";
        String MENU_ITEM_NEW_TABULAR_DATA = ".newTabularData";
        String MENU_ITEM_NEW_R_DATA = ".newRData";
        String MENU_ITEM_NEW_PYTHON_DATA = ".newPythonData";
        String MENU_ITEM_NEW_PERL_DATA = ".newPerlData";
        String MENU_ITEM_NEW_SHELL_DATA = ".newShellData";
        String MENU_ITEM_NEW_MD_DATA = ".newMdData";
        String MENU_ITEM_NEW_PATH_LIST = ".newPathList";
        String MENU_ITEM_MOVE_TO_TRASH = ".moveToTrash";
        String MENU_ITEM_RENAME = ".rename";
        String MENU_ITEM_MOVE = ".move";
        String MENU_ITEM_DELETE = ".delete";
        String MENU_ITEM_EDIT_FILE = ".editFile";
        String MENU_ITEM_EDIT_INFO_TYPE = ".editInfoType";
        String METADATA_MENU = ".metadata";
        String MENU_ITEM_SIMPLE_DOWNLOAD = ".simpleDownload";
        String MENU_ITEM_BULK_DOWNLOAD = ".bulkDownload";
        String MENU_ITEM_SHARE_WITH_COLLABORATORS = ".shareWithCollaborators";
        String MENU_ITEM_CREATE_PUBLIC_LINK = ".createPublicLink";
        String MENU_ITEM_SHARE_FOLDER_LOCATION = ".shareFolderLocation";
        String MENU_ITEM_SEND_TO_COGE = ".sendToCoge";
        String MENU_ITEM_SEND_TO_ENSEMBL = ".sendToEnsembl";
        String MENU_ITEM_SEND_TO_TREE_VIEWER = ".sendToTreeViewer";
        String MENU_ITEM_OPEN_TRASH = ".openTrash";
        String MENU_ITEM_RESTORE = ".restore";
        String MENU_ITEM_EMPTY_TRASH = ".emptyTrash";


        String ACTION_CELL_DATA_LINK_ADD = ".actionCellDataLinkAdd";
        String ACTION_CELL_DATA_LINK = ".actionCellDataLink";
        String ACTION_CELL_SHARE = ".actionCellShare";
        String ACTION_CELL_METADATA = ".actionCellMetadata";
        String ACTION_CELL_FAVORITE = ".favorite";
        String ACTION_CELL_COMMENTS = ".comments";
        String ACTION_CEL_COPYPATH =".copyPath";
        
        String NAME_CELL = ".nameCell";
        String PATH_CELL = ".pathCell";
        String DETAILS = ".details";
        String NAV_TREE = ".tree";
        String TREE_COLLAPSE = ".collapse";

        String MENU_ITEM_METADATA_COPY = ".mcopy";
        String MENU_ITEM_METADATA_SAVE = ".msave";
        String MENU_ITEM_METADATA_EDIT = ".medit";
        String MENU_ITEM_DOWNLOAD_TEMPLATE =".downloadTemplate";

        String MENU_ITEM_NCBI_SRA = ".ncbisra";

        String MENU_ITEM_IMPORT_FROM_COGE = ".CogeImport";

        String MENU_ITEM_BULK_METADATA = ".bulkmetadata";
        String MENU_ITEM_REQUEST_DOI = ".requestdoi";
        String MENU_ITEM_SELECTFILE = ".selectfile";

        String SHARING_DLG = "dataSharingDlg";
        String SHARING_VIEW = ".view";
        String SHARING_PERMS = ".permPanel";
        String DOT_MENU = ".dotMenu";
        String MENU_ITEM_AUTOMATE_HT_PATH = ".automateHTPath";
        String MENU_ITEM_EDIT_COMMENTS = ".editComments";
        String DATA_LINK_DLG = "dataLinkDlg";
        String OK_BTN = ".okBtn";
        String DATA_LINK_URL = ".urlField";
        String DATA_LINk_URL_INPUT = ".urlFieldInput";
        String MD5_DIALOG = "md5Dlg";
        String CREATE_FOLDER_DLG = "createFolderDlg";
        String FOLDER_NAME = ".folderName";
        String CREATE_NCBI_STRUCTURE_DLG = "createNcbiFolderDlg";
        String NCBI_PROJECT_NAME = ".projectName";
        String NCBI_SAMPLE_NUM = ".sampleNum";
        String NCBI_LIB_NUM = ".libNum";
        String CREATE_PUBLIC_LINK_DLG = "createPublicLinkDlg";
        String DONE_BTN = ".doneBtn";
        String VIEW = ".view";
        String ADVANCED_SHARING_BTN = ".advancedSharingBtn";
        String COLLAPSE_ALL_BTN = ".collapseAllBtn";
        String SHOW_LINK_BTN = ".showLinkBtn";
        String CREATE_LINK_BTN = ".createLinkBtn";
        String EXPAND_ALL_BTN = ".expandAllBtn";
        String GENOME_SEARCH_DLG = "genomeSearchDlg";
        String GENOME_SEARCH_FIELD = ".searchField";
        String GENOME_IMPORT_BTN = ".importBtn";
        String RENAME_RESOURCE_DLG = "renameResourceDlg";
        String TEXT_FIELD = ".txtField";
        String DETAILS_LAST_MODIFIED = ".lastModified";
        String DETAILS_DATE_SUBMITTED = ".dateSubmitted";
        String DETAILS_PERMISSIONS = ".permissions";
        String DETAILS_SHARE = ".share";
        String DETAILS_SIZE = ".size";
        String DETAILS_TYPE = ".type";
        String DETAILS_INFO_TYPE = ".infoType";
        String DETAILS_MD5 = ".md5";
        String DETAILS_SEND_TO = ".sendTo";
        String DETAILS_TAGS = ".tags";
        String INFO_TYPE_DLG = "infoTypeDlg";
        String INFO_TYPE_DROPDOWN = ".infoTypeList";
        String MULTI_FILE_WIDGET_TOOLBAR = ".toolbar";
        String MULTI_FILE_WIDGET_ADD = ".addBtn";
        String MULTI_FILE_WIDGET_DELETE = ".deleteBtn";
        String METADATA_COPY_DLG = "metadataCopyDlg";
        String SAVE_AS_DIALOG = "saveAsDlg";
        String FILE_NAME = ".fileName";
        String METADATA_DESC_DLG = "metadataDescDlg";
        String LINK_TEXT = ".linkText";
        String LINK_TEXT_INPUT = ".linkTextInput";
        String SHARE_LINK_DLG = "shareLinkDlg";
        String MENU_ITEM_NEW_MULTI_INPUT_PATH_LIST = ".multiInputPathList";
        String FILE_SELECTOR_BROWSE_BTN = ".browseBtn";
        String FILE_SELECTOR_RESET_BTN = ".resetBtn";
        String FILE_SELECTOR_INPUT = ".inputField";
    }

    interface MetadataIds {

        String METADATA_WINDOW = "metadataWindow";
        String METADATA_VIEW = ".metadataView";
        String ADD_METADATA = ".addMetadata";
        String DELETE_METADATA = ".deleteMetadata";
        String TEMPLATES = ".templates";

        String USER_METADATA = ".userMetadata";
        String USER_METADATA_COLLAPSE = ".collapseBtn";
        String METADATA_TEMPLATE = ".metadataTemplate";
        String METADATA_TEMPLATE_COLLAPSE = ".collapseBtn";
        String EDIT_METADATA = ".editMetadata";

        String SELECT_TEMPLATE_DLG = "selectMetadataTemplate";
        String SELECT_TEMPLATE_OK_BTN_ID =".okBtn";
        String SELECT_TEMPLATE_CLOSE_BTN_ID =".closeBtn";

        String DOWNLOAD_TEMPLATE_CELL = ".downloadTemplate";
        String TEMPLATE_INFO_CELL = ".templateInfo";
        String SAVE_METADATA_TO_FILE = ".saveMetadataToFile";
    }
}
