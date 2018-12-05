package org.iplantc.de.diskResource.client.presenters.grid;

import static java.util.stream.Collectors.toList;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.dataLink.DataLinkFactory;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.MetadataCopyRequest;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorCode;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.BulkMetadataView;
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
import org.iplantc.de.diskResource.client.events.selection.BulkMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.CopyMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.CopyPathSelected;
import org.iplantc.de.diskResource.client.events.selection.DNDDiskResourcesCompleted;
import org.iplantc.de.diskResource.client.events.selection.DownloadTemplateSelectedEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.Md5ValueClicked;
import org.iplantc.de.diskResource.client.events.selection.MetadataInfoBtnSelected;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.SetInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.ShareResourcesLinkDialogFactory;
import org.iplantc.de.diskResource.client.model.DiskResourceModelKeyProvider;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.SelectDiskResourceByIdStoreAddHandler;
import org.iplantc.de.diskResource.client.views.dialogs.InfoTypeEditorDialog;
import org.iplantc.de.diskResource.client.views.dialogs.Md5DisplayDialog;
import org.iplantc.de.diskResource.client.views.dialogs.MetadataCopyDialog;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.grid.DiskResourceColumnModel;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.BulkMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.ManageMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.MetadataTemplateDescDlg;
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

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author jstroot
 */
