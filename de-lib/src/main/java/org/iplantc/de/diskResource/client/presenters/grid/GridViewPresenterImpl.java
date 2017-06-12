package org.iplantc.de.diskResource.client.presenters.grid;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.GridView.Presenter;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.events.DiskResourceNameSelectedEvent;
import org.iplantc.de.diskResource.client.events.DiskResourcePathSelectedEvent;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.events.RequestDiskResourceFavoriteEvent;
import org.iplantc.de.diskResource.client.events.ShowFilePreviewEvent;
import org.iplantc.de.diskResource.client.events.TemplateDownloadEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.selection.CopyMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.DownloadTemplateSelectedEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.Md5ValueClicked;
import org.iplantc.de.diskResource.client.events.selection.ResetInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewFactory;
import org.iplantc.de.diskResource.client.model.DiskResourceModelKeyProvider;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.SelectDiskResourceByIdStoreAddHandler;
import org.iplantc.de.diskResource.client.views.dialogs.InfoTypeEditorDialog;
import org.iplantc.de.diskResource.client.views.dialogs.Md5DisplayDialog;
import org.iplantc.de.diskResource.client.views.dialogs.MetadataCopyDialog;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.grid.DiskResourceColumnModel;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.ManageMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.SelectMetadataTemplateDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.DataSharingDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.ShareResourceLinkDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author jstroot
 */
