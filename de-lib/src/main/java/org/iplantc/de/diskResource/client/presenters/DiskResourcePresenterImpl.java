package org.iplantc.de.diskResource.client.presenters;

import static com.google.common.base.Preconditions.checkState;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceFavorite;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.CommonUiConstants;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToCoGeEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToEnsemblEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToTreeViewerEvent;
import org.iplantc.de.diskResource.client.events.RootFoldersRetrievedEvent;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiSraFolderStructureSubmitted;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderConfirmed;
import org.iplantc.de.diskResource.client.events.selection.DNDDiskResourcesCompleted;
import org.iplantc.de.diskResource.client.events.selection.DeleteDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.EmptyTrashSelected;
import org.iplantc.de.diskResource.client.events.selection.MoveDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.OpenTrashFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.RefreshFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.RenameDiskResourceSelected;
import org.iplantc.de.diskResource.client.events.selection.RestoreDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToCogeSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToEnsemblSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToTreeViewerSelected;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewPresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.ToolbarViewPresenterFactory;
import org.iplantc.de.diskResource.client.presenters.callbacks.CreateFolderCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceDeleteCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMoveCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceRestoreCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.NcbiSraSetupCompleteCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.RenameDiskResourceCallback;
import org.iplantc.de.diskResource.client.views.dialogs.FolderSelectDialog;
import org.iplantc.de.diskResource.client.views.dialogs.RenameResourceDialog;
import org.iplantc.de.diskResource.client.views.search.DiskResourceSearchField;
import org.iplantc.de.diskResource.share.DiskResourceModule;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DataCallback;

import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jstroot
 */