public class GridViewPresenterImpl implements Presenter,
                                              DiskResourcePathSelectedEvent.DiskResourcePathSelectedEventHandler,
                                              DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler,
                                              MetadataInfoBtnSelected.MetadataInfoBtnSelectedHandler {

    class SaveMetadataCallback implements AsyncCallback<String> {
        private final SaveAsDialog saveDialog;

        private SaveMetadataCallback(SaveAsDialog saveDialog) {
            this.saveDialog = saveDialog;
        }

        @Override
        public void onFailure(Throwable caught) {
            saveDialog.unmask();
            String fileName = saveDialog.getFileName();
            if (caught.getMessage().contains(DiskResourceErrorCode.ERR_EXISTS.toString())) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.fileExistsError(fileName)));
            } else {
                ErrorHandler.post(appearance.fileSaveError(fileName), caught);
            }
        }

        @Override
        public void onSuccess(String result) {
            saveDialog.hide();
            announcer.schedule(new SuccessAnnouncementConfig(appearance.metadataSaved(), true, 3000));
        }
    }

    private final class CopyMetadataCallback implements AsyncCallback<String> {
        private final IPlantDialog win;

        private CopyMetadataCallback(IPlantDialog win) {
            this.win = win;
        }

        @Override
        public void onFailure(Throwable caught) {
            win.unmask();
            ErrorHandler.post(appearance.copyMetadataFailure(), caught);

        }

        @Override
        public void onSuccess(String result) {
            announcer.schedule(new SuccessAnnouncementConfig(appearance.copyMetadataSuccess()));
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
            showPublicLink(result);
        }
    }

    protected void showPublicLink(final List<DataLink> result) {
        ShareResourceLinkDialog dlg = srldFactory.create(false);
        dlg.setHeading(appearance.dataLinkTitle());
        dlg.show(result.get(0).getDownloadUrl());
    }


    @Inject IplantAnnouncer announcer;
    @Inject DiskResourceServiceFacade diskResourceService;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject FileSystemMetadataServiceFacade metadataService;
    @Inject AsyncProviderWrapper<InfoTypeEditorDialog> infoTypeDialogProvider;
    @Inject AsyncProviderWrapper<CommentsDialog> commentDialogProvider;
    @Inject AsyncProviderWrapper<ManageMetadataDialog> metadataDialogProvider;
    @Inject AsyncProviderWrapper<DataSharingDialog> dataSharingDialogProvider;
    @Inject AsyncProviderWrapper<SaveAsDialog> saveAsDialogProvider;
    @Inject DiskResourceErrorAutoBeanFactory drErrorFactory;
    @Inject DiskResourceAutoBeanFactory factory;
    @Inject SearchAutoBeanFactory searchFactory;
    @Inject SearchModelUtils searchModelUtils;
    @Inject AsyncProviderWrapper<MetadataCopyDialog> copyMetadataDlgProvider;
    @Inject AsyncProviderWrapper<Md5DisplayDialog> md5DisplayDlgProvider;
    @Inject AsyncProviderWrapper<SelectMetadataTemplateDialog> selectMetaTemplateDlgProvider;
    @Inject AsyncProviderWrapper<BulkMetadataDialog> bulkMetadataDlgProvider;
    @Inject AsyncProviderWrapper<MetadataTemplateDescDlg> metadataTemplateDescDlgProvider;

    @Inject MetadataView.Presenter.Appearance metadataAppearance;

    @Inject DataLinkFactory dlFactory;
    @Inject
    ShareResourcesLinkDialogFactory srldFactory;
    final Logger LOG = Logger.getLogger(GridViewPresenterImpl.class.getName());

    EventBus eventBus;
    private final Appearance appearance;
    private final ListStore<DiskResource> listStore;
    private final NavigationView.Presenter navigationPresenter;
    private final HashMap<EventHandler, HandlerRegistration> registeredHandlers = Maps.newHashMap();
    private final GridView view;
    private boolean filePreviewEnabled = true;
    private HandlerManager handlerManager;
    List<InfoType> infoTypes;

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
        cm.addHasCopyPathSelectedEventHandlers(this);

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

    @Override
    public void setInfoTypes(List<InfoType> infoTypes) {
        this.infoTypes = infoTypes;
    }

    // </editor-fold>

    // <editor-fold desc="Event Handlers">
    @Override
    public void doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent event) {
        Splittable splittable = event.getQueryTemplate();

        DiskResourceQueryTemplate folder = searchModelUtils.convertSplittableToTemplate(splittable);
        doFolderSelected(folder);
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
        final String currentType = event.getSelectedDiskResources().iterator().next().getInfoType();
        infoTypeDialogProvider.get(new AsyncCallback<InfoTypeEditorDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(final InfoTypeEditorDialog result) {
                result.addOkButtonSelectHandler(selectEvent -> {
                    String newType = result.getSelectedValue().toString();
                    setInfoType(event.getSelectedDiskResources(), newType);
                });
                result.show();
                result.addInfoTypes(infoTypes);
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
    public void onCopyPathSelected(CopyPathSelected event) {
        final DiskResource dr = event.getDiskResource();
        ShareResourceLinkDialog dialog = srldFactory.create(false);
        dialog.setHeading(appearance.copyPath());
        dialog.show(dr.getPath());
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

        copyMetadataDlgProvider.get(new AsyncCallback<MetadataCopyDialog>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(MetadataCopyDialog dialog) {
                dialog.addOkButtonSelectHandler(selectEvent -> {
                    List<HasPath> paths = dialog.getValue();
                    if (paths == null || paths.size() == 0) {
                        AlertMessageBox amb = getNoResourcesMessageBox(selected);
                        amb.show();
                        return;
                    }
                    dialog.mask(appearance.loadingMask());
                    copyMetadata(dialog.getSource(), paths, dialog);

                });
                dialog.addCancelButtonSelectHandler(selectEvent -> dialog.hide());
                dialog.show(selected);
            }
        });
    }

    AlertMessageBox getNoResourcesMessageBox(DiskResource selected) {
        return new AlertMessageBox(appearance.copyMetadata(selected.getPath()),
                                   appearance.copyMetadataNoResources());
    }

    @Override
    public void onBulkMetadataSelected(BulkMetadataSelected event) {
        final BulkMetadataView.BULK_MODE mode = event.getMode();
        DiskResource destFolder = view.getSelectionModel().getSelectedItem();
        String destPath = destFolder.getPath();
        bulkMetadataDlgProvider.get(new AsyncCallback<BulkMetadataDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(BulkMetadataDialog dialog) {
                dialog.show(mode);
                dialog.addOkButtonSelectHandler(selectEvent -> {
                    if (dialog.isValid()) {
                        submitBulkMetadataFromExistingFile(dialog.getSelectedPath(), destPath);
                        dialog.hide();
                    }
                });
                dialog.addCancelButtonSelectHandler(selectEvent -> dialog.hide());
            }
        });
    }

    void submitBulkMetadataFromExistingFile(String filePath,
                                            String destFolder) {
        diskResourceService.setBulkMetadataFromFile(filePath, destFolder, new DataCallback<String>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.bulkMetadataError()));
            }

            @Override
            public void onSuccess(String result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.bulkMetadataSuccess()));
            }
        });
    }

    MetadataCopyRequest buildCopyRequest(List<HasPath> paths) {
        MetadataCopyRequest request = factory.metadataCopyRequest().as();
        List<String> ids = paths.stream()
                                .map(hasPath -> String.valueOf(((DiskResource)hasPath).getId()))
                                .collect(toList());
        request.setDestinationIds(ids);
        return request;
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
                result.setHeading(appearance.dataLinkTitle());
                result.show(event.getDiskResourceToShare());
                result.addDialogHideHandler(hideEvent -> {
                    final List<DiskResource> selection = getSelectedDiskResources();
                    if (selection != null && selection.size() == 1) {
                        Iterator<DiskResource> it = selection.iterator();
                        DiskResource next = it.next();
                        if (!next.isFilter()) {
                            fetchDetails(next);
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
            ShareResourceLinkDialog dialog = srldFactory.create(false);
            dialog.setHeading(appearance.copyPath());
            dialog.show(GWT.getHostPageBaseURL() + "?type=data&folder=" + toBeShared.getPath());
        } else {
            diskResourceService.listDataLinks(diskResourceUtil.asStringPathList(Arrays.asList(toBeShared)),
                                              new DataCallback<FastMap<List<DataLink>>>() {

                                                  @Override
                                                  public void onFailure(Integer statusCode,
                                                                        Throwable exception) {
                                                      ErrorHandler.post(appearance.createDataLinksError(),
                                                                        exception);
                                                  }

                                                  @Override
                                                  public void onSuccess(FastMap<List<DataLink>> result) {
                                                      List<DataLink> dlList =
                                                              result.get(toBeShared.getPath());
                                                      if (dlList == null || dlList.isEmpty()) {
                                                          diskResourceService.createDataLinks(Arrays.asList(
                                                                  toBeShared.getPath()),
                                                                                              new CreateDataLinksCallback());
                                                      } else {
                                                          showPublicLink(dlList);
                                                      }

                                                  }
                                              });

        }
    }

    @Override
    public void onSetInfoTypeSelected(SetInfoTypeSelected event) {
        setInfoType(Arrays.asList(event.getDiskResource()), event.getInfoType());
    }

    @Override
    public void onMd5Clicked(Md5ValueClicked event) {
        File f = (File)event.getDiskResource();
        md5DisplayDlgProvider.get(new AsyncCallback<Md5DisplayDialog>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(Md5DisplayDialog dialog) {
                dialog.show(f.getMd5());
            }
        });
    }

    // </editor-fold>

    @Override
    public void deSelectDiskResources() {
        view.getSelectionModel().deselectAll();
    }

    @Override
    public void doMoveDiskResources(Folder targetFolder, List<DiskResource> resources) {
        ensureHandlers().fireEvent(new DNDDiskResourcesCompleted(targetFolder, resources));
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
        FolderContentsLoadConfig loadConfig = gridLoader.getLastLoadConfig();
        loadConfig.setFolder(selectedFolder);
        loadConfig.setOffset(0);
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
                                        }
                                    });

    }

    void setInfoType(final List<DiskResource> resources, String newType) {
        for (DiskResource dr : resources) {
            diskResourceService.setFileType(dr.getPath(), newType, new DataCallback<String>() {

                @Override
                public void onFailure(Integer statusCode,
                                      Throwable arg0) {
                    ErrorHandler.post(arg0);
                }

                @Override
                public void onSuccess(String arg0) {
                    // Fetching the details will update the item in the grid
                    fetchDetails(dr);
                }
            });
        }
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

    void copyMetadata(final DiskResource selected, List<HasPath> paths, MetadataCopyDialog dialog) {
        diskResourceService.copyMetadata(selected.getId(),
                                         buildCopyRequest(paths),
                                         new CopyMetadataCallback(dialog));
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

                saveDialog.addOkButtonSelectHandler(selectEvent -> {
                    saveDialog.mask(appearance.saving());
                    String destination = saveDialog.getSelectedFolder().getPath() + "/"
                                         + saveDialog.getFileName();
                    diskResourceService.saveMetadata(event.getDiskResource().getId(),
                                                     destination,
                                                     true,
                                                     new SaveMetadataCallback(saveDialog));

                });
                saveDialog.addCancelButtonSelectHandler(selectEvent -> saveDialog.hide());
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
        diskResourceService.getMetadataTemplateListing(new AsyncCallback<List<MetadataTemplateInfo>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(metadataAppearance.templateinfoError(), caught);
            }

            @Override
            public void onSuccess(List<MetadataTemplateInfo> result) {

                selectMetaTemplateDlgProvider.get(new AsyncCallback<SelectMetadataTemplateDialog>() {
                    @Override
                    public void onFailure(Throwable throwable) {}

                    @Override
                    public void onSuccess(SelectMetadataTemplateDialog dialog) {
                        dialog.addOkButtonSelectHandler(selectEvent -> {
                            final String encodedSimpleDownloadURL =
                                    diskResourceService.downloadTemplate(dialog.getSelectedTemplate().getId());
                            WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
                        });
                        dialog.show(result, false);
                        dialog.addMetadataInfoBtnSelectedHandler(GridViewPresenterImpl.this);
                    }
                });
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

    @Override
    public HandlerRegistration addDNDDiskResourcesCompletedHandler(DNDDiskResourcesCompleted.DNDDiskResourcesCompletedHandler handler) {
        return ensureHandlers().addHandler(DNDDiskResourcesCompleted.TYPE, handler);
    }

    @Override
    public void onMetadataInfoBtnSelected(MetadataInfoBtnSelected event) {
        metadataTemplateDescDlgProvider.get(new AsyncCallback<MetadataTemplateDescDlg>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(MetadataTemplateDescDlg result) {
                MetadataTemplateInfo info = event.getTemplateInfo();
                result.show(info);
            }
        });
    }
}