public class GridViewPresenterImpl implements Presenter,
                                              DiskResourcePathSelectedEvent.DiskResourcePathSelectedEventHandler,
                                              DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler {

    private final class SaveMetadataCallback implements AsyncCallback<String> {
        private final SaveAsDialog saveDialog;

        private SaveMetadataCallback(SaveAsDialog saveDialog) {
            this.saveDialog = saveDialog;
        }

        @Override
        public void onFailure(Throwable caught) {
            saveDialog.unmask();
            String fileName = saveDialog.getFileName();
            if (caught.getMessage().contains("ERR_EXISTS")) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.fileExistsError(fileName)));
            } else {
                ErrorHandler.post(appearance.fileSaveError(fileName), caught);
            }
        }

        @Override
        public void onSuccess(String result) {
            saveDialog.hide();
            IplantAnnouncer.getInstance()
                           .schedule(new SuccessAnnouncementConfig("Metadata saved!", true, 3000));

        }
    }

    private final class CopyMetadataCallback implements AsyncCallback<String> {
        private final DiskResource selected;
        private final IPlantDialog win;
        private final List<HasPath> paths;

        private CopyMetadataCallback(DiskResource selected, IPlantDialog win, List<HasPath> paths) {
            this.selected = selected;
            this.win = win;
            this.paths = paths;
        }

        @Override
        public void onFailure(Throwable caught) {
            win.unmask();
            ErrorHandler.post(appearance.copyMetadataFailure(), caught);

        }

        @Override
        public void onSuccess(String result) {
            IplantAnnouncer.getInstance()
                           .schedule(new SuccessAnnouncementConfig(appearance.copyMetadataSuccess()));
            win.hide();
        }
    }

    private class CreateDataLinksCallback extends DataCallback<List<DataLink>> {

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            ErrorHandler.post(appearance.createDataLinksError(), caught);
        }

        @Override
        public void onSuccess(final List<DataLink> result) {
            shareLinkDialogProvider.get(new AsyncCallback<ShareResourceLinkDialog>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(ShareResourceLinkDialog dlg) {
                    dlg.show(result.get(0).getDownloadUrl());
                }
            });
        }
    }

    @Inject
    IplantAnnouncer announcer;
    @Inject
    DiskResourceServiceFacade diskResourceService;
    @Inject
    DiskResourceUtil diskResourceUtil;
    @Inject
    FileSystemMetadataServiceFacade metadataService;
    @Inject
    AsyncProviderWrapper<InfoTypeEditorDialog> infoTypeDialogProvider;
    @Inject
    AsyncProviderWrapper<CommentsDialog> commentDialogProvider;
    @Inject
    AsyncProviderWrapper<ManageMetadataDialog> metadataDialogProvider;
    @Inject
    AsyncProviderWrapper<DataSharingDialog> dataSharingDialogProvider;
    @Inject
    AsyncProviderWrapper<ShareResourceLinkDialog> shareLinkDialogProvider;
    @Inject
    AsyncProviderWrapper<SaveAsDialog> saveAsDialogProvider;
    @Inject
    DiskResourceErrorAutoBeanFactory drFactory;
    @Inject
    MetadataCopyDialog mCopyDialog;

    EventBus eventBus;
    private final Appearance appearance;
    private final ListStore<DiskResource> listStore;
    private final NavigationView.Presenter navigationPresenter;
    private final HashMap<EventHandler, HandlerRegistration> registeredHandlers = Maps.newHashMap();
    private final GridView view;
    private boolean filePreviewEnabled = true;
    private DiskResourceView.Presenter parentPresenter;
    private HandlerManager handlerManager;

    @Inject
    GridViewPresenterImpl(final GridViewFactory gridViewFactory,
                          final FolderContentsRpcProxyFactory folderContentsProxyFactory,
                          final Presenter.Appearance appearance,
                          final EventBus eventBus,
                          @Assisted final NavigationView.Presenter navigationPresenter,
                          @Assisted final List<InfoType> infoTypeFilters,
                          @Assisted final TYPE entityType) {
        this.appearance = appearance;
        this.navigationPresenter = navigationPresenter;
        this.listStore = getDiskResourceListStore();
        this.eventBus = eventBus;
        GridView.FolderContentsRpcProxy folderContentsRpcProxy =
                folderContentsProxyFactory.createWithEntityType(infoTypeFilters, entityType);
        setupHandlers();

        this.view = gridViewFactory.create(this, listStore, folderContentsRpcProxy);

        // Wire up Column Model events
        DiskResourceColumnModel cm = this.view.getColumnModel();
        cm.addDiskResourceNameSelectedEventHandler(this);
        cm.addManageSharingSelectedEventHandler(this);
        cm.addManageMetadataSelectedEventHandler(this);
        cm.addCopyMetadataSelectedEventHandler(this);
        cm.addShareByDataLinkSelectedEventHandler(this);
        cm.addManageFavoritesEventHandler(this);
        cm.addManageCommentsSelectedEventHandler(this);
        cm.addDiskResourcePathSelectedEventHandler(this);

        // Fetch Details
        this.view.addDiskResourceSelectionChangedEventHandler(this);
    }

    private void setupHandlers() {
        clearHandlers();
        eventBus.addHandler(TemplateDownloadEvent.TYPE, this);
        eventBus.addHandler(SaveMetadataSelected.TYPE, this);
    }

    @Override
    public HandlerRegistration addStoreUpdateHandler(StoreUpdateEvent.StoreUpdateHandler<DiskResource> handler) {
        return listStore.addStoreUpdateHandler(handler);
    }

    // </editor-fold>

    // <editor-fold desc="Event Handlers">
    @Override
    public void doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent event) {
        doFolderSelected(event.getQueryTemplate());
    }

    @Override
    public void onDiskResourceNameSelected(DiskResourceNameSelectedEvent event) {
        if (!(event.getSelectedItem() instanceof File) || !filePreviewEnabled) {
            return;
        }
        eventBus.fireEvent(new ShowFilePreviewEvent((File)event.getSelectedItem(), null));
    }

    @Override
    public void onDiskResourcePathSelected(DiskResourcePathSelectedEvent event) {
        final OpenFolderEvent openFolderEvent =
                new OpenFolderEvent(diskResourceUtil.parseParent(event.getSelectedDiskResource()
                                                                      .getPath()), true);
        eventBus.fireEvent(openFolderEvent);
    }

    @Override
    public void onDiskResourceSelectionChanged(DiskResourceSelectionChangedEvent event) {
        final List<DiskResource> selection = event.getSelection();
        if (selection == null || selection.size() != 1 || selection.iterator().next().isFilter()) {
            // Only call get stat for single selections
            return;
        }
        fetchDetails(selection.iterator().next());
    }

    @Override
    public void onEditInfoTypeSelected(final EditInfoTypeSelected event) {
        Preconditions.checkState(event.getSelectedDiskResources().size() == 1,
                                 "Only one Disk Resource should be selected, but there are %i",
                                 getSelectedDiskResources().size());

        final String infoType = event.getSelectedDiskResources().iterator().next().getInfoType();
        infoTypeDialogProvider.get(new AsyncCallback<InfoTypeEditorDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig("AsyncProvider failed"));
            }

            @Override
            public void onSuccess(final InfoTypeEditorDialog result) {
                result.addOkButtonSelectHandler(new SelectEvent.SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event1) {
                        String newType = result.getSelectedValue().toString();
                        setInfoType(event.getSelectedDiskResources().iterator().next(), newType);
                    }
                });
                result.show(InfoType.fromTypeString(infoType));
            }
        });
    }

    @Override
    public void onFavoriteRequest(RequestDiskResourceFavoriteEvent event) {
        final DiskResource diskResource = event.getDiskResource();
        Preconditions.checkNotNull(diskResource);
        if (!diskResource.isFavorite()) {
            metadataService.addToFavorites(diskResource.getId(), new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(appearance.markFavoriteError(), caught);
                }

                @Override
                public void onSuccess(String result) {
                    updateFav(diskResource, true);
                }
            });
        } else {
            metadataService.removeFromFavorites(diskResource.getId(), new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(appearance.removeFavoriteError(), caught);
                }

                @Override
                public void onSuccess(String result) {
                    updateFav(diskResource, false);
                }
            });
        }
    }

    @Override
    public void onFolderSelected(FolderSelectionEvent event) {
        doFolderSelected(event.getSelectedFolder());
    }

    @Override
    public void onManageCommentsSelected(ManageCommentsSelected event) {
        final DiskResource dr = event.getDiskResource();
        commentDialogProvider.get(new AsyncCallback<CommentsDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.commentsManageFailure()));
            }

            @Override
            public void onSuccess(CommentsDialog result) {
                result.show(dr, hasOwnPermissions(dr), metadataService);
            }
        });
    }

    @Override
    public void onRequestManageMetadataSelected(ManageMetadataSelected event) {
        final DiskResource selected = event.getDiskResource();

        metadataDialogProvider.get(new AsyncCallback<ManageMetadataDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.metadataManageFailure()));
            }

            @Override
            public void onSuccess(ManageMetadataDialog result) {
                result.show(selected);
            }
        });
    }

    @Override
    public void onRequestCopyMetadataSelected(CopyMetadataSelected event) {
        final DiskResource selected = event.getDiskResource();
        copyMetadata(selected);
    }

    void copyMetadata(final DiskResource selected) {
        mCopyDialog.clearHandlers();
        mCopyDialog.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                List<HasPath> paths = mCopyDialog.getValue();
                if (paths == null || paths.size() == 0) {
                    AlertMessageBox amb = new AlertMessageBox(appearance.copyMetadata(),
                                                              "You must select at least a file or a folder!");
                    amb.show();
                    return;
                }
                mCopyDialog.mask("Loading...");
                doCopyMetadata(mCopyDialog.getSource(), paths);

            }
        });
        mCopyDialog.addCancelButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                mCopyDialog.hide();

            }
        });
        mCopyDialog.clear();
        mCopyDialog.setSource(selected);
        mCopyDialog.setHeader(selected.getPath());
        mCopyDialog.unmask();
        mCopyDialog.show();
    }

    private Splittable buildTargetPaths(List<HasPath> paths) {
        Splittable pathspl = StringQuoter.createSplittable();
        Splittable path_arr = StringQuoter.createIndexed();
        for (HasPath obj : paths) {
            DiskResource dr = (DiskResource)obj;
            StringQuoter.create(String.valueOf(dr.getId())).assign(path_arr, path_arr.size());
        }

        path_arr.assign(pathspl, "destination_ids");
        return pathspl;
    }

    @Override
    public void onRequestManageSharingSelected(final ManageSharingSelected event) {
        dataSharingDialogProvider.get(new AsyncCallback<DataSharingDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.shareFailure()));
            }

            @Override
            public void onSuccess(DataSharingDialog result) {
                result.show(event.getDiskResourceToShare());
                result.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        final List<DiskResource> selection = getSelectedDiskResources();
                        if (selection != null && selection.size() == 1) {
                            Iterator<DiskResource> it = selection.iterator();
                            DiskResource next = it.next();
                            if (!next.isFilter()) {
                                fetchDetails(next);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestShareByDataLinkSelected(ShareByDataLinkSelected event) {
        final DiskResource toBeShared = event.getDiskResourceToShare();
        if (toBeShared instanceof Folder) {
            shareLinkDialogProvider.get(new AsyncCallback<ShareResourceLinkDialog>() {
                @Override
                public void onFailure(Throwable caught) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.shareByLinkFailure()));
                }

                @Override
                public void onSuccess(ShareResourceLinkDialog result) {
                    result.show(GWT.getHostPageBaseURL() + "?type=data&folder=" + toBeShared.getPath());
                }
            });
        } else {
            diskResourceService.createDataLinks(Arrays.asList(toBeShared.getPath()),
                                                new CreateDataLinksCallback());
        }
    }

    @Override
    public void onResetInfoTypeSelected(ResetInfoTypeSelected event) {
        setInfoType(event.getDiskResource(), "");
    }

    @Override
    public void onMd5Clicked(Md5ValueClicked event) {
        File f = (File)event.getDiskResource();
        Md5DisplayDialog dialog = new Md5DisplayDialog(appearance.checksum(),
                                                       appearance.md5Checksum(),
                                                       128,
                                                       f.getMd5(),
                                                       null);
        dialog.show();
    }

    // </editor-fold>

    @Override
    public void deSelectDiskResources() {
        view.getSelectionModel().deselectAll();
    }

    @Override
    public void doMoveDiskResources(Folder targetFolder, List<DiskResource> resources) {
        parentPresenter.doMoveDiskResources(targetFolder, resources);
    }

    @Override
    public Element findGridRow(Element eventTargetElement) {
        return view.findGridRow(eventTargetElement);
    }

    @Override
    public int findGridRowIndex(Element targetRow) {
        return view.findGridRowIndex(targetRow);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        // This method is extended by StoreUpdateHandler, so we must implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DiskResource> getAllDiskResources() {
        return listStore.getAll();
    }

    @Override
    public List<DiskResource> getSelectedDiskResources() {
        return view.getSelectionModel().getSelectedItems();
    }

    @Override
    public Folder getSelectedUploadFolder() {
        return navigationPresenter.getSelectedUploadFolder();
    }

    @Override
    public GridView getView() {
        return view;
    }

    @Override
    public boolean isSelectAllChecked() {
        return view.getSelectionModel().isSelectAllChecked();
    }

    @Override
    public void setFilePreviewEnabled(boolean filePreviewEnabled) {
        this.filePreviewEnabled = filePreviewEnabled;
    }

    @Override
    public void setParentPresenter(DiskResourceView.Presenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void setSelectedDiskResourcesById(List<? extends HasId> diskResourcesToSelect) {
        SelectDiskResourceByIdStoreAddHandler diskResourceByIdStoreAddHandler =
                new SelectDiskResourceByIdStoreAddHandler(diskResourcesToSelect, this);
        HandlerRegistration diskResHandlerReg =
                listStore.addStoreAddHandler(diskResourceByIdStoreAddHandler);
        registeredHandlers.put(diskResourceByIdStoreAddHandler, diskResHandlerReg);
    }

    @Override
    public void unRegisterHandler(EventHandler handler) {
        if (registeredHandlers.containsKey(handler)) {
            registeredHandlers.remove(handler).removeHandler();
        }
    }

    void doFolderSelected(final Folder selectedFolder) {
        final PagingLoader<FolderContentsLoadConfig, PagingLoadResult<DiskResource>> gridLoader =
                view.getGridLoader();
        gridLoader.getLastLoadConfig().setFolder(selectedFolder);
        gridLoader.getLastLoadConfig().setOffset(0);
        gridLoader.load();
    }

    void updateDiskResource(DiskResource diskResource) {
        Preconditions.checkNotNull(diskResource);
        final DiskResource modelWithKey = listStore.findModelWithKey(diskResource.getId());
        if (modelWithKey == null) {
            return;
        }

        final DiskResource updated =
                diskResourceService.combineDiskResources(diskResource, modelWithKey);
        listStore.update(updated);
    }

    void fetchDetails(final DiskResource resource) {
        diskResourceService.getStat(diskResourceUtil.asStringPathTypeMap(Arrays.asList(resource),
                                                                         resource instanceof File ?
                                                                         TYPE.FILE :
                                                                         TYPE.FOLDER),
                                    new DataCallback<FastMap<DiskResource>>() {
                                        @Override
                                        public void onFailure(Integer statusCode, Throwable caught) {
                                            ErrorHandler.post(appearance.retrieveStatFailed(), caught);
                                            ensureHandlers().fireEvent(new FetchDetailsCompleted());

                                            // This unmasks the sendTo.. toolbar buttons
                                            // presenter.unmaskVizMenuOptions();
                                        }

                                        @Override
                                        public void onSuccess(FastMap<DiskResource> drMap) {
                                            /*
                                             * FIXME Fire global event to update diskResource The
                                             * toolbarView will need to listen The gridViewPresenter will
                                             * need to listen -- Fire another event from gridView
                                             */
                                            final DiskResource diskResource =
                                                    drMap.get(resource.getPath());
                                            Preconditions.checkNotNull(diskResource,
                                                                       "This object cannot be null at this point.");
                                            updateDiskResource(diskResource);
                                            ensureHandlers().fireEvent(new FetchDetailsCompleted());

                                            // presenter.getView().unmaskDetailsPanel();
                                            // presenter.unmaskVizMenuOptions();
                                        }
                                    });
        // Need to mask the SendTo... options from the toolbar
        // view.maskSendToCoGe();
        // view.maskSendToEnsembl();
        // view.maskSendToTreeViewer();

    }

    void setInfoType(final DiskResource dr, String newType) {
        diskResourceService.setFileType(dr.getPath(), newType, new DataCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable arg0) {
                ErrorHandler.post(arg0);
            }

            @Override
            public void onSuccess(String arg0) {
                // Fetching the details will update the item in the grid
                fetchDetails(dr);
            }
        });
    }

    void updateFav(final DiskResource diskResource, boolean fav) {
        if (getSelectedDiskResources().size() > 0) {
            Iterator<DiskResource> it = getSelectedDiskResources().iterator();
            if (it.hasNext()) {
                final DiskResource next = it.next();
                if (next.getId().equals(diskResource.getId())) {
                    next.setFavorite(fav);
                    updateDiskResource(next);
                    if (!fav && isViewingFavoritesFolder()) {
                        listStore.remove(next);
                    }
                }
            }
        }
    }

    boolean isViewingFavoritesFolder() {
        return navigationPresenter.getSelectedFolder()
                                  .getName()
                                  .equalsIgnoreCase(NavigationView.FAVORITES_FOLDER_NAME);
    }

    private void doCopyMetadata(final DiskResource selected, List<HasPath> paths) {
        diskResourceService.copyMetadata(selected.getId(),
                                         buildTargetPaths(paths),
                                         new CopyMetadataCallback(selected, mCopyDialog, paths));
    }

    @Override
    public void onRequestSaveMetadataSelected(final SaveMetadataSelected event) {
        saveAsDialogProvider.get(new AsyncCallback<SaveAsDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final SaveAsDialog saveDialog) {

                saveDialog.addOkButtonSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent select_event) {
                        saveDialog.mask("Saving");
                        String destination = saveDialog.getSelectedFolder().getPath() + "/"
                                             + saveDialog.getFileName();
                        diskResourceService.saveMetadata(event.getDiskResource().getId(),
                                                         destination,
                                                         true,
                                                         new SaveMetadataCallback(saveDialog));

                    }
                });
                saveDialog.addCancelButtonSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        saveDialog.hide();

                    }
                });
                saveDialog.show(null);
                saveDialog.toFront();
            }
        });
    }

    @Override
    public void onDownloadClick(TemplateDownloadEvent event) {
        final String encodedSimpleDownloadURL =
                diskResourceService.downloadTemplate(event.getSelectedTemplateId());
        WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
    }

    @Override
    public void onDownloadTemplateSelected(DownloadTemplateSelectedEvent event) {
        final MetadataView.Presenter.Appearance appearance =
                GWT.create(MetadataView.Presenter.Appearance.class);
        diskResourceService.getMetadataTemplateListing(new AsyncCallback<List<MetadataTemplateInfo>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(appearance.templateinfoError(), caught);
            }

            @Override
            public void onSuccess(List<MetadataTemplateInfo> result) {
                final SelectMetadataTemplateDialog view =
                        new SelectMetadataTemplateDialog(result, appearance, false);
                view.getOkButton().addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        final String encodedSimpleDownloadURL =
                                diskResourceService.downloadTemplate(view.getSelectedTemplate().getId());
                        WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
                    }
                });
                view.setModal(false);
                view.setSize("400px", "400px");
                view.setHeading(appearance.selectTemplate());
                view.show();
            }
        });
    }

    @Override
    public HandlerRegistration addFetchDetailsCompletedHandler(FetchDetailsCompleted.FetchDetailsCompletedHandler handler) {
        return ensureHandlers().addHandler(FetchDetailsCompleted.TYPE, handler);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    void clearHandlers() {
      eventBus.removeHandlers(SaveMetadataSelected.TYPE);
      eventBus.removeHandlers(TemplateDownloadEvent.TYPE);
    }

    ListStore<DiskResource> getDiskResourceListStore() {
        return new ListStore<>(new DiskResourceModelKeyProvider());
    }

    boolean hasOwnPermissions(DiskResource dr) {
        return PermissionValue.own.equals(dr.getPermission());
    }

}