public class DiskResourcePresenterImpl implements
                                      DiskResourceView.Presenter,
                                      RootFoldersRetrievedEvent.RootFoldersRetrievedEventHandler,
                                      DeleteDiskResourcesSelected.DeleteDiskResourcesSelectedEventHandler,
                                      EmptyTrashSelected.EmptyTrashSelectedHandler,
                                      MoveDiskResourcesSelected.MoveDiskResourcesSelectedHandler,
                                      RenameDiskResourceSelected.RenameDiskResourceSelectedHandler,
                                      RestoreDiskResourcesSelected.RestoreDiskResourcesSelectedHandler,
                                      SendToTreeViewerSelected.SendToTreeViewerSelectedHandler,
                                      SendToEnsemblSelected.SendToEnsemblSelectedHandler,
                                      SendToCogeSelected.SendToCogeSelectedHandler,
                                      DNDDiskResourcesCompleted.DNDDiskResourcesCompletedHandler,
                                      CreateNcbiSraFolderStructureSubmitted.CreateNcbiSraFolderStructureSubmittedHandler,
                                      OpenTrashFolderSelected.OpenTrashFolderSelectedHandler {

    final IplantAnnouncer announcer;
    final DiskResourceAutoBeanFactory drFactory;
    final DiskResourceView view;
    @Inject DiskResourceView.Presenter.Appearance appearance;
    @Inject DiskResourceServiceFacade diskResourceService;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject AsyncProviderWrapper<FolderSelectDialog> folderSelectDialogProvider;
    @Inject AsyncProviderWrapper<RenameResourceDialog> renameResourceDlgProvider;
    @Inject UserInfo userInfo;
    @Inject CommonUiConstants commonUiConstants;

    private final SearchView.Presenter dataSearchPresenter;
    private final DetailsView.Presenter detailsViewPresenter;
    private final EventBus eventBus;
    private final GridView.Presenter gridViewPresenter;
    private final NavigationView.Presenter navigationPresenter;
    List<InfoType> infoTypes;

    @AssistedInject
    DiskResourcePresenterImpl(final DiskResourceViewFactory diskResourceViewFactory,
                              final DiskResourceAutoBeanFactory drFactory,
                              final NavigationView.Presenter navigationPresenter,
                              final GridViewPresenterFactory gridViewPresenterFactory,
                              final SearchView.Presenter dataSearchPresenter,
                              final ToolbarViewPresenterFactory toolbarViewPresenterFactory,
                              final DetailsView.Presenter detailsViewPresenter,
                              final IplantAnnouncer announcer,
                              final EventBus eventBus,
                              @Assisted("hideToolbar") final boolean hideToolbar,
                              @Assisted("hideDetailsPanel") final boolean hideDetailsPanel,
                              @Assisted("singleSelect") final boolean singleSelect,
                              @Assisted("disableFilePreview") final boolean disableFilePreview,
                              @Assisted final HasPath folderToSelect,
                              @Assisted final List<InfoType> infoTypeFilters,
                              @Assisted final TYPE entityType,
                              @Assisted final IsWidget southWidget) {
        this(diskResourceViewFactory,
             drFactory,
             navigationPresenter,
             gridViewPresenterFactory,
             dataSearchPresenter,
             toolbarViewPresenterFactory,
             detailsViewPresenter,
             announcer,
             eventBus,
             infoTypeFilters,
             entityType);
        view.setNorthWidgetHidden(hideToolbar);
        view.setEastWidgetHidden(hideDetailsPanel);
        if (singleSelect) {
            gridViewPresenter.getView().setSingleSelect();
        }
        if (disableFilePreview) {
            gridViewPresenter.setFilePreviewEnabled(false);
        }
        navigationPresenter.setSelectedFolder(folderToSelect);
        view.setSouthWidget(southWidget);
    }

    @AssistedInject
    DiskResourcePresenterImpl(final DiskResourceViewFactory diskResourceViewFactory,
                              final DiskResourceAutoBeanFactory drFactory,
                              final NavigationView.Presenter navigationPresenter,
                              final GridViewPresenterFactory gridViewPresenterFactory,
                              final SearchView.Presenter dataSearchPresenter,
                              final ToolbarViewPresenterFactory toolbarViewPresenterFactory,
                              final DetailsView.Presenter detailsViewPresenter,
                              final IplantAnnouncer announcer,
                              final EventBus eventBus,
                              @Assisted("hideToolbar") final boolean hideToolbar,
                              @Assisted("hideDetailsPanel") final boolean hideDetailsPanel,
                              @Assisted("singleSelect") final boolean singleSelect,
                              @Assisted("disableFilePreview") final boolean disableFilePreview,
                              @Assisted final HasPath folderToSelect,
                              @Assisted final IsWidget southWidget,
                              @Assisted final int southWidgetHeight) {
        this(diskResourceViewFactory,
             drFactory,
             navigationPresenter,
             gridViewPresenterFactory,
             dataSearchPresenter,
             toolbarViewPresenterFactory,
             detailsViewPresenter,
             announcer,
             eventBus,
             Collections.<InfoType> emptyList(),
             null);
        view.setNorthWidgetHidden(hideToolbar);
        view.setEastWidgetHidden(hideDetailsPanel);
        if (singleSelect) {
            gridViewPresenter.getView().setSingleSelect();
        }
        if (disableFilePreview) {
            gridViewPresenter.setFilePreviewEnabled(false);
        }
        navigationPresenter.setSelectedFolder(folderToSelect);
        view.setSouthWidget(southWidget, southWidgetHeight);
    }

    @AssistedInject
    DiskResourcePresenterImpl(final DiskResourceViewFactory diskResourceViewFactory,
                              final DiskResourceAutoBeanFactory drFactory,
                              final NavigationView.Presenter navigationPresenter,
                              final GridViewPresenterFactory gridViewPresenterFactory,
                              final SearchView.Presenter dataSearchPresenter,
                              final ToolbarViewPresenterFactory toolbarViewPresenterFactory,
                              final DetailsView.Presenter detailsViewPresenter,
                              final IplantAnnouncer announcer,
                              final EventBus eventBus,
                              @Assisted("hideToolbar") final boolean hideToolbar,
                              @Assisted("hideDetailsPanel") final boolean hideDetailsPanel,
                              @Assisted("singleSelect") final boolean singleSelect,
                              @Assisted("disableFilePreview") final boolean disableFilePreview,
                              @Assisted final HasPath folderToSelect,
                              @Assisted final List<HasId> selectedResources) {
        this(diskResourceViewFactory,
             drFactory,
             navigationPresenter,
             gridViewPresenterFactory,
             dataSearchPresenter,
             toolbarViewPresenterFactory,
             detailsViewPresenter,
             announcer,
             eventBus,
             Collections.<InfoType> emptyList(),
             null);
        view.setNorthWidgetHidden(hideToolbar);
        view.setEastWidgetHidden(hideDetailsPanel);
        if (singleSelect) {
            gridViewPresenter.getView().setSingleSelect();
        }
        if (disableFilePreview) {
            gridViewPresenter.setFilePreviewEnabled(false);
        }
        navigationPresenter.setSelectedFolder(folderToSelect);
        setSelectedDiskResourcesById(selectedResources);
    }

    DiskResourcePresenterImpl(final DiskResourceViewFactory diskResourceViewFactory,
                              final DiskResourceAutoBeanFactory drFactory,
                              final NavigationView.Presenter navigationPresenter,
                              final GridViewPresenterFactory gridViewPresenterFactory,
                              final SearchView.Presenter dataSearchPresenter,
                              final ToolbarViewPresenterFactory toolbarViewPresenterFactory,
                              final DetailsView.Presenter detailsViewPresenter,
                              final IplantAnnouncer announcer,
                              final EventBus eventBus,
                              final List<InfoType> infoTypeFilters,
                              final TYPE entityType) {
        this.drFactory = drFactory;
        this.navigationPresenter = navigationPresenter;
        this.detailsViewPresenter = detailsViewPresenter;
        this.gridViewPresenter = gridViewPresenterFactory.create(navigationPresenter,
                                                                 infoTypeFilters,
                                                                 entityType);
        this.announcer = announcer;
        this.eventBus = eventBus;
        this.dataSearchPresenter = dataSearchPresenter;
        ToolbarView.Presenter toolbarPresenter = toolbarViewPresenterFactory.create(this);
        this.view = diskResourceViewFactory.create(navigationPresenter,
                                                   gridViewPresenter,
                                                   toolbarPresenter,
                                                   detailsViewPresenter);

        this.navigationPresenter.setMaskable(view);

        this.gridViewPresenter.addDNDDiskResourcesCompletedHandler(this);

        // Detail Presenter
        this.detailsViewPresenter.addManageSharingSelectedEventHandler(this.gridViewPresenter);
        this.detailsViewPresenter.addEditInfoTypeSelectedEventHandler(this.gridViewPresenter);
        this.detailsViewPresenter.addSetInfoTypeSelectedHandler(this.gridViewPresenter);
        this.detailsViewPresenter.addMd5ValueClickedHandler(this.gridViewPresenter);
        this.detailsViewPresenter.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter.getView());
        this.detailsViewPresenter.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter);
        this.detailsViewPresenter.addSendToCogeSelectedHandler(this);
        this.detailsViewPresenter.addSendToEnsemblSelectedHandler(this);
        this.detailsViewPresenter.addSendToTreeViewerSelectedHandler(this);

        // Toolbar Search Field
        DiskResourceSearchField searchField = toolbarPresenter.getView().getSearchField();
        searchField.addSaveDiskResourceQueryClickedEventHandler(this.dataSearchPresenter);
        searchField.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter.getView());
        searchField.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter);

        // Grid Presenter
        this.gridViewPresenter.getView().addBeforeLoadHandler(this.navigationPresenter);
        this.gridViewPresenter.getView()
                              .addDiskResourceNameSelectedEventHandler(this.navigationPresenter);
        this.gridViewPresenter.getView()
                              .addDiskResourcePathSelectedEventHandler(this.navigationPresenter);
        this.gridViewPresenter.getView()
                              .addDiskResourceSelectionChangedEventHandler(this.detailsViewPresenter.getView());
        this.gridViewPresenter.getView()
                              .addDiskResourceSelectionChangedEventHandler(toolbarPresenter.getView());
        this.gridViewPresenter.addStoreUpdateHandler(this.detailsViewPresenter.getView());
        this.gridViewPresenter.addFetchDetailsCompletedHandler(this.detailsViewPresenter.getView());

        // Navigation Presenter
        this.navigationPresenter.addSavedSearchedRetrievedEventHandler(this.dataSearchPresenter);
        this.navigationPresenter.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter.getView());
        this.navigationPresenter.addSubmitDiskResourceQueryEventHandler(this.gridViewPresenter);
        this.navigationPresenter.addRootFoldersRetrievedEventHandler(this);
        this.navigationPresenter.getView().addFolderSelectedEventHandler(this.gridViewPresenter);
        this.navigationPresenter.getView()
                                .addFolderSelectedEventHandler(this.gridViewPresenter.getView());
        this.navigationPresenter.getView().addFolderSelectedEventHandler(toolbarPresenter.getView());
        this.navigationPresenter.getView().addFolderSelectedEventHandler(searchField);
        this.navigationPresenter.getView()
                                .addDeleteSavedSearchClickedEventHandler(this.dataSearchPresenter);
        this.navigationPresenter.addDNDDiskResourcesCompletedHandler(this);
        this.navigationPresenter.addRefreshFolderSelectedHandler(this);

        // Data Search Presenter
        this.dataSearchPresenter.addUpdateSavedSearchesEventHandler(this.navigationPresenter);
        this.dataSearchPresenter.addSavedSearchDeletedEventHandler(searchField);

        // Toolbar Presenter
        toolbarPresenter.getView().addDeleteSelectedDiskResourcesSelectedEventHandler(this);
        toolbarPresenter.getView().addEditInfoTypeSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addEmptyTrashSelectedHandler(this);
        toolbarPresenter.getView().addManageSharingSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addManageMetadataSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addCopyMetadataSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addDownloadTemplateSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addManageCommentsSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addMoveDiskResourcesSelectedHandler(this);
        toolbarPresenter.getView().addRefreshFolderSelectedHandler(this);
        toolbarPresenter.getView().addRenameDiskResourceSelectedHandler(this);
        toolbarPresenter.getView().addRestoreDiskResourcesSelectedHandler(this);
        toolbarPresenter.getView().addShareByDataLinkSelectedEventHandler(this.gridViewPresenter);
        toolbarPresenter.getView().addSendToCogeSelectedHandler(this);
        toolbarPresenter.getView().addSendToEnsemblSelectedHandler(this);
        toolbarPresenter.getView().addSendToTreeViewerSelectedHandler(this);
        toolbarPresenter.getView().addSimpleUploadSelectedHandler(this.navigationPresenter);
        toolbarPresenter.getView().addImportFromUrlSelectedHandler(this.navigationPresenter);
        toolbarPresenter.getView().addOpenTrashFolderSelectedHandler(this);
        toolbarPresenter.getView().addBulkMetadataSelectedHandler(gridViewPresenter);
        toolbarPresenter.addCreateNewFolderConfirmedHandler(this);
        toolbarPresenter.addCreateNcbiSraFolderStructureSubmittedHandler(this);
    }

    void fetchInfoTypes() {
        diskResourceService.getInfoTypes(new DataCallback<List<InfoType>>() {

            @Override
            public void onFailure(Integer statusCode, Throwable arg0) {
                ErrorHandler.post(arg0);
            }

            @Override
            public void onSuccess(List<InfoType> infoTypes) {
                DiskResourcePresenterImpl.this.infoTypes = infoTypes;
                detailsViewPresenter.getView().setInfoTypes(infoTypes);
            }
        });
    }

    // <editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration
            addDiskResourceSelectionChangedEventHandler(DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler handler) {
        return gridViewPresenter.getView().addDiskResourceSelectionChangedEventHandler(handler);
    }

    @Override
    public HandlerRegistration
            addFolderSelectedEventHandler(FolderSelectionEvent.FolderSelectionEventHandler handler) {
        return navigationPresenter.getView().addFolderSelectedEventHandler(handler);
    }

    // </editor-fold>

    // <editor-fold desc="Event Handlers">
    @Override
    public void onDeleteSelectedDiskResourcesSelected(DeleteDiskResourcesSelected event) {
        List<DiskResource> selectedResources = event.getSelectedDiskResources();
        if (!selectedResources.isEmpty() && diskResourceUtil.isOwner(selectedResources)) {

            if (diskResourceUtil.containsTrashedResource(selectedResources) && event.isConfirmDelete()) {
                confirmDelete(selectedResources);
            } else {
                delete(selectedResources, appearance.deleteMsg());
            }
        }
    }

    @Override
    public void onEmptyTrashSelected(EmptyTrashSelected event) {
        final ConfirmMessageBox cmb = getEmptyTrashMessageBox(appearance.emptyTrash());
        cmb.addDialogHideHandler(hideEvent -> {
            if (PredefinedButton.YES.equals(hideEvent.getHideButton())) {
                doEmptyTrash();
            }
        });
        cmb.setWidth(300);
        cmb.show();
    }

    @Override
    public void onMoveDiskResourcesSelected(MoveDiskResourcesSelected event) {
        folderSelectDialogProvider.get(new AsyncCallback<FolderSelectDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final FolderSelectDialog result) {
                result.addOkButtonSelectHandler(selectEvent -> {
                    Folder targetFolder = result.getValue();
                    final List<DiskResource> selectedResources = getSelectedDiskResources();
                    if (diskResourceUtil.isMovable(targetFolder, selectedResources)) {
                        if (canDragDataToTargetFolder(targetFolder, selectedResources)) {
                            doMoveDiskResources(targetFolder, selectedResources);
                        } else {
                            announcer.schedule(new ErrorAnnouncementConfig(appearance.diskResourceIncompleteMove()));
                            view.unmask();
                        }
                    } else {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.permissionErrorMessage()));
                        view.unmask();
                    }
                });

                result.show(navigationPresenter.getSelectedFolder(), Collections.<InfoType> emptyList());
            }
        });
    }

    @Override
    public void onRefreshFolderSelected(RefreshFolderSelected event) {
        refreshFolder(event.getSelectedFolder());
    }

    void refreshFolder(final Folder selectedFolder) {
        checkState(selectedFolder != null, "Selected folder should not be null");
        view.mask(appearance.loadingMask());
        diskResourceService.refreshFolder(selectedFolder, new DataCallback<List<Folder>>() {
            @Override
            public void onSuccess(List<Folder> result) {
                view.unmask();
            }

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                view.unmask();
                announcer.schedule(new ErrorAnnouncementConfig(appearance.folderRefreshFailed(selectedFolder.getName())));
            }
        });
    }

    @Override
    public void onRenameDiskResourceSelected(RenameDiskResourceSelected event) {
        renameResourceDlgProvider.get(new AsyncCallback<RenameResourceDialog>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(RenameResourceDialog dialog) {
                DiskResource resource;
                if (!getSelectedDiskResources().isEmpty() && (getSelectedDiskResources().size() == 1)) {
                    resource = getSelectedDiskResources().iterator().next();
                } else {
                    resource = navigationPresenter.getSelectedFolder();
                }
                dialog.show(resource);
                dialog.addOkButtonSelectHandler(selectEvent -> {
                    doRenameDiskResource(resource, dialog.getFieldText());
                });
            }
        });

    }

    @Override
    public void onRestoreDiskResourcesSelected(RestoreDiskResourcesSelected event) {
        final List<DiskResource> selectedResources = getSelectedDiskResources();

        if (selectedResources == null || selectedResources.isEmpty()) {
            return;
        }

        mask(""); //$NON-NLS-1$

        Folder currentFolder = navigationPresenter.getSelectedFolder();

        DiskResourceRestoreCallback callback = new DiskResourceRestoreCallback(this,
                                                                               this,
                                                                               drFactory,
                                                                               navigationPresenter.getSelectedFolder(),
                                                                               selectedResources);
        if (gridViewPresenter.isSelectAllChecked() && diskResourceUtil.isTrash(currentFolder)) {
            diskResourceService.restoreAll(callback);
        } else {
            HasPaths request = drFactory.pathsList().as();
            request.setPaths(diskResourceUtil.asStringPathList(selectedResources));
            diskResourceService.restoreDiskResource(request, callback);
        }
    }

    @Override
    public void onRootFoldersRetrieved(RootFoldersRetrievedEvent event) {
        DiskResourceFavorite diskResourceFavorite = drFactory.getFavortieFolder().as();
        String id = userInfo.getHomePath() + FAVORITES_FOLDER_PATH;
        diskResourceFavorite.setId(id);
        diskResourceFavorite.setPath(id);
        diskResourceFavorite.setName(FAVORITES_FOLDER_NAME);
        navigationPresenter.addFolder(diskResourceFavorite);
    }

    @Override
    public void onSendToCogeSelected(SendToCogeSelected event) {
        // There is typically only one resource used.
        for (DiskResource resource : event.getResourcesToSend()) {
            InfoType infoType = InfoType.fromTypeString(resource.getInfoType());
            if (infoType == null || !diskResourceUtil.isGenomeVizInfoType(infoType)) {

                announcer.schedule(new ErrorAnnouncementConfig(appearance.unsupportedCogeInfoType()));
                return;
            }
            eventBus.fireEvent(new RequestSendToCoGeEvent((File)resource));
        }
    }

    @Override
    public void onSendToEnsemblSelected(SendToEnsemblSelected event) {
        final List<DiskResource> resourcesToSend = event.getResourcesToSend();

        boolean allGenomeAndIndexTypes = resourcesToSend.stream().allMatch(resource -> {
            final String path = resource.getPath();
            InfoType infoType = InfoType.fromTypeString(resource.getInfoType());

            return diskResourceUtil.isGenomeIndexFile(path) || diskResourceUtil.isEnsemblInfoType(infoType);
        });

        if (allGenomeAndIndexTypes) {
            eventBus.fireEvent(new RequestSendToEnsemblEvent(resourcesToSend));
        } else {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.unsupportedEnsemblInfoType()));
        }
    }

    @Override
    public void onSendToTreeViewerSelected(SendToTreeViewerSelected event) {
        // There is typically only one resource used.
        for (DiskResource resource : event.getResourcesToSend()) {
            InfoType infoType = InfoType.fromTypeString(resource.getInfoType());
            if (infoType == null || !diskResourceUtil.isTreeInfoType(infoType)) {

                announcer.schedule(new ErrorAnnouncementConfig(appearance.unsupportedTreeInfoType()));
                return;
            }
            eventBus.fireEvent(new RequestSendToTreeViewerEvent((File)resource));
        }
    }

    // </editor-fold>

    @Override
    public void cleanUp() {
        navigationPresenter.cleanUp();
    }

    @Override
    public Folder convertToFolder(DiskResource selectedItem) {
        return diskResourceService.convertToFolder(selectedItem);
    }

    @Override
    public void deSelectDiskResources() {
        gridViewPresenter.deSelectDiskResources();
    }

    @Override
    public void onCreateNewFolderConfirmed(CreateNewFolderConfirmed event) {
        Folder parentFolder = event.getParentFolder();
        String newFolderName = event.getFolderName();
        view.mask(appearance.createFolderLoadingMask());
        diskResourceService.createFolder(parentFolder,
                                         newFolderName,
                                         new CreateFolderCallback(parentFolder, view));
    }

    @Override
    public void onCreateNcbiSraFolderStructureSubmitted(CreateNcbiSraFolderStructureSubmitted event) {
        Folder selectedFolder = event.getSelectedFolder();
        String projectName = event.getProjectText();
        Integer numSample = event.getBioSampleNumber();
        Integer numLibs = event.getLibraryNumber();

        view.mask(appearance.createFolderLoadingMask());
        String[] paths = new String[numSample * numLibs];
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= numSample; i++) {
            for (int j = 1; j <= numLibs; j++) {
                list.add("BioProject_" + projectName + "/" + "BioSample" + i + "/" + "BioSample" + i
                        + "Library" + j);
            }
        }

        list.toArray(paths);
        diskResourceService.createNcbiSraFolderStructure(selectedFolder,
                                                         paths,
                                                         new NcbiSraSetupCompleteCallback(this,
                                                                                          selectedFolder,
                                                                                          view));
    }

    @Override
    public void doMoveDiskResources(Folder targetFolder, List<DiskResource> resources) {
        if (diskResourceUtil.inTrash(targetFolder)) {
            delete(resources, appearance.deleteMsg());
            return;
        }

        view.mask(appearance.moveDiskResourcesLoadingMask());

        Folder parent = navigationPresenter.getSelectedFolder();
        if (diskResourceUtil.contains(resources, parent)) {
            parent = navigationPresenter.getParent(parent);
        }

        DiskResourceMoveCallback callback = new DiskResourceMoveCallback(view);

        if (gridViewPresenter.isSelectAllChecked()) {
            diskResourceService.moveContents(parent, targetFolder, callback);
        } else {
            diskResourceService.moveDiskResources(parent, targetFolder, resources, callback);
        }
    }

    @Override
    public void doRenameDiskResource(final DiskResource dr, final String newName) {
        if (dr != null && !dr.getName().equals(newName)) {
            view.mask(appearance.renameDiskResourcesLoadingMask());
            diskResourceService.renameDiskResource(dr, newName, new RenameDiskResourceCallback(dr, view));
        }
    }

    @Override
    public List<DiskResource> getSelectedDiskResources() {
        return gridViewPresenter.getSelectedDiskResources();
    }

    @Override
    public Folder getSelectedFolder() {
        return navigationPresenter.getSelectedFolder();
    }

    @Override
    public void go(HasOneWidget container, boolean collapseDetailsPanel) {
        container.setWidget(view);
        // JDS Re-select currently selected folder in order to load center
        // panel.
        navigationPresenter.setSelectedFolder(navigationPresenter.getSelectedFolder());
        view.setDetailsCollapsed(collapseDetailsPanel);
        if (infoTypes == null) {
            fetchInfoTypes();
        } else {
            detailsViewPresenter.getView().setInfoTypes(infoTypes);
        }
    }

    @Override
    public void go(HasOneWidget container,
                   HasPath folderToSelect,
                   final List<? extends HasId> diskResourcesToSelect) {

        if ((folderToSelect == null) || Strings.isNullOrEmpty(folderToSelect.getPath())) {
            go(container, true);
        } else {
            container.setWidget(view);
            navigationPresenter.setSelectedFolder(folderToSelect);
            setSelectedDiskResourcesById(diskResourcesToSelect);
        }
    }

    @Override
    public boolean isDetailsCollapsed() {
      return view.isDetailsCollapsed();
    }

    @Override
    public ColumnModel<DiskResource> getColumns() {
        return view.getColumns();
    }

    /**
     * Set column preferences.
     *
     * @param preferences A map of column preferences.
     */
    @Override
    public void setColumnPreferences(Map<String, String> preferences) {
        view.setColumnPreferences(preferences);
    }

    @Override
    public void mask(String loadingMask) {
        view.mask((Strings.isNullOrEmpty(loadingMask)) ? appearance.loadingMask() : loadingMask);
    }

    @Override
    public void onOpenTrashFolderSelected(OpenTrashFolderSelected event) {
        final HasPath hasPath = CommonModelUtils.getInstance()
                                                .createHasPathFromString(userInfo.getTrashPath());
        navigationPresenter.setSelectedFolder(hasPath);
    }

    @Override
    public void setSelectedDiskResourcesById(final List<? extends HasId> diskResourcesToSelect) {
        gridViewPresenter.setSelectedDiskResourcesById(diskResourcesToSelect);
    }

    @Override
    public void setSelectedFolderByPath(final HasPath folderToSelect) {
        navigationPresenter.setSelectedFolder(folderToSelect);
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.DISK_RESOURCE_VIEW);
    }

    @Override
    public String getWestPanelWidth() {
        return view.getWestPanelWidth();
    }

    @Override
    public void setWestPanelWidth(String width) {
        view.setWestPanelWidth(width);
    }

    @Override
    public void unmask() {
        view.unmask();
    }

        boolean canDragDataToTargetFolder(final Folder targetFolder, final Collection<DiskResource> dropData) {
        if (targetFolder instanceof DiskResourceQueryTemplate) {
            return false;
        }

        if (targetFolder.isFilter()) {
            return false;
        }

        // Assuming that ownership is of no concern.
        for (DiskResource dr : dropData) {
            // if the resource is a direct child of target folder
            if (diskResourceUtil.isChildOfFolder(targetFolder, dr)) {
                return false;
            }

            if (dr instanceof Folder) {
                if (targetFolder.getPath().equals(dr.getPath())) {
                    return false;
                }

                // cannot drag an ancestor (parent, grandparent, etc) onto a
                // child and/or descendant
                if (diskResourceUtil.isDescendantOfFolder((Folder)dr, targetFolder)) {
                    return false;
                }
            }
        }

        return true;
    }

    void doEmptyTrash() {
        view.mask(appearance.loadingMask());
        diskResourceService.emptyTrash(userInfo.getUsername(), new DataCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(String result) {
                refreshFolder(navigationPresenter.getFolderByPath(userInfo.getTrashPath()));
            }
        });
    }

    void confirmDelete(final List<DiskResource> drSet) {
        final MessageBox confirm = getEmptyTrashMessageBox(appearance.warning());

        confirm.addDialogHideHandler(event -> {
            if (PredefinedButton.YES.equals(event.getHideButton())) {
                delete(drSet, appearance.deleteTrash());
            }
        });

        confirm.show();
    }

    void delete(List<DiskResource> drSet, String announce) {
        view.mask(appearance.loadingMask());
        Folder selectedFolder = navigationPresenter.getSelectedFolder();
        final DECallback<HasPaths> callback = new DiskResourceDeleteCallback(drSet,
                                                                             selectedFolder,
                                                                             view,
                                                                             announce);

        if (gridViewPresenter.isSelectAllChecked() && selectedFolder != null) {
            diskResourceService.deleteContents(selectedFolder.getPath(), callback);

        } else {
            diskResourceService.deleteDiskResources(drSet, callback);
        }
    }

    @Override
    public void onDNDDiskResourcesCompleted(DNDDiskResourcesCompleted event) {
        doMoveDiskResources(event.getTargetFolder(), event.getResources());
    }

    ConfirmMessageBox getEmptyTrashMessageBox(String title) {
        return new ConfirmMessageBox(title,
                                     appearance.emptyTrashWarning());
    }

}
